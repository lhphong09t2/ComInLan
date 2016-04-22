using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model.Packet
{
	public class ClientPacket<T> : IClientPacket<T>
	{
		public string Id { get; set; }
		public string Name { get; set; }
		public ClientPacketType Type { get; set; }
		public T Data { get; set; }
	}
}
