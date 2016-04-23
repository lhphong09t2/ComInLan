using ComInLan.Model.Base;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model.Packet
{
	public interface IClientPacket : IJson
	{
		string Id { get; }
		string Name { get; }
		ClientPacketType Type { get; }
	}
}
