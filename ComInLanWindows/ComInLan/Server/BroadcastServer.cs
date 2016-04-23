﻿using ComInLan.Model;
using ComInLan.Model.Base;
using ComInLan.Model.Packet;
using Newtonsoft.Json;
using System;
using System.Net;
using System.Net.NetworkInformation;
using System.Net.Sockets;
using System.Timers;
using Timer = System.Timers.Timer;

namespace ComInLan.Server
{
	public abstract class BroadcastServer : NetworkUtility, IBroadcastServer
	{
		public readonly int[] ClientUdpPort = new int[] { 55176, 23435, 34523, 45349 };
		public const int AdvertisingPeriod = 5000;

		public string Id { get { return _broadcastPacket.Id; } }

		public string Name { get; set; }

		public bool IsRunning { get; private set; }

		public int ListeningPort { get; private set; }

		public abstract string DomainId { get; }

		private Timer _advertisingTimer;
		private ServerPacket _broadcastPacket;

		public BroadcastServer()
		{
			ListeningPort = FindAvaiablePortToListen();

			var broadcastData = new BroadcastData()
			{
				ListeningPort = ListeningPort
			};

			_broadcastPacket = new ServerPacket()
			{
				DataJson = JsonConvert.SerializeObject(broadcastData),
				Id = Guid.NewGuid().ToString(),
				DomainId = DomainId,
				Type = ServerPacketType.Broadcast
			};

			_advertisingTimer = new Timer(AdvertisingPeriod)
			{
				AutoReset = true,
			};

			_advertisingTimer.Elapsed += delegate {
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

						foreach (var port in ClientUdpPort)
						{
							SendUdp(packetJson, address, port);
						}
					}
				}
		}
	}
}