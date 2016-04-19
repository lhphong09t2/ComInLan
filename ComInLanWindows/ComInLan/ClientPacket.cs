using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan
{
	internal class ClientPacket<T> : IClientPacket<T>
	{
		public string Id { get; set; }
		public string Name { get; set; }
		public T Data { get; set; }
	}
}
