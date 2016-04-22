using ComInLan.Model.Base;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;

namespace ComInLan.Model
{
	public class Server : BaseModel, IServer
	{
		public Server()
		{

		}

		public IPAddress Address { get; set; }

		public string Checksum { get; set; }

		public string Id { get; set; }

		public string Name { get; set; }

		public int Port { get; set; }

		public long RefreshTime { get; set; }

		public void Refresh()
		{
			RefreshTime = (long)(DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0)).TotalSeconds;
		}
	}
}
