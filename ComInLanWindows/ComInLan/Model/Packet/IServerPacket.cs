using ComInLan.Model.Base;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model.Packet
{
	public interface IServerPacket : IJson
	{
		string Id { get; }
		string DomainId { get; }
		string Name { get; }
		ServerPacketType Type { get; }
	}
}
