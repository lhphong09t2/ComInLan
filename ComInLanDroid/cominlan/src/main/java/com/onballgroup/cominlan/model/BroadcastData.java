package com.onballgroup.cominlan.model;

import com.onballgroup.cominlan.model.Base.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phong Le on 4/19/2016.
 */
public class BroadcastData extends BaseModel implements IBroadcastData {
    private  int _listeningPort;

    public BroadcastData(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public int getListeningPort() {
        return _listeningPort;
    }

    public  void setListeningPort(int port)
    {
        _listeningPort = port;
    }

    @Override
    public void create(JSONObject jsonObject) {
        try {
            _listeningPort = jsonObject.getInt("ListeningPort");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject createJsonObject() {
        return null;
    }
}
