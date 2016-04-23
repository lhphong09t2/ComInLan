using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model.Base
{
	public interface IJson : IBaseModel
	{
		string DataJson { get; }
	}
}
