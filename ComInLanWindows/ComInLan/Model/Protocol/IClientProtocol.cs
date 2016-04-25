using ComInLan.Model.Base;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model.Protocol
{
	public interface IClientProtocol : IJson
	{
		ClientMessage Message { get; }
	}
}
