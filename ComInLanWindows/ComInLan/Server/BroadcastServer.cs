using ComInLan.Model;
using ComInLan.Model.Base;
using ComInLan.Model.Packet;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.NetworkInformation;
using System.Net.Sockets;
using System.Timers;
using System.Windows.Forms;
using Timer = System.Timers.Timer;

namespace ComInLan.Server
{
	public abstract class BroadcastServer : NetworkUtility, IBroadcastServer
	{
		public string Id { get; private set; }

		public string Name { get; set; }

		public bool IsRunning { get; private set; }

		public int ListeningPort { get; private set; }

		public abstract string DomainId { get; }

		private Timer _advertisingTimer;
		private ServerPacket _broadcastPacket;

		public List<IClient> Clients { get; private set; }

		protected Control Control { get; private set; }

		public BroadcastServer(Control control)
		{
			Control = control;
			Clients = new List<IClient>();

			Id = Guid.NewGuid().ToString();
			ListeningPort = FindAvaiablePortToListen();

			var broadcastData = new BroadcastData()
			{
				ListeningPort = ListeningPort
			};

			_broadcastPacket = new ServerPacket()
			{
				DataJson = JsonConvert.SerializeObject(broadcastData),
				Id = Id,
				DomainId = DomainId,
				Type = ServerPacketType.Broadcast
			};

			_advertisingTimer = new Timer(CConstant.AdvertisingPeriod)
			{
				AutoReset = true,
			};

			_advertisingTimer.Elapsed += delegate
			{
				Advertise();
			};

			IsRunning = false;

			InitUdp();
		}

		public virtual bool Start()
		{
			if (StartUdp(ListeningPort))
			{
				_advertisingTimer.Start();
				Advertise();

				IsRunning = true;
			}
			else
			{
				return false;
			}

			return true;
		}

		public virtual void Stop()
		{
			StopUdp();
			_advertisingTimer.Stop();
			IsRunning = false;
		}

		public void ChangId()
		{
			_broadcastPacket.Id = Guid.NewGuid().ToString();
		}

		protected override void OnUdpDataReceived(string dataJson, IPAddress address)
		{
			var clientPacket = JsonConvert.DeserializeObject<ClientPacket>(dataJson);

			switch (clientPacket.Type)
			{
				case ClientPacketType.Refresh:
					HandleRefreshPacket(clientPacket);
					break;
				case ClientPacketType.Protocol:
					HandleProtocolPacket(clientPacket, address);
					break;
				case ClientPacketType.Data:
					HandleDataPacket(clientPacket);
					break;
			}
		}

		private void Advertise()
		{
			_broadcastPacket.Name = Name;

			var packetJson = JsonConvert.SerializeObject(_broadcastPacket);

			foreach (var networkInterface in NetworkInterface.GetAllNetworkInterfaces())
				foreach (var ua in networkInterface.GetIPProperties().UnicastAddresses)
				{
					if (ua.Address.AddressFamily == AddressFamily.InterNetwork)
					{
						var address = GetBroadcastAddress(ua);

						foreach (var port in CConstant.UdpListenerPort)
						{
							SendUdp(packetJson, address, port);
						}
					}
				}
		}

		private void HandleDataPacket(ClientPacket dataPacket)
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

		protected abstract void HandleProtocolPacket(ClientPacket protocolPacket, IPAddress address);

		protected abstract void HandleRefreshPacket(ClientPacket freshPacket);
	}
}