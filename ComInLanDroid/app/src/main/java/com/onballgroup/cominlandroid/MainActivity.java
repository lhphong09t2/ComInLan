package com.onballgroup.cominlandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    private boolean _isListening = false;
    private Thread _serverThread;
    private DatagramSocket _serverSocket;
    public void startButtonClick(View v) {

        if (_isListening)
        {
            _serverThread.interrupt();
            _serverSocket.close();
            ((Button)v).setText("Start");
            _resultView.setText("");
            _isListening = false;
        }
        else
        {
            try {
                _serverSocket = new DatagramSocket(11000);
                final byte[] receiveData = new byte[1024];

                _serverThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted())
                        {
                            try {
                                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                                _serverSocket.receive(receivePacket);
                                String sentence = new String(receivePacket.getData(), Charset.forName("US-ASCII"));
                                showText(sentence);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                _serverThread.start();

                ((Button)v).setText("Listening");
                _isListening = true;
            } catch (SocketException e) {
                e.printStackTrace();
            }
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
