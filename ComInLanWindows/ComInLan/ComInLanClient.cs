
namespace ComInLan
{
	public class ComInLanClient : NetworkUtility, IComInLanClient
	{
		protected override void OnTcpDataReceived(string dataJson)
		{
		}

		protected override void OnUdpDataReceived(string dataJson)
		{
		}
	}
}
