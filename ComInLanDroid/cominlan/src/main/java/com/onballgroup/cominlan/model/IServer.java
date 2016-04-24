package com.onballgroup.cominlan.model;

import com.onballgroup.cominlan.model.Base.IBaseModel;

import java.net.InetAddress;

/**
 * Created by Phong Le on 4/20/2016.
 */
public interface IServer extends IBaseModel {
    String getId();
    String getName();
    int getPort();

    InetAddress getAddress();
    ServerState getState();
    String getChecksum();
    long getRefreshTime();

    void setOnServerListener(OnServerListener listener);
}
