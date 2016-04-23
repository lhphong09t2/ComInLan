package com.onballgroup.cominlan.model.packet;

import com.onballgroup.cominlan.model.Base.IJson;

/**
 * Created by Phong Le on 4/18/2016.
 */
public interface IClientPacket extends IJson {
    String getId();
    String getName();
    ClientPacketType getType();
}
