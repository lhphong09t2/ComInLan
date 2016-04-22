using System.Collections.Generic;
using System.Net;
using System.Net.NetworkInformation;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;
using System.Linq;
using System;
using System.Text;

namespace ComInLan
{
	public abstract class NetworkUtility
	{
		protected const int UdpPacketSizeInByte = 4096;

		private Socket _udpSocket;
		private UdpClient _udpClient;
		private IPEndPoint _groupEP;

		protected void InitUdp()
		{
			_udpSocket = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);
		}

		protected void SendUdp(string dataJson, IPAddress address, int port)
		{
			_udpSocket.SendTo(Encoding.UTF8.GetBytes(dataJson), new IPEndPoint(address, port));
		}

		protected bool StartUdp(int udpListeningPort)
		{
			try
			{
				_udpClient = new UdpClient(udpListeningPort);
				_groupEP = new IPEndPoint(IPAddress.Any, udpListeningPort);
			}
			catch (Exception e)
			{
				return false;
			}

			ListenUdp();
			return true;
		}

		protected void StopUdp()
		{
			_listenCTSource.Cancel();
			_udpClient.Close();
		}

		protected void InitTcp()
		{
			//TODO write code to handle this one
		}

		protected void SendTcp(byte[] data)
		{
			//TODO write code to handle this one
		}

		protected void StartTcp()
		{
			//TODO write code to handle this one
		}

		protected void StopTcp()
		{
			//TODO write code to handle this one
		}

		protected abstract void OnUdpDataReceived(string dataJson, IPAddress address);
		//protected abstract void OnTcpDataReceived(string dataJson, IPAddress address);

		private CancellationTokenSource _listenCTSource;
		private Task ListenUdp()
		{
			_listenCTSource = new CancellationTokenSource();
			var cancellationToken = _listenCTSource.Token;

			return Task.Factory.StartNew(delegate
			{
				while (!cancellationToken.IsCancellationRequested)
				{
					var bytes = _udpClient.Receive(ref _groupEP);

					Task.Factory.StartNew(delegate
					{
						OnUdpDataReceived(Encoding.UTF8.GetString(bytes), _groupEP.Address);
					});
				}

			}, cancellationToken);
		}

		public static int FindAvaiablePortToListen(int startingPort = 20032)
		{
			IPEndPoint[] endPoints;
			List<int> portArray = new List<int>();

			IPGlobalProperties properties = IPGlobalProperties.GetIPGlobalProperties();

			//getting active connections
			TcpConnectionInformation[] connections = properties.GetActiveTcpConnections();
			portArray.AddRange(from n in connections
							   where n.LocalEndPoint.Port >= startingPort
							   select n.LocalEndPoint.Port);

			//getting active tcp listners - WCF service listening in tcp
			endPoints = properties.GetActiveTcpListeners();
			portArray.AddRange(from n in endPoints
							   where n.Port >= startingPort
							   select n.Port);

			//getting active udp listeners
			endPoints = properties.GetActiveUdpListeners();
			portArray.AddRange(from n in endPoints
							   where n.Port >= startingPort
							   select n.Port);

			portArray.Sort();

			for (int i = startingPort; i < UInt16.MaxValue; i++)
				if (!portArray.Contains(i))
					return i;

			return 0;
		}

		public static IPAddress GetBroadcastAddress(UnicastIPAddressInformation ua)
		{
			byte[] ipAdressBytes = ua.Address.GetAddressBytes();
			byte[] subnetMaskBytes = ua.IPv4Mask.GetAddressBytes();

			if (ipAdressBytes.Length != subnetMaskBytes.Length)
				throw new ArgumentException("Lengths of IP address and subnet mask do not match.");

			byte[] broadcastAddress = new byte[ipAdressBytes.Length];
			for (int i = 0; i < broadcastAddress.Length; i++)
			{
				broadcastAddress[i] = (byte)(ipAdressBytes[i] | (subnetMaskBytes[i] ^ 255));
			}

			return new IPAddress(broadcastAddress);
		}
	}
}
