using System;

namespace ComInLan
{
	public delegate void BroadcastServerHandler(object sender, string dataJson);

	public interface IBroadcastServer
	{
		string Id { get; }
		string DomainId { get; }
		string Name { get; set; }
		int ListeningPort { get; }
		bool IsRunning { get; }

		event BroadcastServerHandler DataReceived;

		void Start();
		void Stop();

		void ChangId();
	}
}
