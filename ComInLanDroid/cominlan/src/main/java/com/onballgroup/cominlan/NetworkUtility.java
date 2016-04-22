package com.onballgroup.cominlan;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.Charset;

/**
 * Created by lep on 4/19/2016.
 */
public abstract class NetworkUtility {
    protected final int UdpPacketSizeInByte = 4096;

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

    protected void sendUdp(String dataJson, InetAddress address, int port) {
        byte[] data = dataJson.getBytes(Charset.forName("UTF-8"));
        DatagramPacket packet = new DatagramPacket(
                data, data.length, address, port);
        try {
            _udpSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected boolean startUdp(int udpListeningPort) {
        try {
            _udpListenerSocket = new DatagramSocket(udpListeningPort);
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }

        _udpListenerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final byte[] receiveData = new byte[UdpPacketSizeInByte];

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        _udpListenerSocket.receive(receivePacket);
                        final String dataJson = new String(receivePacket.getData(), 0, receivePacket.getLength(), Charset.forName("UTF-8"));

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                onUdpDataReceived(dataJson, receivePacket.getAddress());
                            }
                        }).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        _udpListenerThread.start();
        return  true;
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

    protected abstract void onUdpDataReceived(String dataJson, InetAddress address);
    // protected abstract void onTcpDataReceived(String dataJson, InetAddress address);
}
