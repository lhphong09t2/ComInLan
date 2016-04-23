using ComInLan.Model.Base;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model
{
	public class Client : BaseModel, IClient
	{
		//--------------Created from packet------------------//
		public string Id { get; set;  }

		public string Name { get; set; }



		//--------------Created by app------------------//

		public long RefreshTime { get; private set; }

		public ServerState State { get; set; }

		public void Refresh()
		{
			RefreshTime = GetCurrentUnixTimestamp();
		}
	}
}
