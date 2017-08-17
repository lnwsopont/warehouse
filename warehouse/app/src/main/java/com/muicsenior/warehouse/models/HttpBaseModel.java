package com.muicsenior.warehouse.models;

import com.tamemo.simplehttp.Session;
import com.tamemo.simplehttp.SimpleHttp;

/**
 * Created by Ta on 2017-08-10.
 */

public class HttpBaseModel extends BaseModel {
    protected Session connector;

    public HttpBaseModel(){
        connector = SimpleHttp.session(1);
    }

    protected Session connect(){
        return connector;
    }
}
