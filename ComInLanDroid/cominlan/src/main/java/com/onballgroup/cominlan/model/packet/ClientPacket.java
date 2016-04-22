package com.onballgroup.cominlan.model.packet;

import com.onballgroup.cominlan.model.Base.BaseModel;
import com.onballgroup.cominlan.model.Base.IBaseModel;

import org.json.JSONObject;

/**
 * Created by Phong Le on 4/18/2016.
 */
public class ClientPacket<T>  extends BaseModel implements IClientPacket<T> {
    private String _id;
    private String _name;
    private ClientPacketType _type;
    private T _data;

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
    public String getJson(IBaseModel model) {
        return null;
    }
}
