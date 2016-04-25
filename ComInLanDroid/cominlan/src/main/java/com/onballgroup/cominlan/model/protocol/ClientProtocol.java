package com.onballgroup.cominlan.model.protocol;

import com.onballgroup.cominlan.model.Base.Json;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phong Le on 4/20/2016.
 */
public class ClientProtocol  extends Json implements IClientProtocol {
    ClientMessage _message;

    @Override
    public ClientMessage getMessage() {
        return _message;
    }

    public void setMessage(ClientMessage message) {
        _message = message;
    }

    @Override
    public void create(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            _message = ClientMessage.values()[jsonObject.getInt("Message")];
            setDataJson(jsonObject.getString("DataJson"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String createJson()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Message", _message);
            jsonObject.put("DataJson", getDataJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  jsonObject.toString();
    }
}
