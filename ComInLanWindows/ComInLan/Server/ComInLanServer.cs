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
		public event ClientEventHandler ClientChanged;
		public event ClientEventHandler ClientRemoved;
		public event ClientsEventHandler ClientsChanged;

		public List<IClient> Clients { get; private set; }

		public override string DomainId
		{
			get
			{
				return "ComInLan";
			}
		}

		protected Control Control { get; private set; }

		public ComInLanServer(Control control)
		{
			Clients = new List<IClient>();
			Control = control;
		}

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

		protected override void HandleDataPacket(ClientPacket dataPacket)
		{
			var client = Clients.FirstOrDefault(x => x.Id == dataPacket.Id && x.State == ClientState.Accepted) as CClient;

			if (client != null)
			{
				Control.Invoke((MethodInvoker)delegate
				{
					client.CallDataReceived(dataPacket.DataJson);
				});
			}
		}

		protected override void HandleRefreshPacket(ClientPacket freshPacket)
		{
			var client = Clients.FirstOrDefault(x => x.Id == freshPacket.Id) as CClient;

			if (client != null)
			{
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
		}

		private void HandleRequestConnectCommand(ClientPacket protocolPacket, IClientProtocol protocol, IPAddress address)
		{
			var client = Clients.FirstOrDefault(x => x.Id == protocolPacket.Id) as CClient;

			if (client == null)
			{
				client = new CClient()
				{
					Id = protocolPacket.Id,
					Name = protocolPacket.Name,
					Port = int.Parse(protocol.DataJson),
					Address = address,
					State = ClientState.WaitingPasscode
				};

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

				client.CalculateChecksum();
				client.Refresh();

				var responseProtocol = new ServerProtocol()
				{
					Command = ServerCommand.RequestPasscode
				};

				var reponsePacket = new ServerPacket()
				{
					Id = Id,
					DomainId = DomainId,
					Name = Name,
					Type = ServerPacketType.Protocol,
					DataJson = JsonConvert.SerializeObject(responseProtocol)
				};

				SendUdp(JsonConvert.SerializeObject(reponsePacket), client.Address, client.Port);
			}
		}

		private void HandlePasscodeComand(String id, String passcode)
		{
			var client = Clients.FirstOrDefault(x => x.Id == id && x.State == ClientState.WaitingPasscode) as CClient;

			if (client != null)
			{
				Control.Invoke((MethodInvoker)delegate
				{
					client.State = passcode == client.Passcode ? ClientState.Accepted : ClientState.Refused;
				});


				var responseProtocol = new ServerProtocol()
				{
					Command = passcode == client.Passcode ? ServerCommand.Accept : ServerCommand.Refuse
				};

				var reponsePacket = new ServerPacket()
				{
					Id = Id,
					DomainId = DomainId,
					Name = Name,
					Type = ServerPacketType.Protocol,
					DataJson = JsonConvert.SerializeObject(responseProtocol)
				};

				SendUdp(JsonConvert.SerializeObject(reponsePacket), client.Address, client.Port);
			}
		}

		private void HandleDisconnectCommand(String id)
		{
			var client = Clients.FirstOrDefault(x => x.Id == id && x.State == ClientState.Accepted) as CClient;

			if (client != null)
			{
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
		}
	}
}