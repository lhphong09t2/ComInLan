package com.onballgroup.cominlan.client;

import com.onballgroup.cominlan.model.IServer;

import java.util.List;

/**
 * Created by Phong Le on 4/21/2016.
 */
public interface IBroadcastClient {
    String getId();
    String getDomainId();
    String getName();
    int getListeningPort();
    List<IServer> getServers();
    boolean start();
    void stop();
    void setOnComInClientListener(OnBroadcastClientListener listener);
}
