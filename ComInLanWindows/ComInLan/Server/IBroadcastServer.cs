using System;

namespace ComInLan.Server
{
	public interface IBroadcastServer
	{
		string Id { get; }
		string DomainId { get; }
		string Name { get; set; }
		int ListeningPort { get; }
		bool IsRunning { get; }

		bool Start();
		void Stop();

		void ChangId();
	}
}
