package com.onballgroup.cominlan;

import android.app.Activity;

import com.onballgroup.cominlan.model.IServer;
import com.onballgroup.cominlan.model.packet.IServerPacket;
import com.onballgroup.cominlan.model.protocol.IServerProtocol;

/**
 * Created by Phong Le on 4/17/2016.
 */
public class ComInLanClient extends BroadcastClient implements IComInLanClient {

    @Override
    public String getDomainId() {
        return "ComInLan";
    }

    public ComInLanClient(Activity activity) {
        super(activity);
    }

    @Override
    protected void handleProtocolPacket(IServerPacket<IServerProtocol> protocolPacket) {

    }

    @Override
    protected void handleDatapacket(IServerPacket dataPacket) {

    }

    @Override
    public boolean connect(IServer server) {
        return false;
    }

    @Override
    public void disconnect(IServer server) {

    }

    @Override
    public void sendData(String jsonData, IServer server) {

    }
}

