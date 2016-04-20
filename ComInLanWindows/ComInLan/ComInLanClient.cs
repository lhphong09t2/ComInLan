using System.Net;

namespace ComInLan
{
	public class ComInLanClient : NetworkUtility, IComInLanClient
	{
		protected override void OnTcpDataReceived(string dataJson, IPAddress address)
		{
		}

		protected override void OnUdpDataReceived(string dataJson, IPAddress address)
		{
		}
	}
}
