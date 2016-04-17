package com.onballgroup.cominlandroid;

/**
 * Created by Phong Le on 4/17/2016.
 */
public interface IServer {
    String getId();
    void setId(String id);
    String getDomainId();
    void setDomainId(String id);
    String getName();
    void setName(String name);
    int getListeningPort();
    void setListeningPort(int port);
}
