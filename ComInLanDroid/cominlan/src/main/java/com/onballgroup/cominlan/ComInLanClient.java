package com.onballgroup.cominlan;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phong Le on 4/17/2016.
 */
public class ComInLanClient extends NetworkUtility implements IComInLanClient {
    public final int UdpListenerPort = 55176;

    private Activity _activity;

    private List<IServerPacket<IBroadcastData>> _broadcastPackets;

    public ComInLanClient(Activity activity)
    {
        _activity = activity;
        _broadcastPackets = new ArrayList<IServerPacket<IBroadcastData>>();

        initUdp(UdpListenerPort);
    }

    private boolean _isRunning = false;
    public  boolean isRunning()
    {
        return  _isRunning;
    }

    private OnComInLanListener _onComInClientListener;

    @Override
    public void start() {
        startUdp();
        _isRunning = true;
    }

    @Override
    public void stop()
    {
        stopUdp();
        _isRunning = false;
    }

    @Override
    public void setOnComInClientListener(OnComInLanListener listener) {
        _onComInClientListener = listener;
    }

    private IServerPacket parseJsonToServer(String json)
    {
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


    @Override
    protected void onUdpDataReceived(byte[] data) {
        String packetJson = new String(data, Charset.forName("US-ASCII"));
        final IServerPacket serverPacket = parseJsonToServer(packetJson);

        if (serverPacket.getDomainId().equals("ComInLanServer"))
        {
            switch (serverPacket.getType())
            {
                case Broadcast:
                    handleBroadcastPacketsComing(serverPacket);
                    break;
                case Data:
                    break;
            }
        }
    }

    @Override
    protected void onTcpDataReceived(byte[] data) {

    }

    private void handleBroadcastPacketsComing(final IServerPacket<IBroadcastData> broadcastPackets)
    {
        IServerPacket<IBroadcastData> interServer = null;

        for (IServerPacket<IBroadcastData> object : _broadcastPackets) {
            if (object.getId().equals(broadcastPackets.getId())) {
                interServer = object;
            }
        }

        if (interServer == null)
        {
            _broadcastPackets.add(broadcastPackets);

            _activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _onComInClientListener.onServerNewFound(broadcastPackets);
                    _onComInClientListener.onServersChanged(_broadcastPackets);
                }
            });
        }
        else
        {
            _broadcastPackets.remove(interServer);
            _broadcastPackets.add(broadcastPackets);

            _activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _onComInClientListener.onServerChanged(broadcastPackets);
                    _onComInClientListener.onServersChanged(_broadcastPackets);
                }
            });
        }
    }
}

