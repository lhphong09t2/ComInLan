package com.onballgroup.cominlan.model;

import java.net.InetAddress;

/**
 * Created by Phong Le on 4/20/2016.
 */
public interface IServer {
    String getId();
    String getName();
    InetAddress getAddress();
    int getPort();
    String getChecksum();
    long getRefreshTime();
}
