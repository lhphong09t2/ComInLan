package com.onballgroup.cominlandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onballgroup.cominlan.ComInLanClient;
import com.onballgroup.cominlan.IBroadcastData;
import com.onballgroup.cominlan.IServerPacket;
import com.onballgroup.cominlan.OnComInLanListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnComInLanListener {

    private TextView _resultView;
    private ComInLanClient _comInLanClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _resultView = (TextView) findViewById(R.id.result);

        _comInLanClient = new ComInLanClient(this);
        _comInLanClient.setOnComInClientListener(this);
    }

    public void startButtonClick(View v) {

        if (_comInLanClient.isRunning()) {
            _comInLanClient.stop();
            ((Button) v).setText("Start");

        } else {
            _comInLanClient.start();
            ((Button) v).setText("Listening");
        }
    }

    private void showText(final String text) {
    }


    @Override
    public void onServerNewFound(IServerPacket<IBroadcastData> server) {

    }

    @Override
    public void onServerChanged(IServerPacket<IBroadcastData> server) {

    }

    @Override
    public void onServersChanged(List<IServerPacket<IBroadcastData>> servers) {
        String output = "";

        for (IServerPacket<IBroadcastData> server : servers) {
            output += server.getDomainId() + " "
                    + server.getId() + " "
                    + server.getName() + " "
                    + server.getData().getListeningPort() + "\n";
        }

        _resultView.setText(output);
    }
}
