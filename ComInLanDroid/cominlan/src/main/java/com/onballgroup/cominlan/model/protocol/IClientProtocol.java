package com.onballgroup.cominlan.model.protocol;

import com.onballgroup.cominlan.model.Base.IJson;

/**
 * Created by Phong Le on 4/20/2016.
 */
public interface IClientProtocol extends IJson {
    ClientMessage getMessage();
}
