
namespace ComInLan.Model
{
	public class ClientPacket<T> : BaseModel, IClientPacket<T>
	{
		public string Id { get; set; }
		public string Name { get; set; }
		public T Data { get; set; }
	}
}
