using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model
{
	public delegate void ClientPasscodeEventHandler(IClient client);
	public delegate void ClientStateEventHandler(IClient client);
	public delegate void ClientDataEventHandler(IClient client, string dataJson);

	public interface IClient
	{
		event ClientPasscodeEventHandler PasscodeCreated;
		event ClientStateEventHandler StateChanged;
		event ClientDataEventHandler DataReceived;

		string Id { get; }
		string Name { get; }
		int Port { get; }

		IPAddress Address { get; }
		string Checksum { get; }
		string Passcode { get; }
		long RefreshTime { get; }
		ClientState State { get; }
	}
}
