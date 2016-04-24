package com.onballgroup.cominlan.model;

import com.onballgroup.cominlan.model.Base.BaseModel;

import java.net.InetAddress;

/**
 * Created by Phong Le on 4/20/2016.
 */
public class CClient extends BaseModel implements IClient {
    private String _id;
    private String _name;
    private int _port;

    public CClient(){
        _state = ClientState.None;
    }

    @Override
    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    @Override
    public int getPort() {
        return _port;
    }

    public void setPort(int port) {
        _port = port;
    }

    //--------------Created by app------------------//
    private InetAddress _address;
    private String _passcode;
    private ClientState _state;
    private String _checksum;
    private long _refreshTime;

    @Override
    public InetAddress getAddress() {
        return _address;
    }

    public void setAddress(InetAddress address) {
        _address = address;
    }

    @Override
    public String getPasscode() {
        return _passcode;
    }

    public void setPasscode(String passcode) {
        _passcode = passcode;

        if (_onClientListener != null)
        {
            _onClientListener.onPasscodeCreated(this);
        }
    }

    @Override
    public ClientState getState() {
        return _state;
    }

    public void setState(ClientState state)
    {
        _state = state;

        if (_onClientListener != null)
        {
            _onClientListener.onStateChanged(this);
        }
    }

    @Override
    public String getChecksum() {
        return _checksum;
    }

    @Override
    public long getRefreshTime() {
        return _refreshTime;
    }


    //--------------Methods------------------//
    public void calculateChecksum() {
        _checksum = calculateChecksum(_id + _name);
    }

    public void refresh() {
        _refreshTime = getCurrentUnixTimestamp();
    }

    //--------------Events------------------//
    private  OnClientListener _onClientListener;

    @Override
    public void setOnClientListener(OnClientListener listener) {
        _onClientListener = listener;
    }

    public void callDataReceived(String dataJson)
    {
        if (_onClientListener != null)
        {
            _onClientListener.onDataReceived(this);
        }
    }
}
