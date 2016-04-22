package com.onballgroup.cominlan.model;

import com.onballgroup.cominlan.model.Base.BaseModel;
import com.onballgroup.cominlan.model.Base.IBaseModel;

import org.json.JSONObject;

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
    private long _refreshTime;
    private boolean _isConnected;

    public Server() {
        refresh();
    }

    public Server(JSONObject jsonObject) {
        super(jsonObject);
        refresh();
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

    @Override
    public long getRefreshTime() {
        return _refreshTime;
    }

    @Override
    public boolean isConnected() {
        return _isConnected;
    }

    public void setConnected(boolean value) {
        _isConnected = value;
    }

    public void calculateChecksum() {
        _checksum = calculateChecksum(_id + _name + _address + _port);
    }

    public void refresh() {
        _refreshTime = System.currentTimeMillis() / 1000;
    }

    @Override
    public void create(JSONObject jsonObject) {

    }

    @Override
    public String getJson(IBaseModel model) {
        return null;
    }
}
