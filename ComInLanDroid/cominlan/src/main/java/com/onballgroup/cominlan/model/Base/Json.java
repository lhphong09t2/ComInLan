package com.onballgroup.cominlan.model.Base;

/**
 * Created by Phong Le on 4/23/2016.
 */
public abstract class Json extends BaseModel implements IJson {
    private String _dataJson;

    @Override
    public String getDataJson()
    {
        return  _dataJson;
    }

    public void setDataJson(String dataJson) {
        _dataJson = dataJson;
    }
}
