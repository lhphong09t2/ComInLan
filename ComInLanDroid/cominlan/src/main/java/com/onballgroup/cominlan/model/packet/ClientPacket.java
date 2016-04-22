package com.onballgroup.cominlan.model.packet;

/**
 * Created by Phong Le on 4/18/2016.
 */
public class ClientPacket<T>  implements IClientPacket<T> {
    private String _id;
    private String _name;
    private ClientPacketType _type;
    private T _data;

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
}
