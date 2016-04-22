package com.onballgroup.cominlan.model.packet;

import com.onballgroup.cominlan.model.Base.IBaseModel;

/**
 * Created by Phong Le on 4/17/2016.
 */
public interface IServerPacket<T> extends IBaseModel {
    String getId();
    String getDomainId();
    String getName();
    ServerPacketType getType();
    T getData();
}
