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

    @Override
    public String getId() {
        return _id;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public int getPort() {
        return _port;
    }

    //--------------Created by app------------------//
    private InetAddress _address;
    private String _passcode;
    private long _refreshTime;
    private ClientState _state;

    @Override
    public InetAddress getAddress() {
        return _address;
    }

    @Override
    public String getPasscode() {
        return _passcode;
    }

    @Override
    public long getRefreshTime() {
        return _refreshTime;
    }

    @Override
    public ClientState getState() {
        return _state;
    }

    public void refresh() {
        _refreshTime = getCurrentUnixTimestamp();
    }
}
