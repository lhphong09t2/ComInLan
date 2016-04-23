package com.onballgroup.cominlan.model;

/**
 * Created by Phong Le on 4/23/2016.
 */
public interface OnServerStateListener {
    void onStateChanged(IServer server, ServerState state);
}
