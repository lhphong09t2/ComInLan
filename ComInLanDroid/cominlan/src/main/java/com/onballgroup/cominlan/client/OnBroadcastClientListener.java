package com.onballgroup.cominlan.client;

import com.onballgroup.cominlan.model.IServer;

import java.util.List;

/**
 * Created by Phong Le on 4/17/2016.
 */
public interface OnBroadcastClientListener {
    void onServerNewFound(IServer server);
    void onServerChanged(IServer server);
    void onServerRemoved(IServer server);
    void onServersChanged(List<IServer> servers);
}
