package com.onballgroup.cominlandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    private TextView _resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _resultView = (TextView)findViewById(R.id.result);
    }

    public void startButtonClick(View v) {
        try {
            final DatagramSocket serverSocket = new DatagramSocket(11000);
            final byte[] receiveData = new byte[1024];

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true)
                    {
                        try {
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            serverSocket.receive(receivePacket);
                            String sentence = new String(receivePacket.getData(), Charset.forName("US-ASCII"));
                            showText(sentence);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void showText(final String text)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _resultView.setText(_resultView.getText() +
                        System.getProperty("line.separator") + text);
            }
        });
    }
}
