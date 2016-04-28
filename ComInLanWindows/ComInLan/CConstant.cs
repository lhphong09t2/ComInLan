using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan
{
	public class CConstant
	{
		public const string DomainId = "ComInLan";

		public static readonly int[] UdpListenerPort = { 55176, 23435, 34523, 45349 };
		public const int UdpPacketSizeInByte = 4096;

		public const int AdvertisingPeriod = 5000;
		public const int ClientRefreshPeriod = 5000;

		public static readonly int ServerCleanupPeriod = AdvertisingPeriod* 2 + 1000;
		public static readonly int ClientCleanupPeriod = ClientRefreshPeriod * 10;
	}
}
