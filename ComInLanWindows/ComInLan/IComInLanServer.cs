using System;

namespace ComInLan
{
    public delegate void ComInLanServerHandler (object sender, string dataJson);

    public interface IComInLanServer
    {
        Guid Id { get; }
        string Name { get; set; }
        int ListeningPort { get; }
        bool IsRunning { get; }

        event ComInLanServerHandler DataReceived;

        void Start();
        void Stop();

        void ChangId();
    }
}
