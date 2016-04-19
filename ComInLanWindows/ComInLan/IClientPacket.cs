using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLan
{
    public interface IClientPacket<T>
    {
        string Id { get; }
        string Name { get; }
        T Data { get; }
    }
}
