package com.onballgroup.cominlan.model;

import com.onballgroup.cominlan.model.Base.BaseModel;

import org.json.JSONObject;

/**
 * Created by Phong Le on 4/20/2016.
 */
public class Client extends BaseModel implements IClient {

    public Client() {
        super();
    }

    public Client(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void create(JSONObject jsonObject) {
    }

    @Override
    public JSONObject createJsonObject() {
        return null;
    }
}
