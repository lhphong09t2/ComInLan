
namespace ComInLan
{
	public class ComInLanClient : NetworkUtility, IComInLanClient
	{
		protected override void OnTcpDataReceived(byte[] data)
		{
		}

		protected override void OnUdpDataReceived(byte[] data)
		{
		}
	}
}
