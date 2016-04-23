using ComInLan.Model.Base;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model
{
	public class CClient : BaseModel, IClient
	{
		public event ClientPasscodeEventHandler PasscodeCreated;
		public event ClientStateEventHandler StateChanged;
		public event ClientDataEventHandler DataReceived;

		//--------------Created from packet------------------//
		public string Id { get; set;  }

		public string Name { get; set; }

		public int Port { get; set; }

		//--------------Created by app------------------//
		public IPAddress Address { get; set; }

		public string Checksum { get; private set; }

		private string _passcode;
		public string Passcode
		{
			get
			{
				return _passcode;
			}
			set
			{
				_passcode = value;

				if (PasscodeCreated != null)
				{
					PasscodeCreated(this);
				}
			}
		}

		public long RefreshTime { get; private set; }

		private ClientState _state;
		public ClientState State
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

		public void Refresh()
		{
			RefreshTime = GetCurrentUnixTimestamp();
		}

		public void CallDataReceived(String dataJson)
		{
			if (DataReceived != null)
			{
				DataReceived(this, dataJson);
			}
		}

		public void CalculateChecksum()
		{
			Checksum = CalculateChecksum(Id + Name);
		}
	}
}
