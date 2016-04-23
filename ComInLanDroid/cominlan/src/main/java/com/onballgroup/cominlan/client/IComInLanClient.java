package com.onballgroup.cominlan.client;

import com.onballgroup.cominlan.model.IServer;

import org.json.JSONObject;

/**
 * Created by Phong Le on 4/17/2016.
 */
public interface IComInLanClient extends IBroadcastClient {
    String getName();
    void connect(IServer server);
    void disconnect(IServer server);
    void sendData(JSONObject dataJsonObject, IServer server);
}
