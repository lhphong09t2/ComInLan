package com.onballgroup.cominlan.model.packet;

import com.onballgroup.cominlan.model.Base.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Phong Le on 4/18/2016.
 */
public class ClientPacket<T>  extends BaseModel implements IClientPacket<T> {
    private String _id;
    private String _name;
    private ClientPacketType _type;
    private T _data;

    public ClientPacket() {
        super();
    }

    public ClientPacket(JSONObject jsonObject) {
        super(jsonObject);
    }

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
    public T getData() {
        return _data;
    }

    public void setData(T data) {
        _data = data;
    }

    @Override
    public void create(JSONObject jsonObject) {

    }

    @Override
    public JSONObject createJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Id", _id);
            jsonObject.put("Name", _name);
            jsonObject.put("Type", _id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  jsonObject;
    }
}
