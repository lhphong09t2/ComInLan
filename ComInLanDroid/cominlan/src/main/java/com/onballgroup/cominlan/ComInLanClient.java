package com.onballgroup.cominlan;

import android.app.Activity;

import com.onballgroup.cominlan.model.IServer;
import com.onballgroup.cominlan.model.protocol.IServerProtocol;

/**
 * Created by Phong Le on 4/17/2016.
 */
public class ComInLanClient extends BroadcastClient implements IComInLanClient {

    public ComInLanClient(Activity activity) {
        super(activity);
    }

    @Override
    protected void OnProtocolReceived(IServer server, IServerProtocol serverProtocol) {

    }

    @Override
    protected void OnDataReceived(IServer server, Object data) {

    }

    @Override
    public String getDomainId() {
        return "ComInLan";
    }
}

