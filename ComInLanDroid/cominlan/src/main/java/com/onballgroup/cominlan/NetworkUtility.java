package com.onballgroup.cominlan;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by lep on 4/19/2016.
 */
public abstract class NetworkUtility {

    private DatagramSocket _udpSocket;
    private Thread _udpListenerThread;
    private DatagramSocket _udpListenerSocket;

    protected void initUdp() {
        try {
            _udpSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    protected void sendUdp(byte[] data, InetAddress address, int port) {
        DatagramPacket packet = new DatagramPacket(
                data, data.length, address, port);
        try {
            _udpSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void startUdp(int udpListeningPort) {
        try {
            _udpListenerSocket = new DatagramSocket(udpListeningPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        _udpListenerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final byte[] receiveData = new byte[1024];

                while (!Thread.currentThread().isInterrupted()) {
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

        _udpListenerThread.start();
    }

    protected void stopUdp() {
        _udpListenerThread.interrupt();;
        _udpListenerSocket.close();
    }

    protected void initTcp() {
        //TODO write code to handle this one
    }

    protected void sendTcp(byte[] data) {
        //TODO write code to handle this one
    }

    protected void startTcp() {
        //TODO write code to handle this one
    }

    protected void stopTcp() {
        //TODO write code to handle this one
    }

    protected abstract void onUdpDataReceived(byte[] data);

    protected abstract void onTcpDataReceived(byte[] data);
}
