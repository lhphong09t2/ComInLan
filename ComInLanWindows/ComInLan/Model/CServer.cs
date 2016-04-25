using ComInLan.Model.Base;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;

namespace ComInLan.Model
{
	public class CServer : BaseModel, IServer
	{
		//--------------Created from packet------------------//
		public string Id { get; set; }

		public string Name { get; set; }

		public int Port { get; set; }

		public CServer()
		{
			Refresh();
			State = ServerState.None;
		}

		//--------------Created by app------------------//
		public IPAddress Address { get; set; }

		private ServerState _state;
		public ServerState State
		{
			get { return _state; }
			set
			{
				_state = value;

				if (StateChanged != null)
				{
					StateChanged(this);
				}
			}
		}

		public string Checksum { get; private set; }

		public long RefreshTime { get; private set; }


		//--------------Methods------------------//
		public void CalculateChecksum()
		{
			Checksum = CalculateChecksum(Id + Name);
		}

		public void Refresh()
		{
			RefreshTime = GetCurrentUnixTimestamp();
		}

		//--------------Events------------------//
		public event ServerStateEventHandler StateChanged;
		public event SeverDataEventHandler DataReceived;

		public void CallIDataReceived(String data)
		{
			if (DataReceived != null)
			{
				DataReceived(this, data);
			}
		}
	}
}
