package com.onballgroup.cominlan;

/**
 * Created by Phong Le on 4/21/2016.
 */
public interface IBroadcastClient {
    String getDomainId();
    int getListeningPort();
    void start();
    void stop();
    void setOnComInClientListener(OnBroadcastClientListener listener);
}
