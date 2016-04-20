using ComInLan.Model;
using Newtonsoft.Json;
using System;
using System.Net;
using System.Net.NetworkInformation;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Timers;
using Timer = System.Timers.Timer;

namespace ComInLan
{
	public class ComInLanServer : NetworkUtility, IComInLanServer
	{
		public const int BroadcastPort = 55176;

		public string Id { get { return _broadcastPacket.Id; } }

		public string Name { get; set; }

		public bool IsRunning { get; private set; }

		public int ListeningPort { get { return _broadcastPacket.Data.ListeningPort; } }

		public event ComInLanServerHandler DataReceived;

		private Timer _advertisingTimer;
		private ServerPacket<IBroadcastData> _broadcastPacket;

		public ComInLanServer()
		{
			var broadcastData = new BroadcastData()
			{
				ListeningPort = FindAvaiablePortToListen()
			};

			_broadcastPacket = new ServerPacket<IBroadcastData>()
			{
				Data = broadcastData,
				Id = Guid.NewGuid().ToString(),
				DomainId = "ComInLanServer",
				Type = ServerPacketType.Broadcast
			};

			_advertisingTimer = new Timer(5000)
			{
				AutoReset = true,
			};

			_advertisingTimer.Elapsed += Advertise;

			IsRunning = false;

			InitUdp();
		}

		public void Start()
		{
			_advertisingTimer.Start();
			IsRunning = true;
		}

		public void Stop()
		{
			_advertisingTimer.Stop();
			IsRunning = false;
		}

		public void ChangId()
		{
			_broadcastPacket.Id = Guid.NewGuid().ToString();
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
						SendUdp(packetJson, address, BroadcastPort);
					}
				}
		}

		protected override void OnUdpDataReceived(string dataJson)
		{
		}

		protected override void OnTcpDataReceived(string dataJson)
		{
		}
	}
}