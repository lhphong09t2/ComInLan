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
				return "ComInLan";
			}
		}

		public ComInLanServer(Control control) : base(control) { }

		protected override void HandleProtocolPacket(ClientPacket protocolPacket, IPAddress address)
		{
			var protocol = JsonConvert.DeserializeObject<ClientProtocol>(protocolPacket.DataJson);

			switch (protocol.Command)
			{
				case ClientCommand.RequestConnect:
					HandleRequestConnectCommand(protocolPacket, protocol, address);
					break;
				case ClientCommand.Passcode:
					HandlePasscodeComand(protocolPacket.Id, protocol.DataJson);
					break;
				case ClientCommand.Disconnect:
					HandleDisconnectCommand(protocolPacket.Id);
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
					if (ClientChanged != null)
					{
						ClientChanged(client);
					}
				});
			}
		}

		private void HandleRequestConnectCommand(ClientPacket protocolPacket, IClientProtocol protocol, IPAddress address)
		{
			var client = Clients.FirstOrDefault(x => x.Id == protocolPacket.Id) as CClient;

			if (client != null)
			{
				return;
			}

			var port = int.Parse(protocol.DataJson);

			if (!UdpListenerPort.Contains(port))
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
				if (ClientNew != null)
				{
					ClientNew(client);
				}

				if (ClientsChanged != null)
				{
					ClientsChanged(Clients);
				}

				var generator = new Random();
				var r = generator.Next(1, 1000000);
				var passcode = r.ToString().PadLeft(6, '0');
				client.Passcode = passcode;
			});

			var responseProtocol = new ServerProtocol()
			{
				Command = ServerCommand.RequestPasscode
			};

			SendServerPacket(ServerPacketType.Protocol, JsonConvert.SerializeObject(responseProtocol), client);

			Control.Invoke((MethodInvoker)delegate
			{
				client.State = ClientState.WaitingPasscode;
			});
		}

		private void HandlePasscodeComand(String id, String passcode)
		{
			var client = Clients.FirstOrDefault(x => x.Id == id && x.State == ClientState.WaitingPasscode) as CClient;

			if (client == null || client.State != ClientState.WaitingPasscode)
			{
				return;
			}

			Control.Invoke((MethodInvoker)delegate
			{
				client.State = passcode == client.Passcode ? ClientState.Accepted : ClientState.Refused;
			});

			var responseProtocol = new ServerProtocol()
			{
				Command = passcode == client.Passcode ? ServerCommand.Accept : ServerCommand.Refuse
			};

			SendServerPacket(ServerPacketType.Protocol, JsonConvert.SerializeObject(responseProtocol), client);
		}

		private void HandleDisconnectCommand(String id)
		{
			var client = Clients.FirstOrDefault(x => x.Id == id && x.State == ClientState.Accepted) as CClient;

			if (client == null || client.State != ClientState.Accepted)
			{
				return;
			}

			lock (Clients)
			{
				Clients.Remove(client);
			}

			Control.Invoke((MethodInvoker)delegate
			{
				if (ClientChanged != null)
				{
					ClientRemoved(client);
				}

				if (ClientsChanged != null)
				{
					ClientsChanged(Clients);
				}
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