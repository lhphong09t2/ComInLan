package com.onballgroup.cominlan.client;

import android.app.Activity;

import com.onballgroup.cominlan.model.CServer;
import com.onballgroup.cominlan.model.IServer;
import com.onballgroup.cominlan.model.ServerState;
import com.onballgroup.cominlan.model.packet.ClientPacket;
import com.onballgroup.cominlan.model.packet.ClientPacketType;
import com.onballgroup.cominlan.model.packet.IServerPacket;
import com.onballgroup.cominlan.model.protocol.ClientCommand;
import com.onballgroup.cominlan.model.protocol.ClientProtocol;
import com.onballgroup.cominlan.model.protocol.ServerCommand;
import com.onballgroup.cominlan.model.protocol.ServerProtocol;

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
        CServer server = null;
        for (IServer item : getServers()) {
            if (item.getId().equals(protocolPacket.getId()) &&
                    item.getState() == ServerState.Waiting)
            {
                server = (CServer)item;
                break;
            }
        }

        if (server != null)
        {
            ServerProtocol protocol = new ServerProtocol();
            protocol.create(protocolPacket.getDataJson());

            switch (protocol.getCommand())
            {
                case RequestPasscode:
                    handleRequestPasscodeCommand(server);
                    break;
                case Accept:
                case Refuse:
                    handleResultOfConnecting(server, protocol.getCommand());
                    break;
            }
        }
    }

    @Override
    protected void handleDatapacket(IServerPacket dataPacket) {
        CServer server = null;
        for (IServer item : getServers()) {
            if (item.getId().equals(dataPacket.getId()))
            {
                server = (CServer)item;
                break;
            }
        }

        if (server != null)
        {
            server.callIDataReceived(dataPacket.getDataJson());
        }
    }

    @Override
    public void connect(IServer server)
    {
       if (server.getState() != ServerState.None)
       {
           return;
       }

        ((CServer) server).setState(ServerState.Waiting);

        ClientProtocol protocol = new ClientProtocol();
        protocol.setCommand(ClientCommand.RequestConnect);
        protocol.setDataJson(String.valueOf(getListeningPort()));
        sendClientPacket(ClientPacketType.Protocol, protocol.createJson(), server);
    }

    @Override
    public void sendPasscode(IServer server, String passcode) {
        if (server.getState() == ServerState.PasscodeRequested)
        {
            ClientProtocol protocol = new ClientProtocol();
            protocol.setCommand(ClientCommand.RequestConnect);
            protocol.setDataJson(passcode);
            sendClientPacket(ClientPacketType.Protocol, protocol.createJson(), server);

            ((CServer)server).setState(ServerState.PasscodeSent);
        }
    }

    @Override
    public void disconnect(IServer server) {

    }

    @Override
    public void sendData(String dataJson, IServer server) {
        sendClientPacket(ClientPacketType.Data, dataJson , server);
    }

    private void sendClientPacket(ClientPacketType type, String dataJson, IServer server)
    {
        ClientPacket clientPacket = new ClientPacket();
        clientPacket.setId(UUID.randomUUID().toString());
        clientPacket.setName(_name);
        clientPacket.setType(type);
        clientPacket.setDataJson(dataJson);

        sendUdp(clientPacket.createJson(), server.getAddress(), server.getPort());
    }

    private void handleRequestPasscodeCommand(final CServer server)
    {
        if (server.getState() != ServerState.Waiting)
        {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                server.setState(ServerState.PasscodeRequested);
            }
        });
    }

    private void handleResultOfConnecting(final CServer server, final ServerCommand command)
    {
        if (server.getState() != ServerState.PasscodeSent)
        {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                server.setState(command == ServerCommand.Accept ?
                ServerState.Connected : ServerState.None);
            }
        });
    }
}

