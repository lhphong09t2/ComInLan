package com.onballgroup.cominlan.server;

import java.net.InetAddress;

/**
 * Created by Phong Le on 4/18/2016.
 */
public class ComInLanServer extends BroadcastServer implements IComInLanServer {
    @Override
    protected void onUdpDataReceived(String dataJson, InetAddress address) {

    }
}
