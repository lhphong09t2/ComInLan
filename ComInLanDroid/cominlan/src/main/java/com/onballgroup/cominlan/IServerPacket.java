package com.onballgroup.cominlan;

/**
 * Created by Phong Le on 4/17/2016.
 */
public interface IServerPacket<T> {
    String getId();
    String getDomainId();
    String getName();
    ServerPacketType getType();
    T getData();
}
