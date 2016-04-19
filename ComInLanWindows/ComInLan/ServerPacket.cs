
namespace ComInLan
{
	internal class ServerPacket<T> : IServerPacket<T>
	{
		public string DomainId { get; set; }

		public string Id { get; set; }

		public string Name { get; set; }

		public ServerPacketType Type { get; set; }

		public T Data { get; set; }
	}
}
