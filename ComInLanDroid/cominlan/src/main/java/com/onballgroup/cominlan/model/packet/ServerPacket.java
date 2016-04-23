package com.onballgroup.cominlan.model.packet;

import com.onballgroup.cominlan.model.Base.Json;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phong Le on 4/17/2016.
 */
public class ServerPacket<T>  extends Json implements IServerPacket {
    private String _id;
    private String _domainId;
    private String _name;
    private ServerPacketType _type;

    @Override
    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    @Override
    public String getDomainId() {
        return _domainId;
    }

    public void setDomainId(String id) {
        _domainId = id;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public ServerPacketType getType() { return _type; }

    public void setType(ServerPacketType type) { _type = type; }

    public void setName(String name) {
        _name = name;
    }

    @Override
    public void create(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            _id = jsonObject.getString("Id");
            _domainId = jsonObject.getString("DomainId");
            _name = jsonObject.getString("Name");
            _type = ServerPacketType.values()[jsonObject.getInt("Type")];
            setDataJson(jsonObject.getString("DataJson"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String createJson() {
        return null;
    }
}
