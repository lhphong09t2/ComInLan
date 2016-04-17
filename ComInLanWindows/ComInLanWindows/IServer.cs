using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLanWindows
{
    interface IServer
    {
        Guid Id { get; set; }
        string DomainId { get; }
        string Name { get; set; }   
        int ListeningPort { get; set; }
    }
}
