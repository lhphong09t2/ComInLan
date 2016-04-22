package com.onballgroup.cominlan;

import android.app.Activity;

import com.onballgroup.cominlan.model.BroadcastData;
import com.onballgroup.cominlan.model.IBroadcastData;
import com.onballgroup.cominlan.model.IServer;
import com.onballgroup.cominlan.model.Server;
import com.onballgroup.cominlan.model.packet.IServerPacket;
import com.onballgroup.cominlan.model.packet.ServerPacket;
import com.onballgroup.cominlan.model.packet.ServerPacketType;
import com.onballgroup.cominlan.model.protocol.IServerProtocol;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Phong Le on 4/21/2016.
 */
public abstract class BroadcastClient extends NetworkUtility implements IBroadcastClient {
    public final int[] UdpListenerPort = { 55176, 23435, 34523, 45349 };
    public final int ServerCleanupPeriod = 6000;

    private Activity _activity;
    private List<IServer> _servers;
    private Timer _serverCleanupTimer;

    public BroadcastClient(Activity activity) {
        _activity = activity;
        _servers = new ArrayList<IServer>();

        initUdp();

        _serverCleanupTimer = new Timer();
    }

    private boolean _isRunning = false;
    public boolean isRunning() {
        return _isRunning;
    }

    private int _listeningPort;

    @Override
    public int getListeningPort() {
        return _listeningPort;
    }

    public void start() {
        int index = 0;
        while (!startUdp(UdpListenerPort[index]))
        {
            index++;
            if(index >= 4)
            {
                break;
            }
        }

        if (index < 4)
        {
            _listeningPort = UdpListenerPort[index];
            _isRunning = true;

            _serverCleanupTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    long currentTime = System.currentTimeMillis();
                    Iterator<IServer> it = _servers.iterator();

                    synchronized (_servers) {
                        while (it.hasNext()) {
                            IServer server = it.next();
                            if (currentTime - server.getRefreshTime() > ServerCleanupPeriod) {
                                it.remove();
                            }
                        }
                    }

                    _activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _onBroadcastClientListener.onServersChanged(_servers);
                        }
                    });
                }
            }, ServerCleanupPeriod, ServerCleanupPeriod);
        }
    }

    public void stop() {
        stopUdp();
        _isRunning = false;

        _serverCleanupTimer.cancel();
    }

    private OnBroadcastClientListener _onBroadcastClientListener;
    public void setOnComInClientListener(OnBroadcastClientListener listener) {
        _onBroadcastClientListener = listener;
    }

    protected void onUdpDataReceived(String dataJson, InetAddress address) {
        final IServerPacket serverPacket = parseJsonToServer(dataJson);

        if (serverPacket.getDomainId().equals(getDomainId())) {
            switch (serverPacket.getType()) {
                case Broadcast:
                    handleBroadcastPacket(serverPacket, address);
                    break;
                case Protocol:
                    handleProtocolPacket(serverPacket);
                    break;
                case Data:
                    handleDatapacket(serverPacket);
                    break;
            }
        }
    }

    private void handleBroadcastPacket(final IServerPacket<IBroadcastData> broadcastPacket, InetAddress address) {
        final Server server = new Server();
        server.setId(broadcastPacket.getId());
        server.setName(broadcastPacket.getName());
        server.setAddress(address);
        server.setPort(broadcastPacket.getData().getListeningPort());
        server.calculateChecksum();

        Server temp = null;
        for (IServer object : _servers) {
            if (object.getId().equals(broadcastPacket.getId())) {
                temp = (Server) object;
            }
        }

        synchronized (_servers) {
            if (temp == null) {
                _servers.add(server);
                _activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _onBroadcastClientListener.onServerNewFound(server);
                        _onBroadcastClientListener.onServersChanged(_servers);
                    }
                });
            } else if (!temp.getChecksum().equals(server.getChecksum())) {
                temp.setId(broadcastPacket.getId());
                temp.setName(broadcastPacket.getName());
                temp.setAddress(address);
                temp.setPort(broadcastPacket.getData().getListeningPort());
                temp.calculateChecksum();
                temp.refreshTime();

                final Server temp2 = temp;

                _activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _onBroadcastClientListener.onServerChanged(temp2);
                        _onBroadcastClientListener.onServersChanged(_servers);
                    }
                });
            } else {
                temp.refreshTime();
            }
        }
    }

    protected abstract void handleProtocolPacket(IServerPacket<IServerProtocol> protocolPacket);
    protected abstract void handleDatapacket(IServerPacket dataPacket);

    private IServerPacket parseJsonToServer(String json) {
        ServerPacket serverPacket = new ServerPacket();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONObject dataJsonObject = new JSONObject(jsonObject.getString("Data"));
            BroadcastData broadcastData = new BroadcastData();
            broadcastData.setListeningPort(dataJsonObject.getInt("ListeningPort"));

            serverPacket.setId(jsonObject.getString("Id"));
            serverPacket.setDomainId(jsonObject.getString("DomainId"));
            serverPacket.setName(jsonObject.getString("Name"));
            serverPacket.setType(ServerPacketType.values()[jsonObject.getInt("Type")]);
            serverPacket.setData(broadcastData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return serverPacket;
    }
}
