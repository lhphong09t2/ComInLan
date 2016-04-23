package com.onballgroup.cominlan.model.protocol;

import com.onballgroup.cominlan.model.Base.Json;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phong Le on 4/20/2016.
 */
public class ClientProtocol  extends Json implements IClientProtocol {
    ClientCommand _command;
    Object _data;

    @Override
    public ClientCommand getCommand() {
        return _command;
    }

    public void setCommand(ClientCommand command) {
        _command = command;
    }

    @Override
    public Object getData() {
        return _data;
    }

    public void setData(Object data) {
        _data = data;
    }

    @Override
    public void create(String json) {
    }

    @Override
    public String createJson()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Command", _command);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  jsonObject.toString();
    }
}
