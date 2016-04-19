package com.onballgroup.cominlan;

import java.net.InetAddress;

/**
 * Created by Phong Le on 4/18/2016.
 */
public class ComInLanServer extends NetworkUtility implements IComInLanServer {
    @Override
    protected void onUdpDataReceived(String dataJson, InetAddress address) {

    }

    @Override
    protected void onTcpDataReceived(String dataJson, InetAddress address) {

    }
}
