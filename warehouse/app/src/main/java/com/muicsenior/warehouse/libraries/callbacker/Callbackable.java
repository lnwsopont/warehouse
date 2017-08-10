package com.muicsenior.warehouse.libraries.callbacker;

/**
 * Created by Ta on 25/08/58.
 */
public interface Callbackable{
    void success(String res);
    void fail(int code, String res);
    void noResponse();

}
