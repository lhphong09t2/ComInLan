package com.onballgroup.cominlandroid;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.onballgroup.cominlan.client.ComInLanClient;
import com.onballgroup.cominlan.client.OnBroadcastClientListener;
import com.onballgroup.cominlan.model.CServer;
import com.onballgroup.cominlan.model.IServer;
import com.onballgroup.cominlan.model.OnServerListener;
import com.onballgroup.cominlan.model.ServerState;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnBroadcastClientListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ListView _serverListView;
    private ServerAdapter _arrayAdapter;

    private EditText _nameEditText;
    private EditText _dataEditText;

    private View _sendButton;
    private View _sendPasscodeButon;

    private ComInLanClient _comInLanClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _serverListView = (ListView) findViewById(R.id.serverListView);
        _nameEditText = (EditText) findViewById(R.id.nameEditText);
        _dataEditText = (EditText) findViewById(R.id.dataEditText);
        _sendButton = findViewById(R.id.sendButton);
        _sendPasscodeButon = findViewById(R.id.passcodeButton);

        _nameEditText.setText(Build.MODEL);

        _comInLanClient = new ComInLanClient(this, _nameEditText.getText().toString());
        _comInLanClient.setOnComInClientListener(this);

        _arrayAdapter = new ServerAdapter(this, 0, _comInLanClient.getServers());
        _serverListView.setAdapter(_arrayAdapter);
        _serverListView.setOnItemClickListener(this);
        _serverListView.setOnItemLongClickListener(this);
    }

    public void startButtonClick(View v) {

        if (_comInLanClient.isRunning()) {
            _comInLanClient.stop();
            ((Button) v).setText("Start");
            _nameEditText.setEnabled(true);

            _sendButton.setEnabled(false);
        } else {
            _comInLanClient.start();
            ((Button) v).setText("Listening");
            _nameEditText.setEnabled(false);
        }
    }

    public void sendPasscodeButtonClick(View v) {
        int checkedItemPosition = _serverListView.getCheckedItemPosition();

        if (checkedItemPosition < 0) {
            return;
        }

        CServer checkedServer = (CServer)_serverListView.getItemAtPosition(checkedItemPosition);

        if (checkedServer != null) {
            _comInLanClient.sendPasscode(checkedServer, _dataEditText.getText().toString());
        }
    }

    public void sendButtonClick(View v) {
        int checkedItemPosition = _serverListView.getCheckedItemPosition();

        if (checkedItemPosition < 0) {
            return;
        }

        CServer checkedServer = (CServer)_serverListView.getItemAtPosition(checkedItemPosition);

        _comInLanClient.sendData(checkedServer,_dataEditText.getText().toString());
    }

    @Override
    public void onServerNewFound(IServer server) {
    }

    @Override
    public void onServerChanged(IServer server) {
        _arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onServerRemoved(IServer server) {
        if (_comInLanClient.getServers().contains(server))
        {
            return;
        }

        _sendButton.setEnabled(false);
        _sendPasscodeButon.setEnabled(false);
    }

    @Override
    public void onServersChanged(List<IServer> servers) {
        _arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        _serverListView.setItemChecked(position, true);
        IServer server = _arrayAdapter.getItem(position);

        if (server.getState() == ServerState.Connected) {
            _sendButton.setEnabled(true);
        } else {
            _sendButton.setEnabled(false);
        }

        if (server.getState() == ServerState.PasscodeRequested) {
            _sendPasscodeButon.setEnabled(true);
        } else {
            _sendPasscodeButon.setEnabled(false);
        }

        if (server.getState() == ServerState.None) {
            server.setOnServerListener(new OnServerListener() {
                @Override
                public void onStateChanged(IServer server) {
                    _arrayAdapter.notifyDataSetChanged();

                    int checkedItemPosition = _serverListView.getCheckedItemPosition();

                    if (checkedItemPosition < 0) {
                        return;
                    }

                    Object checkedServer = _serverListView.getItemAtPosition(checkedItemPosition);

                    if (checkedServer == server) {
                        if (server.getState() == ServerState.Connected) {
                            _sendButton.setEnabled(true);
                        } else {
                            _sendButton.setEnabled(false);
                        }

                        if (server.getState() == ServerState.PasscodeRequested) {
                            _sendPasscodeButon.setEnabled(true);
                        } else {
                            _sendPasscodeButon.setEnabled(false);
                        }
                    }
                }

                @Override
                public void onDataReceived(IServer server, String data) {

                }
            });

            _comInLanClient.connect(server);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        IServer server = _arrayAdapter.getItem(position);
        _comInLanClient.disconnect(server);
        return true;
    }
}
