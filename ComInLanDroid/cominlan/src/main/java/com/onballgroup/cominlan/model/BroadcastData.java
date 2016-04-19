package com.onballgroup.cominlan.model;

/**
 * Created by Phong Le on 4/19/2016.
 */
public class BroadcastData implements IBroadcastData {
    private  int _listeningPort;

    @Override
    public int getListeningPort() {
        return _listeningPort;
    }

    public  void setListeningPort(int port)
    {
        _listeningPort = port;
    }
}
