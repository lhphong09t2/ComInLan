using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan
{
    interface IServerPacket<T>
    {
        string Id { get; }
        string DomainId { get; }
        string Name { get; }
        T Data { get; }       
    }
}
