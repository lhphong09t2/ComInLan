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
import com.onballgroup.cominlan.model.IServer;
import com.onballgroup.cominlan.model.ServerState;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnBroadcastClientListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        AdapterView.OnItemSelectedListener {
    private ListView _serverListView;
    private ServerAdapter _arrayAdapter;

    private EditText _nameEditText;
    private EditText _dataEditText;

    private View _sendButton;

    private ComInLanClient _comInLanClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _serverListView = (ListView) findViewById(R.id.serverListView);
        _nameEditText = (EditText) findViewById(R.id.nameEditText);
        _dataEditText = (EditText) findViewById(R.id.dataEditText);
        _sendButton = findViewById(R.id.sendButton);

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
            _dataEditText.setEnabled(false);
        } else {
            _comInLanClient.start();
            ((Button) v).setText("Listening");
            _nameEditText.setEnabled(false);
        }
    }

    public void sendButtonClick(View v) {
        _serverListView.getSelectedItem();
    }

    private void showText(final String text) {

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
    }

    @Override
    public void onServersChanged(List<IServer> servers) {
        _arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IServer server = _arrayAdapter.getItem(position);
        _comInLanClient.connect(server);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        IServer server = _arrayAdapter.getItem(position);
        _comInLanClient.disconnect(server);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        IServer server = _arrayAdapter.getItem(position);

        if (server.getState() == ServerState.Connected) {
            _sendButton.setEnabled(true);
            _dataEditText.setEnabled(true);
        } else {
            _sendButton.setEnabled(false);
            _dataEditText.setEnabled(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        _sendButton.setEnabled(false);
        _dataEditText.setEnabled(false);
    }
}
