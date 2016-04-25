package com.onballgroup.cominlan.model;

import com.onballgroup.cominlan.model.Base.BaseModel;

import java.net.InetAddress;

/**
 * Created by Phong Le on 4/20/2016.
 */
public class CServer extends BaseModel implements IServer {
    //--------------Created from packet------------------//
    private String _id;
    private String _name;
    private int _port;

    public CServer() {
        refresh();
        _state = ServerState.None;
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
    private ServerState _state;
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
    public ServerState getState() {
        return _state;
    }

    public void setState(ServerState state) {
        _state = state;

        if (_onServerListener != null)
        {
            _onServerListener.onStateChanged(this);
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
    private OnServerListener _onServerListener;

    @Override
    public void setOnServerListener(OnServerListener listener) {
        _onServerListener = listener;
    }

    public void callIDataReceived(String data)
    {
        if (_onServerListener != null)
        {
            _onServerListener.onDataReceived(this, data);
        }
    }
}
