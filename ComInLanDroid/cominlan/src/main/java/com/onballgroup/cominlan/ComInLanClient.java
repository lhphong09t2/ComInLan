package com.onballgroup.cominlan;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phong Le on 4/17/2016.
 */
public class ComInLanClient implements Runnable, IComInLanClient {
    public final int ListenerPort = 55176;

    private Activity _activity;
    private Thread _listenerThread;
    private DatagramSocket _listenerSocket;

    List<IServer> _servers;

    public ComInLanClient(Activity activity)
    {
        _activity = activity;
        _servers = new ArrayList<IServer>();

        try {
            _listenerSocket = new DatagramSocket(ListenerPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        _listenerThread = new Thread(this);
    }

    private boolean _isRunning = false;
    public  boolean isRunning()
    {
        return  _isRunning;
    }

    private OnComInLanListener _onComInClientListener;

    @Override
    public void start() {
        _listenerThread.start();
        _isRunning = true;
    }

    @Override
    public void stop() {
        _listenerThread.interrupt();
        _listenerSocket.close();
        _isRunning = false;
    }

    @Override
    public void setOnComInClientListener(OnComInLanListener listener) {
        _onComInClientListener = listener;
    }

    @Override
    public void run() {
        final byte[] receiveData = new byte[1024];

        while (!Thread.currentThread().isInterrupted())
        {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                _listenerSocket.receive(receivePacket);
                String packetJson = new String(receivePacket.getData(), Charset.forName("US-ASCII"));

                final IServer server = parseJsonToServer(packetJson);

                if (server.getDomainId().equals("ComInLanServer"))
                {
                    HandleServerComing(server);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private IServer parseJsonToServer(String json)
    {
        Server server = new Server();
        try {
            JSONObject jsonObject = new JSONObject(json);

            server.setId(jsonObject.getString("Id"));
            server.setDomainId(jsonObject.getString("DomainId"));
            server.setName(jsonObject.getString("Name"));
            server.setListeningPort(jsonObject.getInt("ListeningPort"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  server;
    }

    private void HandleServerComing(final IServer server)
    {
        IServer interServer = null;

        for (IServer object : _servers) {
            if (object.getId().equals(server.getId())) {
                interServer = object;
            }
        }

        if (interServer == null)
        {
            _servers.add(server);

            _activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _onComInClientListener.onServerNewFound(server);
                    _onComInClientListener.onServersChanged(_servers);
                }
            });
        }
        else
        {
            _servers.remove(interServer);
            _servers.add(server);

            _activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _onComInClientListener.onServerChanged(server);
                    _onComInClientListener.onServersChanged(_servers);
                }
            });
        }
    }
}

