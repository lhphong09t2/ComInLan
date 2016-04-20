package com.onballgroup.cominlan;

import android.app.Activity;

import com.onballgroup.cominlan.model.BroadcastData;
import com.onballgroup.cominlan.model.IBroadcastData;
import com.onballgroup.cominlan.model.IServer;
import com.onballgroup.cominlan.model.IServerPacket;
import com.onballgroup.cominlan.model.Server;
import com.onballgroup.cominlan.model.ServerPacket;
import com.onballgroup.cominlan.model.ServerPacketType;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Phong Le on 4/17/2016.
 */
public class ComInLanClient extends NetworkUtility implements IComInLanClient {
    public final int UdpListenerPort = 55176;

    private Activity _activity;

    private List<IServer> _servers;

    private Timer _serverCleanupTimer;

    public ComInLanClient(Activity activity) {
        _activity = activity;
        _servers = new ArrayList<IServer>();

        initUdp();

        _serverCleanupTimer = new Timer();
    }

    private boolean _isRunning = false;
    public boolean isRunning() {
        return _isRunning;
    }

    private OnComInLanListener _onComInClientListener;

    @Override
    public void start() {
        startUdp(UdpListenerPort);
        _isRunning = true;

        _serverCleanupTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                Iterator<IServer> it = _servers.iterator();
                while (it.hasNext())
                {
                    IServer server = it.next();
                    if (currentTime - server.getRefreshTime() > 10000)
                    {
                        it.remove();
                    }

                    _activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _onComInClientListener.onServersChanged(_servers);
                        }
                    });
                }
            }
        }, 10000, 10000);
    }

    @Override
    public void stop() {
        stopUdp();
        _isRunning = false;

        _serverCleanupTimer.cancel();
    }

    @Override
    public void setOnComInClientListener(OnComInLanListener listener) {
        _onComInClientListener = listener;
    }

    @Override
    protected void onUdpDataReceived(String dataJson, InetAddress address) {
        final IServerPacket serverPacket = parseJsonToServer(dataJson);

        if (serverPacket.getDomainId().equals("ComInLanServer")) {
            switch (serverPacket.getType()) {
                case Broadcast:
                    handleBroadcastPackets(serverPacket, address);
                    break;
                case Data:
                    break;
            }
        }
    }

    @Override
    protected void onTcpDataReceived(String dataJson, InetAddress address) {

    }

    private void handleBroadcastPackets(final IServerPacket<IBroadcastData> broadcastPacket, InetAddress address) {
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

        if (temp == null) {
            _servers.add(server);
            _activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _onComInClientListener.onServerNewFound(server);
                    _onComInClientListener.onServersChanged(_servers);
                }
            });
        }
        else if(!temp.getChecksum().equals(server.getChecksum()))
        {
            _servers.remove(temp);
            _servers.add(server);

            _activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _onComInClientListener.onServerChanged(server);
                    _onComInClientListener.onServersChanged(_servers);
                }
            });
        } else
        {
            temp.refreshTime();
        }
    }

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

