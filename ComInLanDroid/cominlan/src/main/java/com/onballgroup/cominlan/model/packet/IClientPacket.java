package com.onballgroup.cominlan.model.packet;

import com.onballgroup.cominlan.model.Base.IBaseModel;

/**
 * Created by Phong Le on 4/18/2016.
 */
public interface IClientPacket<T> extends IBaseModel {
    String getId();
    String getName();
    ClientPacketType getType();
    T getData();
}
