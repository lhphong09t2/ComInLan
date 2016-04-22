package com.onballgroup.cominlan.model.protocol;

import com.onballgroup.cominlan.model.Base.IBaseModel;

/**
 * Created by Phong Le on 4/20/2016.
 */
public interface IServerProtocol extends IBaseModel {
    ServerCommand getCommand();
    Object getData();
}
