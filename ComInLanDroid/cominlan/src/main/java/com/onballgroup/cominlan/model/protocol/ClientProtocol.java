package com.onballgroup.cominlan.model.protocol;

import com.onballgroup.cominlan.model.Base.BaseModel;
import com.onballgroup.cominlan.model.Base.IBaseModel;

import org.json.JSONObject;

/**
 * Created by Phong Le on 4/20/2016.
 */
public class ClientProtocol  extends BaseModel implements IClientProtocol {
    ClientCommand _command;
    Object _data;

    public ClientProtocol(JSONObject jsonObject) {
        super(jsonObject);
    }

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

    @Override
    public void create(JSONObject jsonObject) {
    }

    @Override
    public String getJson(IBaseModel model) {
        return null;
    }
}
