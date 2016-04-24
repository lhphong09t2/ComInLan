package com.onballgroup.cominlan.model;

import com.onballgroup.cominlan.model.Base.IBaseModel;

import java.net.InetAddress;

/**
 * Created by Phong Le on 4/20/2016.
 */
public interface IClient extends IBaseModel {
    String getId();
    String getName();
    int getPort();

    InetAddress getAddress();
    String getPasscode();
    ClientState getState();
    String getChecksum();
    long getRefreshTime();

    void setOnClientListener(OnClientListener listener);
}
