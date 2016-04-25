package com.onballgroup.cominlan.client;

import android.app.Activity;

import com.onballgroup.cominlan.CConstant;
import com.onballgroup.cominlan.model.CServer;
import com.onballgroup.cominlan.model.IServer;
import com.onballgroup.cominlan.model.ServerState;
import com.onballgroup.cominlan.model.packet.ClientPacket;
import com.onballgroup.cominlan.model.packet.ClientPacketType;
import com.onballgroup.cominlan.model.packet.IServerPacket;
import com.onballgroup.cominlan.model.protocol.ClientMessage;
import com.onballgroup.cominlan.model.protocol.ClientProtocol;
import com.onballgroup.cominlan.model.protocol.ServerMessage;
import com.onballgroup.cominlan.model.protocol.ServerProtocol;

import java.util.Timer;
import java.util.TimerTask;

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

    private Timer _clientRefreshTimer;

    public ComInLanClient(Activity activity, String name) {
        super(activity);
        _name = name;
    }

    @Override
    public boolean start() {
        if (super.start()) {

            _clientRefreshTimer = new Timer();
            _clientRefreshTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    refreshClients();
                }
            }, CConstant.ServerCleanupPeriod, CConstant.ServerCleanupPeriod);
        } else {
            return false;
        }

        return true;
    }

    @Override
    public void stop() {
        super.stop();
        _clientRefreshTimer.cancel();
        _clientRefreshTimer.purge();
    }

    @Override
    public void connect(IServer server) {
        if (server.getState() != ServerState.None) {
            return;
        }

        ClientProtocol protocol = new ClientProtocol();
        protocol.setMessage(ClientMessage.RequestConnect);
        protocol.setDataJson(String.valueOf(getListeningPort()));
        sendClientPacket(ClientPacketType.Protocol, protocol.createJson(), server);

        ((CServer) server).setState(ServerState.Waiting);
    }

    @Override
    public void sendPasscode(IServer server, String passcode) {
        if (server.getState() != ServerState.PasscodeRequested) {
            return;
        }

        ClientProtocol protocol = new ClientProtocol();
        protocol.setMessage(ClientMessage.Passcode);
        protocol.setDataJson(passcode);
        sendClientPacket(ClientPacketType.Protocol, protocol.createJson(), server);

        ((CServer) server).setState(ServerState.PasscodeSent);
    }

    @Override
    public void disconnect(IServer server) {
        if (server.getState() == ServerState.None) {
            return;
        }

        ClientProtocol protocol = new ClientProtocol();
        protocol.setMessage(ClientMessage.Disconnect);
        sendClientPacket(ClientPacketType.Protocol, protocol.createJson(), server);

        ((CServer) server).setState(ServerState.None);
    }

    @Override
    public void sendData(IServer server, String dataJson) {
        sendClientPacket(ClientPacketType.Data, dataJson, server);
    }

    @Override
    protected void handleProtocolPacket(IServerPacket protocolPacket) {
        CServer server = getServerById(protocolPacket.getId());

        if (server == null || server.getState() == ServerState.None) {
            return;
        }

        ServerProtocol protocol = new ServerProtocol();
        protocol.create(protocolPacket.getDataJson());

        switch (protocol.getMessage()) {
            case RequestPasscode:
                handleRequestPasscodeMessage(server);
                break;
            case Accept:
            case Refuse:
                handleResultOfConnecting(server, protocol.getMessage());
                break;
        }
    }

    private void handleRequestPasscodeMessage(final CServer server) {
        if (server.getState() != ServerState.Waiting) {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                server.setState(ServerState.PasscodeRequested);
            }
        });
    }

    private void handleResultOfConnecting(final CServer server, final ServerMessage message) {
        if (server.getState() != ServerState.PasscodeSent) {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                server.setState(message == ServerMessage.Accept ?
                        ServerState.Connected : ServerState.None);
            }
        });
    }

    private void refreshClients() {
        synchronized (getServers()) {
            for (IServer server : getServers()) {
                if (server.getState() != ServerState.None)
                {
                    sendClientPacket(ClientPacketType.Refresh, null, server);
                }
            }
        }
    }

    private void sendClientPacket(ClientPacketType type, String dataJson, IServer server) {
        ClientPacket clientPacket = new ClientPacket();
        clientPacket.setId(getId());
        clientPacket.setName(_name);
        clientPacket.setType(type);
        clientPacket.setDataJson(dataJson);

        sendUdp(clientPacket.createJson(), server.getAddress(), server.getPort());
    }
}

