package com.onballgroup.cominlan.model.packet;

import com.onballgroup.cominlan.model.Base.IJson;

/**
 * Created by Phong Le on 4/17/2016.
 */
public interface IServerPacket extends IJson {
    String getId();
    String getDomainId();
    String getName();
    ServerPacketType getType();
}
