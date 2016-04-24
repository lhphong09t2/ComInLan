package com.onballgroup.cominlan.model;

/**
 * Created by Phong Le on 4/23/2016.
 */
public interface OnServerListener {
    void onStateChanged(IServer server);
    void onDataReceived(IServer server);
}
