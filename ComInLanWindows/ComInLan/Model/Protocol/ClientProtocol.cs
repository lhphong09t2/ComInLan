using ComInLan.Model.Base;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model.Protocol
{
	public class ClientProtocol : Json, IClientProtocol
	{
		public ClientMessage Message { get; set; }
	}
}
