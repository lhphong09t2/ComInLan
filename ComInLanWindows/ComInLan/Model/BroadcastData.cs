﻿using ComInLan.Model.Base;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model
{
	public class BroadcastData : Json, IBroadcastData
	{
		public int ListeningPort { get; set; }
	}
}
