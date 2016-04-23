using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model.Base
{
	public abstract class Json : BaseModel, IJson
	{
		public string DataJson { get; set; }
	}
}
