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
	public class ComInLanServer : BroadcastServer, IComInLanServer
	{
		public override string DomainId
		{
			get
			{
				return "ComInLan";
			}
		}

		public ComInLanServer()
		{

		}

		protected override void OnUdpDataReceived(string dataJson, IPAddress address)
		{

		}

		protected override void OnTcpDataReceived(string dataJson, IPAddress address)
		{
		}
	}
}