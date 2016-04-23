package com.onballgroup.cominlan.model.protocol;

import com.onballgroup.cominlan.model.Base.Json;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phong Le on 4/20/2016.
 */
public class ClientProtocol  extends Json implements IClientProtocol {
    ClientCommand _command;

    @Override
    public ClientCommand getCommand() {
        return _command;
    }

    public void setCommand(ClientCommand command) {
        _command = command;
    }

    @Override
    public void create(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            _command = ClientCommand.values()[jsonObject.getInt("Command")];
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
            jsonObject.put("Command", _command);
            jsonObject.put("DataJson", getDataJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  jsonObject.toString();
    }
}
