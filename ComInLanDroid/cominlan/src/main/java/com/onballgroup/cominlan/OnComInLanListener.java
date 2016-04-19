package com.onballgroup.cominlan;

import com.onballgroup.cominlan.model.IServer;

import java.util.List;

/**
 * Created by Phong Le on 4/17/2016.
 */
public interface OnComInLanListener {
    void onServerNewFound(IServer server);
    void onServerChanged(IServer server);
    void onServersChanged(List<IServer> servers);
}
