using ComInLan.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Server
{
	public interface IComInLanServer : IBroadcastServer
	{
		List<IClient> ConfirmingClients { get; }
		List<IClient> ConnectedClients { get; }
		List<IClient> BlackClients { get; }
		List<IClient> WhiteClients { get; }
	}
}
