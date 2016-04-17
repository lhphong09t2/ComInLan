package com.onballgroup.cominlandroid;

/**
 * Created by Phong Le on 4/17/2016.
 */
public interface IComInLanClient {
    void start();
    void stop();
    void setOnComInClientListener(OnComInLanListener listener);
}
