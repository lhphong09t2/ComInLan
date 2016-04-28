package com.onballgroup.cominlan.client;

import com.onballgroup.cominlan.model.IServer;

/**
 * Created by Phong Le on 4/17/2016.
 */
public interface IComInLanClient extends IBroadcastClient {
    void connect(IServer server);
    void sendPasscode(IServer server, String passcode);
    void disconnect(IServer server);
    void sendData(IServer server, String dataJson);
}
