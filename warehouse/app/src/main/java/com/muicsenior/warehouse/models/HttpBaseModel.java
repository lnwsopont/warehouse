package com.muicsenior.warehouse.models;

import com.muicsenior.warehouse.libraries.HttpConnector;

/**
 * Created by Ta on 2017-08-10.
 */

public class HttpBaseModel extends BaseModel {
    protected HttpConnector connector;

    public HttpBaseModel(){
        connector = HttpConnector.connect();
    }

    protected HttpConnector connect(){
        return connector;
    }
}
