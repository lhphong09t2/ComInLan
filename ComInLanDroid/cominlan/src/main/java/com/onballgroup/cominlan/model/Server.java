package com.onballgroup.cominlan.model;

import java.net.InetAddress;

/**
 * Created by Phong Le on 4/20/2016.
 */
public class Server extends BaseModel implements IServer {
    private String _id;
    private String _name;
    private InetAddress _address;
    private int _port;
    private String _checksum;

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
    public InetAddress getAddress() {
        return _address;
    }

    public void setAddress(InetAddress address) {
        _address = address;
    }

    @Override
    public int getPort() {
        return _port;
    }

    public void setPort(int port) {
        _port = port;
    }

    @Override
    public String getChecksum() {
        return _checksum;
    }

    public void calculateChecksum()
    {
        _checksum = calculateChecksum(_id + _name + _address + _port);
    }
}
