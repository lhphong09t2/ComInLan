package com.onballgroup.cominlan;

/**
 * Created by Phong Le on 4/21/2016.
 */
public interface IBroadcastClient {
    String getDomainId();
    void start();
    void stop();
    void setOnComInClientListener(OnComInLanListener listener);
}
