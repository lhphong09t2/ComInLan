using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model
{
	public interface IServer
	{
		string Id { get; }
		string Name { get; }
		int Port { get; }

		IPAddress Address { get; }
		String Checksum { get; }
		long RefreshTime { get; }
		ClientState State { get; }
	}
}
