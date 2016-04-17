using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.NetworkInformation;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Timers;
using Timer = System.Timers.Timer;

namespace ComInLanWindows
{
    public class ComInLanServer : IComInLanServer
    {
        public const int BroadcastPort = 55176;
        
        public Guid Id { get; private set; }

        public string Name { get; set; }

        public bool IsRunning { get; private set; }

        public int ListeningPort { get; private set; }

        public event ComInLanServerHandler DataReceived;

        private Socket _broadcastSocket;
        private Timer _advertisingTimer;
        private IServer _broadcastPacket;

        private UdpClient _listener;
        private IPEndPoint _groupEP;

        public ComInLanServer()
        {
            Id = Guid.NewGuid();

            ListeningPort = FindAvaiablePortToListen();

            _broadcastSocket = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);
            _broadcastPacket = new Server()
            {
                ListeningPort = ListeningPort
            };

            _advertisingTimer = new Timer(5000)
            {
                AutoReset = true,
            };

            _advertisingTimer.Elapsed += Advertise;

            _listener = new UdpClient(ListeningPort);
            _groupEP = new IPEndPoint(IPAddress.Any, BroadcastPort);

            IsRunning = false;
        }

        public void Start()
        {
            _advertisingTimer.Start();
            Listen();

            IsRunning = true;
        }

        public void Stop()
        {
            _advertisingTimer.Stop();
            _listenCTSource.Cancel();
            _listener.Close();

            IsRunning = false;
        }



        public void ChangId()
        {
            Id = Guid.NewGuid();
        }

        private CancellationTokenSource _listenCTSource;
        private Task Listen()
        {
            _listenCTSource = new CancellationTokenSource();
            var cancellationToken = _listenCTSource.Token;

            return Task.Factory.StartNew(delegate
            {
                while (!cancellationToken.IsCancellationRequested)
                {
                    var bytes = _listener.Receive(ref _groupEP);
                    var packetJson = Encoding.ASCII.GetString(bytes);

                    //TODO: write code to excute later
                    DataReceived(this, packetJson);

                    Console.WriteLine("Content " + packetJson);
                }

            }, cancellationToken);
        }

        private void Advertise(object sender, ElapsedEventArgs e)
        {
            _broadcastPacket.Name = Name;
            _broadcastPacket.Id = Id;

            var packetJson = JsonConvert.SerializeObject(_broadcastPacket);
            var sendbuf = Encoding.ASCII.GetBytes(packetJson);

            foreach (var i in NetworkInterface.GetAllNetworkInterfaces())
                foreach (var ua in i.GetIPProperties().UnicastAddresses)
                {
                    if (ua.Address.AddressFamily == AddressFamily.InterNetwork)
                    {
                        var address = GetBroadcastAddress(ua);
                        var broadcastEp = new IPEndPoint(address, BroadcastPort);
                        _broadcastSocket.SendTo(sendbuf, broadcastEp);

                        Console.WriteLine("Sent " + packetJson + " by " + address.ToString());
                    }
                }
        }

        private IPAddress GetBroadcastAddress(UnicastIPAddressInformation ua)
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

        private int FindAvaiablePortToListen(int startingPort = 20032)
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
    }
}