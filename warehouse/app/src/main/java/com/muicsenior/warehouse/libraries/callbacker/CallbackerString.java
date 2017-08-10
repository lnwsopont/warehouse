package com.muicsenior.warehouse.libraries.callbacker;


import com.muicsenior.warehouse.libraries.JSON;

/**
 * Created by ta on 19/2/2015.
 */
public abstract class CallbackerString extends CallbackerJSON{

    public abstract void success(String message);
    
    public void success(JSON o){
        success(o.toString());
    }

}
