package com.onballgroup.cominlan.model.protocol;

/**
 * Created by Phong Le on 4/20/2016.
 */
public interface IClientProtocol {
    ClientCommand getCommand();
    Object getData();
}
