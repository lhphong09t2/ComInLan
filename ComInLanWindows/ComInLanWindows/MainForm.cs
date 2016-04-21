using ComInLan;
using System;
using System.Windows.Forms;

namespace ComInLanWindows
{
	public partial class MainForm : Form
	{
		private IComInLanServer _server;

		public MainForm()
		{
			InitializeComponent();

			_server = new ComInLanServer();
			_server.DataReceived += _server_DataReceived;
			listenAtPort.Text = "Listen at " + _server.ListeningPort;

			inputText.Text = Environment.MachineName;
			idText.Text = _server.Id.ToString();
		}

		private void _server_DataReceived(object sender, string dataJson)
		{
			outputText.Text = dataJson;
		}

		private void startButton_Click(object sender, EventArgs e)
		{
			if (_server.IsRunning)
			{
				startButton.Text = "Start";
				_server.Stop();
			}
			else
			{
				startButton.Text = "Stop";
				_server.Start();
			}
		}

		private void inputText_TextChanged(object sender, EventArgs e)
		{
			_server.Name = inputText.Text;
		}

		private void changIdButton_Click(object sender, EventArgs e)
		{
			_server.ChangId();
			idText.Text = _server.Id.ToString();
		}
	}
}
