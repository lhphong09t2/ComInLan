using ComInLan.Model;
using ComInLan.Model.Packet;
using Newtonsoft.Json;
using System;
using System.Net;
using System.Net.NetworkInformation;
using System.Net.Sockets;
using System.Timers;
using Timer = System.Timers.Timer;

namespace ComInLan
{
	public abstract class BroadcastServer : NetworkUtility, IBroadcastServer
	{
		public const int ClientUdpPort = 55176;
		public const int AdvertisingPeriod = 5000;

		public string Id { get { return _broadcastPacket.Id; } }

		public string Name { get; set; }

		public bool IsRunning { get; private set; }

		public int ListeningPort { get { return _broadcastPacket.Data.ListeningPort; } }

		public abstract string DomainId { get; }

		public event BroadcastServerHandler DataReceived;

		private Timer _advertisingTimer;
		private ServerPacket<IBroadcastData> _broadcastPacket;

		public BroadcastServer()
		{
			var broadcastData = new BroadcastData()
			{
				ListeningPort = FindAvaiablePortToListen()
			};

			_broadcastPacket = new ServerPacket<IBroadcastData>()
			{
				Data = broadcastData,
				Id = Guid.NewGuid().ToString(),
				DomainId = DomainId,
				Type = ServerPacketType.Broadcast
			};

			_advertisingTimer = new Timer(AdvertisingPeriod)
			{
				AutoReset = true,
			};

			_advertisingTimer.Elapsed += Advertise;

			IsRunning = false;

			InitUdp();
		}

		public virtual void Start()
		{
			_advertisingTimer.Start();
			IsRunning = true;
		}

		public virtual void Stop()
		{
			_advertisingTimer.Stop();
			IsRunning = false;
		}

		public void ChangId()
		{
			_broadcastPacket.Id = Guid.NewGuid().ToString();
		}

		protected override void OnUdpDataReceived(string dataJson, IPAddress address)
		{

		}

		private void Advertise(object sender, ElapsedEventArgs e)
		{
			_broadcastPacket.Name = Name;

			var packetJson = JsonConvert.SerializeObject(_broadcastPacket);

			foreach (var i in NetworkInterface.GetAllNetworkInterfaces())
				foreach (var ua in i.GetIPProperties().UnicastAddresses)
				{
					if (ua.Address.AddressFamily == AddressFamily.InterNetwork)
					{
						var address = GetBroadcastAddress(ua);
						SendUdp(packetJson, address, ClientUdpPort);
					}
				}
		}
	}
}
