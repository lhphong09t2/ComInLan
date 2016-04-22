package com.onballgroup.cominlan.model.protocol;

import com.onballgroup.cominlan.model.Base.IBaseModel;

/**
 * Created by Phong Le on 4/20/2016.
 */
public interface IClientProtocol extends IBaseModel {
    ClientCommand getCommand();
    Object getData();
}
