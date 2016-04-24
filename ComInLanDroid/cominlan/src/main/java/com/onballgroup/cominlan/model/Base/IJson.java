package com.onballgroup.cominlan.model.Base;

/**
 * Created by Phong Le on 4/23/2016.
 */
public interface IJson extends IBaseModel {
    String getDataJson();
    void create(String json);
    String createJson();
}
