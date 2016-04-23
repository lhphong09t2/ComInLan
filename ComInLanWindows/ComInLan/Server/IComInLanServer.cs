using ComInLan.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Server
{
	public delegate void ClientEventHandler(IClient client);
	public delegate void ClientsEventHandler(List<IClient> clients);

	public interface IComInLanServer : IBroadcastServer
	{
		event ClientEventHandler ClientNew;
		event ClientEventHandler ClientChanged;
		event ClientEventHandler ClientRemoved;
		event ClientsEventHandler ClientsChanged;

		List<IClient> Clients { get; }
	}
}
