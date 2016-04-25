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
		//--------------Created from packet------------------//
		public string Id { get; set;  }

		public string Name { get; set; }

		public int Port { get; set; }

		public CClient()
		{
			State = ClientState.None;
		}

		//--------------Created by app------------------//
		public IPAddress Address { get; set; }

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

		public string Checksum { get; private set; }

		public long RefreshTime { get; private set; }

		//--------------Methods------------------//
		public void Refresh()
		{
			RefreshTime = GetCurrentUnixTimestamp();
		}

		public void CalculateChecksum()
		{
			Checksum = CalculateChecksum(Id + Name);
		}

		//--------------Events------------------//
		public event ClientPasscodeEventHandler PasscodeCreated;
		public event ClientStateEventHandler StateChanged;
		public event ClientDataEventHandler DataReceived;

		public void CallDataReceived(String dataJson)
		{
			if (DataReceived != null)
			{
				DataReceived(this, dataJson);
			}
		}
	}
}
