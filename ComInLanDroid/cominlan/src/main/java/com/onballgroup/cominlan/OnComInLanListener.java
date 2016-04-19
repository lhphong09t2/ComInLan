package com.onballgroup.cominlan;

import com.onballgroup.cominlan.model.IBroadcastData;
import com.onballgroup.cominlan.model.IServerPacket;

import java.util.List;

/**
 * Created by Phong Le on 4/17/2016.
 */
public interface OnComInLanListener {
    void onServerNewFound(IServerPacket<IBroadcastData> server);
    void onServerChanged(IServerPacket<IBroadcastData> server);
    void onServersChanged(List<IServerPacket<IBroadcastData>> servers);
}
