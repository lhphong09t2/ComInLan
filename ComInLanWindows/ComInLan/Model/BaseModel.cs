using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan.Model
{
	public abstract class BaseModel
	{
		public static string CalculateChecksum(string content)
		{
			if (string.IsNullOrEmpty(content))
			{
				return "";
			}

			string hash;

			using (System.Security.Cryptography.MD5 md5 = System.Security.Cryptography.MD5.Create())
			{
				hash = BitConverter.ToString(
				  md5.ComputeHash(Encoding.UTF8.GetBytes(content))
				).Replace("-", String.Empty);
			}

			return hash;
		}
	}
}
