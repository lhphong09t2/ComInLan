using ComInLan.Model.Base;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model.Protocol
{
	public interface IServerProtocol : IJson
	{
		ServerMessage Message { get; }
	}
}
