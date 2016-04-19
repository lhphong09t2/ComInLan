package com.onballgroup.cominlan;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by lep on 4/19/2016.
 */
public abstract class NetworkUtility {

    private Thread _udpListenerThread;
    private DatagramSocket _udpListenerSocket;

    protected void initUdp(int port)
    {
        try {
            _udpListenerSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        _udpListenerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final byte[] receiveData = new byte[1024];

                while (!Thread.currentThread().isInterrupted())
                {
                    try {
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        _udpListenerSocket.receive(receivePacket);
                        onUdpDataReceived(receivePacket.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected void sendUdp(byte[] data)
    {
        //TODO write code to handle this one
    }

    protected void startUdp()
    {
        _udpListenerThread.start();
    }

    protected void stopUdp()
    {
        _udpListenerThread.interrupt();
        _udpListenerSocket.close();
    }

    protected void initTcp()
    {
        //TODO write code to handle this one
    }

    protected void sendTcp(byte[] data)
    {
        //TODO write code to handle this one
    }

    protected void startTcp()
    {
        //TODO write code to handle this one
    }

    protected void stopTcp()
    {
        //TODO write code to handle this one
    }

    protected abstract void onUdpDataReceived(byte[] data);
    protected abstract void onTcpDataReceived(byte[] data);
}
