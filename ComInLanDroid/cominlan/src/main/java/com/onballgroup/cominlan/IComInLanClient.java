package com.onballgroup.cominlan;

import com.onballgroup.cominlan.model.IServer;

/**
 * Created by Phong Le on 4/17/2016.
 */
public interface IComInLanClient extends IBroadcastClient {
    boolean connect(IServer server);
    void disconnect(IServer server);
    void sendData(String jsonData, IServer server);
}
