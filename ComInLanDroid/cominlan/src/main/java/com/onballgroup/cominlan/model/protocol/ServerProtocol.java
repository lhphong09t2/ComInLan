package com.onballgroup.cominlan.model.protocol;

import com.onballgroup.cominlan.model.Base.Json;

/**
 * Created by Phong Le on 4/20/2016.
 */
public class ServerProtocol  extends Json implements IServerProtocol {
    ServerCommand _command;
    Object _data;

    @Override
    public ServerCommand getCommand() {
        return _command;
    }

    public void setCommand(ServerCommand command) {
        _command = command;
    }

    @Override
    public Object getData() {
        return _data;
    }

    public void setData(Object data) {
        _data = data;
    }

    @Override
    public void create(String json) {
    }

    @Override
    public String createJson() {
        return null;
    }
}
