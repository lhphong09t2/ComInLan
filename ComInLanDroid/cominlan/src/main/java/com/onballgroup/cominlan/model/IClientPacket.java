package com.onballgroup.cominlan.model;

/**
 * Created by Phong Le on 4/18/2016.
 */
public interface IClientPacket<T> {
    String getId();
    String getName();
    T getData();
}
