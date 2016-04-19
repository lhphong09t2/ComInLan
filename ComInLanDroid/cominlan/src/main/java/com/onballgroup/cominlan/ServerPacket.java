package com.onballgroup.cominlan;

/**
 * Created by Phong Le on 4/17/2016.
 */
class ServerPacket<T> implements IServerPacket<T> {
    private String _id;
    private String _domainId;
    private String _name;
    private ServerPacketType _type;
    private T _data;

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
    public T getData() {
        return _data;
    }

    public void setData(T data) {
        _data = data;
    }
}
