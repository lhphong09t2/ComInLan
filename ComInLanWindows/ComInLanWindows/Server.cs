using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComInLanWindows
{
    public class Server : IServer
    {
        public string DomainId
        {
            get
            {
                return "ComInLanServer";
            }
            
        }

        public Guid Id { get; set; }

        public string Name { get; set; }

        public int ListeningPort { get; set; }
    }
}
