package com.onballgroup.cominlan.model.packet;

/**
 * Created by Phong Le on 4/18/2016.
 */
public interface IClientPacket<T> {
    String getId();
    String getName();
    ClientPacketType getType();
    T getData();
}
