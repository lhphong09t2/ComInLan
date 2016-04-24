package com.onballgroup.cominlan.model;

/**
 * Created by Phong Le on 4/24/2016.
 */
public interface OnClientListener {
    void onPasscodeCreated(IClient client);
    void onStateChanged(IClient client);
    void onDataReceived(IClient client);
}
