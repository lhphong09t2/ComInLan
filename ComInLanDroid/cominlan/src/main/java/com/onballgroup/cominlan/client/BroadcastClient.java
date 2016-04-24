package com.onballgroup.cominlan.client;

import android.app.Activity;

import com.onballgroup.cominlan.NetworkUtility;
import com.onballgroup.cominlan.model.BroadcastData;
import com.onballgroup.cominlan.model.IBroadcastData;
import com.onballgroup.cominlan.model.IServer;
import com.onballgroup.cominlan.model.CServer;
import com.onballgroup.cominlan.model.packet.IServerPacket;
import com.onballgroup.cominlan.model.packet.ServerPacket;

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
    public final int[] UdpListenerPort = {55176, 23435, 34523, 45349};
    public final int ServerCleanupPeriod = 6000;

    private Activity _activity;

    protected Activity getActivity() {
        return _activity;
    }

    private List<IServer> _servers;

    @Override
    public List<IServer> getServers() {
        return _servers;
    }

    private Timer _serverCleanupTimer;

    public BroadcastClient(Activity activity) {
        _activity = activity;
        _servers = new ArrayList<IServer>();

        initUdp();
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
        while (!startUdp(UdpListenerPort[index])) {
            index++;
            if (index >= UdpListenerPort.length) {
                break;
            }
        }

        if (index < UdpListenerPort.length) {
            _listeningPort = UdpListenerPort[index];
            _isRunning = true;

            _serverCleanupTimer = new Timer();
            _serverCleanupTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    long currentTime = System.currentTimeMillis() / 1000;
                    Iterator<IServer> it = _servers.iterator();

                    boolean hasChange = false;

                    synchronized (_servers) {
                        while (it.hasNext()) {
                            final IServer server = it.next();
                            if (currentTime - server.getRefreshTime() > ServerCleanupPeriod / 1000) {
                                it.remove();

                                _activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        _onBroadcastClientListener.onServerRemoved(server);
                                    }
                                });

                                hasChange = true;
                            }
                        }
                    }

                    if (hasChange) {
                        _activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _onBroadcastClientListener.onServersChanged(_servers);
                            }
                        });
                    }
                }
            }, ServerCleanupPeriod, ServerCleanupPeriod);
        }
    }

    public void stop() {
        stopUdp();
        _isRunning = false;

        _serverCleanupTimer.cancel();
        _serverCleanupTimer.purge();

        synchronized (_servers) {
            _servers.clear();
            _onBroadcastClientListener.onServersChanged(_servers);
        }
    }

    private OnBroadcastClientListener _onBroadcastClientListener;

    public void setOnComInClientListener(OnBroadcastClientListener listener) {
        _onBroadcastClientListener = listener;
    }

    protected void onUdpDataReceived(String dataJson, InetAddress address) {
        IServerPacket serverPacket = new ServerPacket();
        serverPacket.create(dataJson);

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

    private void handleBroadcastPacket(final IServerPacket broadcastPacket, InetAddress address) {
        final CServer server = new CServer();

        IBroadcastData data = new BroadcastData();
        data.create(broadcastPacket.getDataJson());

        server.setId(broadcastPacket.getId());
        server.setName(broadcastPacket.getName());
        server.setAddress(address);
        server.setPort(data.getListeningPort());
        server.calculateChecksum();

        CServer temp = null;
        for (IServer object : _servers) {
            if (object.getId().equals(broadcastPacket.getId())) {
                temp = (CServer) object;
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
                temp.setPort(data.getListeningPort());
                temp.calculateChecksum();
                temp.refresh();

                final CServer temp2 = temp;

                _activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _onBroadcastClientListener.onServerChanged(temp2);
                    }
                });
            } else {
                temp.refresh();
            }
        }
    }

    protected abstract void handleProtocolPacket(IServerPacket protocolPacket);

    protected abstract void handleDatapacket(IServerPacket dataPacket);
}
