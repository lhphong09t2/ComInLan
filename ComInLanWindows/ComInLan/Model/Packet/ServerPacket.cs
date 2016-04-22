﻿using ComInLan.Model.Base;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model.Packet
{
	public class ServerPacket<T> : BaseModel, IServerPacket<T>
	{
		public string DomainId { get; set; }

		public string Id { get; set; }

		public string Name { get; set; }

		public ServerPacketType Type { get; set; }

		public T Data { get; set; }
	}
}
