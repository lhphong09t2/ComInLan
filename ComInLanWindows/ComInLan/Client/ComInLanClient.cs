using System.Net;

namespace ComInLan.Client
{
	public class ComInLanClient : BroadcastClient, IComInLanClient
	{
		protected override void OnUdpDataReceived(string dataJson, IPAddress address)
		{
		}
	}
}
