package com.onballgroup.cominlan.model.packet;

import com.onballgroup.cominlan.model.Base.Json;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phong Le on 4/18/2016.
 */
public class ClientPacket extends Json implements IClientPacket {
    private String _id;
    private String _name;
    private ClientPacketType _type;

    @Override
    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    @Override
    public ClientPacketType getType() { return _type; }

    public void setType(ClientPacketType type) { _type = type; }

    @Override
    public void create(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            _id = jsonObject.getString("Id");
            _name = jsonObject.getString("Name");
            _type = ClientPacketType.values()[jsonObject.getInt("Type")];
            setDataJson(jsonObject.getString("DataJson"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String createJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Id", _id);
            jsonObject.put("Name", _name);
            jsonObject.put("Type",  _type);
            jsonObject.put("DataJson", getDataJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  jsonObject.toString();
    }
}
