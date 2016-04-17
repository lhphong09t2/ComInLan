package com.onballgroup.cominlandroid;

/**
 * Created by Phong Le on 4/17/2016.
 */
public class Server implements IServer {
    private String _id;
    private String _domainId;
    private String _name;
    private int _listeingPort;

    @Override
    public String getId() {
        return _id;
    }

    @Override
    public void setId(String id) {
        _id = id;
    }

    @Override
    public String getDomainId() {
        return _domainId;
    }

    @Override
    public void setDomainId(String id) {
        _domainId = id;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }

    @Override
    public int getListeningPort() {
        return _listeingPort;
    }

    @Override
    public void setListeningPort(int port) {
        _listeingPort = port;
    }
}
