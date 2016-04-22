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
		IPAddress Address { get; }
		int Port { get; }
		String Checksum { get; }
		long RefreshTime { get; }
	}
}
