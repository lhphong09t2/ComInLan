using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ComInLanWindows
{
    public partial class MainForm : Form
    {
        public MainForm()
        {
            InitializeComponent();
        }

        private void startButton_Click(object sender, EventArgs e)
        {
            Socket s = new Socket(AddressFamily.InterNetwork, SocketType.Dgram,
            ProtocolType.Udp);

            IPAddress broadcast = IPAddress.Parse("192.168.1.255");

            byte[] sendbuf = Encoding.ASCII.GetBytes("Hello guys");
            IPEndPoint ep = new IPEndPoint(broadcast, 11000);

            s.SendTo(sendbuf, ep);
        }
    }
}
