
namespace ComInLan.Model
{
	public interface IServerPacket<T>
	{
		string Id { get; }
		string DomainId { get; }
		string Name { get; }
		ServerPacketType Type { get; }
		T Data { get; }
	}
}
