using ComInLan.Model;
using System;
using System.Collections.Generic;

namespace ComInLan.Server
{
	public interface IBroadcastServer
	{
		string Id { get; }
		string DomainId { get; }
		string Name { get; set; }
		int ListeningPort { get; }
		bool IsRunning { get; }
		List<IClient> Clients { get; }

		bool Start();
		void Stop();
		void ChangId();
	}
}
