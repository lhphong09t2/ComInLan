﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model
{
	public enum ClientState
	{
		None,
		WaitingPasscode,
		Accepted,
		Refused,
	}
}
