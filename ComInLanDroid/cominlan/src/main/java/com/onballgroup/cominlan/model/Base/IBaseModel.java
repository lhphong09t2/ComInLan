package com.onballgroup.cominlan.model.Base;

import org.json.JSONObject;

/**
 * Created by lep on 4/22/2016.
 */
public interface IBaseModel {
    void create(JSONObject jsonObject);
    String getJson(IBaseModel model);
    JSONObject getDataJsonObject();
}
