using System;
using System.Linq;
using System.Collections.Generic;
using ComInLan.Model;
using ComInLan.Model.Packet;
using Newtonsoft.Json;
using ComInLan.Model.Protocol;
using System.Windows.Forms;
using System.Net;

namespace ComInLan.Server
{
	public class ComInLanServer : BroadcastServer, IComInLanServer
	{
		public event ClientEventHandler ClientNew;
		public event ClientEventHandler ClientRemoved;
		public event ClientEventHandler ClientChanged;
		public event ClientsEventHandler ClientsChanged;

		public override string DomainId
		{
			get
			{
				return CConstant.DomainId;
			}
		}

		public ComInLanServer(Control control) : base(control) { }

		protected override void HandleProtocolPacket(ClientPacket protocolPacket, IPAddress address)
		{
			var protocol = JsonConvert.DeserializeObject<ClientProtocol>(protocolPacket.DataJson);

			switch (protocol.Message)
			{
				case ClientMessage.RequestConnect:
					HandleRequestConnectMessage(protocolPacket, protocol, address);
					break;
				case ClientMessage.Passcode:
					HandlePasscodeMessage(protocolPacket.Id, protocol.DataJson);
					break;
				case ClientMessage.Disconnect:
					HandleDisconnectMessage(protocolPacket.Id);
					break;
			}
		}

		protected override void HandleRefreshPacket(ClientPacket freshPacket)
		{
			var client = Clients.FirstOrDefault(x => x.Id == freshPacket.Id) as CClient;

			if (client == null)
			{
				return;
			}

			client.Refresh();

			if (client.Name != freshPacket.Name)
			{
				client.Name = freshPacket.Name;

				Control.Invoke((MethodInvoker)delegate
				{
					ClientChanged?.Invoke(client);
				});
			}
		}

		private void HandleRequestConnectMessage(ClientPacket protocolPacket, IClientProtocol protocol, IPAddress address)
		{
			var client = Clients.FirstOrDefault(x => x.Id == protocolPacket.Id) as CClient;

			if (client != null)
			{
				return;
			}

			var port = int.Parse(protocol.DataJson);

			if (!CConstant.UdpListenerPort.Contains(port))
			{
				return;
			}

			client = new CClient()
			{
				Id = protocolPacket.Id,
				Name = protocolPacket.Name,
				Port = port,
				Address = address,
			};

			client.CalculateChecksum();
			client.Refresh();

			lock (Clients)
			{
				Clients.Add(client);
			}

			Control.Invoke((MethodInvoker)delegate
			{
				ClientNew?.Invoke(client);

				ClientsChanged?.Invoke(Clients);

				var generator = new Random();
				var r = generator.Next(1, 1000000);
				var passcode = r.ToString().PadLeft(6, '0');
				client.Passcode = passcode;
			});

			var responseProtocol = new ServerProtocol()
			{
				Message = ServerMessage.RequestPasscode
			};

			SendServerPacket(ServerPacketType.Protocol, JsonConvert.SerializeObject(responseProtocol), client);

			Control.Invoke((MethodInvoker)delegate
			{
				client.State = ClientState.WaitingPasscode;
			});
		}

		private void HandlePasscodeMessage(String id, String passcode)
		{
			var client = Clients.FirstOrDefault(x => x.Id == id && x.State == ClientState.WaitingPasscode) as CClient;

			if (client == null || client.State != ClientState.WaitingPasscode)
			{
				return;
			}

			var responseProtocol = new ServerProtocol();

			if (passcode == client.Passcode)
			{
				Control.Invoke((MethodInvoker)delegate
				{
					client.State = ClientState.Accepted;
				});

				responseProtocol.Message = ServerMessage.Accept;
			}
			else
			{
				RemoveClient(client);

				responseProtocol.Message = ServerMessage.Refuse;
			}

			SendServerPacket(ServerPacketType.Protocol, JsonConvert.SerializeObject(responseProtocol), client);
		}

		private void HandleDisconnectMessage(String id)
		{
			var client = Clients.FirstOrDefault(x => x.Id == id) as CClient;

			if (client == null || !(client.State == ClientState.Accepted || client.State == ClientState.WaitingPasscode))
			{
				return;
			}

			RemoveClient(client);
		}

		private void RemoveClient(CClient client)
		{
			Control.Invoke((MethodInvoker)delegate
			{
				client.State = ClientState.Refused;
			});

			lock (Clients)
			{
				Clients.Remove(client);
			}

			Control.Invoke((MethodInvoker)delegate
			{
				ClientRemoved?.Invoke(client);

				ClientsChanged?.Invoke(Clients);
			});
		}

		private void SendServerPacket(ServerPacketType type, string dataJson, IClient client)
		{
			var reponsePacket = new ServerPacket()
			{
				Id = Id,
				DomainId = DomainId,
				Name = Name,
				Type = ServerPacketType.Protocol,
				DataJson = dataJson
			};

			SendUdp(JsonConvert.SerializeObject(reponsePacket), client.Address, client.Port);
		}
	}
}