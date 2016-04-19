
namespace ComInLan.Model
{
	public interface IClientPacket<T>
	{
		string Id { get; }
		string Name { get; }
		T Data { get; }
	}
}
