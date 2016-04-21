package com.onballgroup.cominlan.model.protocol;

/**
 * Created by Phong Le on 4/20/2016.
 */
public class ClientProtocol implements IClientProtocol {
    ClientCommand _command;
    Object _data;

    @Override
    public ClientCommand getCommand() {
        return _command;
    }

    public void setCommand(ClientCommand command) {
        _command = command;
    }

    @Override
    public Object getData() {
        return _data;
    }

    public void setData(Object data) {
        _data = data;
    }
}
