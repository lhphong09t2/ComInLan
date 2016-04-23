package com.onballgroup.cominlan.model;

import com.onballgroup.cominlan.model.Base.Json;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phong Le on 4/19/2016.
 */
public class BroadcastData extends Json implements IBroadcastData {
    private  int _listeningPort;

    @Override
    public int getListeningPort() {
        return _listeningPort;
    }

    public  void setListeningPort(int port)
    {
        _listeningPort = port;
    }

    @Override
    public void create(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            _listeningPort = jsonObject.getInt("ListeningPort");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String createJson() {
        return null;
    }
}
