using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model
{
	public delegate void ServerStateEventHandler(IServer server);
	public delegate void SeverDataEventHandler(IServer server, string dataJson);

	public interface IServer
	{
		string Id { get; }
		string Name { get; }
		int Port { get; }

		IPAddress Address { get; }
		ServerState State { get; }
		String Checksum { get; }
		long RefreshTime { get; }

		event ServerStateEventHandler StateChanged;
		event SeverDataEventHandler DataReceived;
	}
}
