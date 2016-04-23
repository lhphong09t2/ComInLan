
using ComInLan.Model.Base;

namespace ComInLan.Model
{
	public interface IBroadcastData : IJson
	{
		int ListeningPort { get; }
	}
}
