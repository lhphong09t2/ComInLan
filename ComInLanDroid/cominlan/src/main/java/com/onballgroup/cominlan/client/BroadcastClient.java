package com.onballgroup.cominlan.client;

import android.app.Activity;

import com.onballgroup.cominlan.CConstant;
import com.onballgroup.cominlan.NetworkUtility;
import com.onballgroup.cominlan.model.Base.BaseModel;
import com.onballgroup.cominlan.model.BroadcastData;
import com.onballgroup.cominlan.model.CServer;
import com.onballgroup.cominlan.model.IBroadcastData;
import com.onballgroup.cominlan.model.IServer;
import com.onballgroup.cominlan.model.packet.IServerPacket;
import com.onballgroup.cominlan.model.packet.ServerPacket;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by Phong Le on 4/21/2016.
 */
public abstract class BroadcastClient extends NetworkUtility implements IBroadcastClient {
    private String _id;

    public String getId() {
        return _id;
    }

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
        _id = UUID.randomUUID().toString();
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

    public boolean start() {
        int index = 0;
        while (!startUdp(CConstant.UdpListenerPort[index])) {
            index++;
            if (index >= CConstant.UdpListenerPort.length) {
                break;
            }
        }

        if (index < CConstant.UdpListenerPort.length) {
            _listeningPort = CConstant.UdpListenerPort[index];
            _isRunning = true;

            _serverCleanupTimer = new Timer();
            _serverCleanupTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    cleanUpServers();
                }
            }, CConstant.ServerCleanupPeriod, CConstant.ServerCleanupPeriod);
        }
        else
        {
            return false;
        }

        return true;
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
                    handleDataPacket(serverPacket);
                    break;
            }
        }
    }

    protected void runOnUiThread(Runnable runnable)
    {
        if (_activity == null)
        {
            runnable.run();
        }
        else
        {
            _activity.runOnUiThread(runnable);
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

        CServer temp = getServerById(broadcastPacket.getId());

        synchronized (_servers) {
            if (temp == null) {
                _servers.add(server);
                runOnUiThread(new Runnable() {
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

                runOnUiThread(new Runnable() {
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

    private void handleDataPacket(IServerPacket dataPacket) {
        CServer server = getServerById(dataPacket.getId());

        if (server != null) {
            server.callIDataReceived(dataPacket.getDataJson());
        }
    }

    private void cleanUpServers() {
        long currentUnixTimestamp = BaseModel.getCurrentUnixTimestamp();

        boolean hasChange = false;
        Iterator<IServer> it = _servers.iterator();

        synchronized (_servers) {
            while (it.hasNext()) {
                final IServer server = it.next();
                if (currentUnixTimestamp - server.getRefreshTime() > CConstant.ServerCleanupPeriod / 1000) {
                    it.remove();

                    runOnUiThread(new Runnable() {
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _onBroadcastClientListener.onServersChanged(_servers);
                }
            });
        }
    }

    protected CServer getServerById(String id) {
        CServer server = null;
        for (IServer item : getServers()) {
            if (item.getId().equals(id)) {
                server = (CServer) item;
                break;
            }
        }

        return server;
    }

    protected abstract void handleProtocolPacket(IServerPacket protocolPacket);
}
