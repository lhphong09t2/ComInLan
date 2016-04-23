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

		//--------------Created from packet------------------//
		public string Id { get; set; }

		public string Name { get; set; }

		public int Port { get; set; }


		//--------------Created by app------------------//
		public IPAddress Address { get; set; }

		public string Checksum { get; set; }

		public long RefreshTime { get; private set; }

		public ClientState State { get; set; }

		public void Refresh()
		{
			RefreshTime = GetCurrentUnixTimestamp();
		}
	}
}
