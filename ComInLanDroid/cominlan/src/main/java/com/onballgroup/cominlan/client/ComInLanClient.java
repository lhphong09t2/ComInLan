package com.onballgroup.cominlan.client;

import android.app.Activity;

import com.onballgroup.cominlan.model.IServer;
import com.onballgroup.cominlan.model.Server;
import com.onballgroup.cominlan.model.ServerState;
import com.onballgroup.cominlan.model.packet.ClientPacket;
import com.onballgroup.cominlan.model.packet.ClientPacketType;
import com.onballgroup.cominlan.model.packet.IServerPacket;
import com.onballgroup.cominlan.model.protocol.ClientCommand;
import com.onballgroup.cominlan.model.protocol.ClientProtocol;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Phong Le on 4/17/2016.
 */
public class ComInLanClient extends BroadcastClient implements IComInLanClient {

    @Override
    public String getDomainId() {
        return "ComInLan";
    }

    private String _name;
    @Override
    public String getName() {
        return _name;
    }

    public ComInLanClient(Activity activity, String name) {
        super(activity);
        _name = name;
    }

    @Override
    protected void handleProtocolPacket(IServerPacket protocolPacket) {

    }

    @Override
    protected void handleDatapacket(IServerPacket dataPacket) {

    }

    @Override
    public void connect(IServer server)
    {
       if (server.getState() != ServerState.None)
       {
           return;
       }

        ((Server) server).setState(ServerState.Waiting);

        ClientProtocol protocol = new ClientProtocol();
        protocol.setCommand(ClientCommand.RequestConnect);
        sendClientPacket(ClientPacketType.Protocol, protocol.getDataJsonObject(), server);
    }

    @Override
    public void disconnect(IServer server) {

    }

    @Override
    public void sendData(JSONObject dataJsonObject, IServer server) {
        sendClientPacket(ClientPacketType.Data, dataJsonObject, server);
    }

    private void sendClientPacket(ClientPacketType type, JSONObject dataJsonObject, IServer server)
    {
        ClientPacket clientPacket = new ClientPacket();
        clientPacket.setId(UUID.randomUUID().toString());
        clientPacket.setName(_name);
        clientPacket.setType(ClientPacketType.Protocol);

        JSONObject jsonObject = clientPacket.createJsonObject();

        try {
            jsonObject.put("Data", dataJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendUdp(jsonObject.toString(), server.getAddress(), server.getPort());
    }
}

