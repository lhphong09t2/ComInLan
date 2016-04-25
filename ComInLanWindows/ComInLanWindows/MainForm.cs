using ComInLan.Server;
using System;
using System.Runtime.InteropServices;
using System.Windows.Forms;
using WindowsInput;
using WindowsInput.Native;

namespace ComInLanWindows
{
	public partial class MainForm : Form
	{
		private IComInLanServer _server;
		private InputSimulator _inputsimulator;

		public MainForm()
		{
			InitializeComponent();

			_server = new ComInLanServer(this);
			_server.ClientNew += _server_ClientNew;
			_server.ClientChanged += _server_ClientChanged;
			_server.ClientRemoved += _server_ClientRemoved;
			_server.ClientsChanged += _server_ClientsChanged;

			listenAtPort.Text = "Listen at " + _server.ListeningPort;
			inputText.Text = Environment.MachineName;
			idText.Text = _server.Id.ToString();

			_inputsimulator = new InputSimulator();
		}

		private void _server_ClientsChanged(System.Collections.Generic.List<ComInLan.Model.IClient> clients)
		{
			WriteLine("Clients count: " + clients.Count);
		}

		private void _server_ClientRemoved(ComInLan.Model.IClient client)
		{
			WriteLine("Remove: " + client.Id + " " + client.Name + " " + client.Port + " " + client.Address);
		}

		private void _server_ClientChanged(ComInLan.Model.IClient client)
		{
			WriteLine("Change: " + client.Id + " " + client.Name + " " + client.Port + " " + client.Address);
		}

		private void _server_ClientNew(ComInLan.Model.IClient client)
		{
			WriteLine("New: " + client.Id + " " + client.Name + " " + client.Port + " " + client.Address);
			WriteLine("State: " + client.State.ToString());

			client.PasscodeCreated += Client_PasscodeCreated;
			client.StateChanged += Client_StateChanged;
		}

		void client_DataReceived(ComInLan.Model.IClient client, string dataJson)
		{
			WriteLine("From " + client.Name + ": " + dataJson);

			switch (dataJson)
			{
				case "b":
					_inputsimulator.Keyboard.KeyPress(VirtualKeyCode.BACK);
					break;
				case "n":
					_inputsimulator.Keyboard.KeyPress(VirtualKeyCode.NEXT);
					break;
				case "f": 
					_inputsimulator.Keyboard.KeyPress(VirtualKeyCode.F5);
					break;
			}
		}

		private void Client_StateChanged(ComInLan.Model.IClient client)
		{
			WriteLine("State changed: " + client.State.ToString());

			if (client.State == ComInLan.Model.ClientState.Accepted)
			{
				client.DataReceived += client_DataReceived;
			}

			if (client.State == ComInLan.Model.ClientState.None)
			{

				client.DataReceived -= client_DataReceived;
			}
		}

		private void Client_PasscodeCreated(ComInLan.Model.IClient client)
		{
			WriteLine("Passcode: " + client.Passcode);
		}

		private void startButton_Click(object sender, EventArgs e)
		{
			if (_server.IsRunning)
			{
				startButton.Text = "Start";
				_server.Stop();
			}
			else if (_server.Start())
			{
				startButton.Text = "Stop";
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

		private void WriteLine(string content)
		{
			outputText.Text = content + System.Environment.NewLine + outputText.Text;
		}
	}
}
