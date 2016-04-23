using System;
using System.Linq;
using System.Collections.Generic;
using ComInLan.Model;
using ComInLan.Model.Packet;

namespace ComInLan.Server
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
			ConfirmingClients = new List<IClient>();
			ConnectedClients = new List<IClient>();
			BlackClients = new List<IClient>();
			WhiteClients = new List<IClient>();
		}

		public List<IClient> ConfirmingClients { get; private set; }
		public List<IClient> ConnectedClients { get; private set; }
		public List<IClient> BlackClients { get; private set; }
		public List<IClient> WhiteClients { get; private set; }


		protected override void HandleProtocolPacket(ClientPacket protocolPacket)
		{

		}

		protected override void HandleDatapacket(ClientPacket dataPacket)
		{

		}

		protected override void HandleRefreshPacket(ClientPacket freshPacket)
		{
			throw new NotImplementedException();
		}
	}
}