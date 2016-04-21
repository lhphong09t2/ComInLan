package com.onballgroup.cominlan.model.protocol;

/**
 * Created by Phong Le on 4/20/2016.
 */
public interface IServerProtocol {
    ServerCommand getCommand();
    Object getData();
}
