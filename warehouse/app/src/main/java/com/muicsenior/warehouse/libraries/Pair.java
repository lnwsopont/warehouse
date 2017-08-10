package com.muicsenior.warehouse.libraries;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ta on 24/2/2015.
 */
public class Pair {

    public static Pair noop(){
        return attach();
    }

    public static Pair attach(){
        return new Pair();
    }

    public static Pair attach(String key, Object value){
        return new Pair().with(key, value);
    }

    public static Pair attach(String key){
        return new Pair().with(key);
    }

    Map<String, String> params;

    public Pair(){
        params = new HashMap<String, String>();
    }

    public Pair with(String key){
        params.put(key, null);
        return this;
    }

    public Pair with(String key, Object value){
        params.put(key, value == null ? null : value.toString());
        return this;
    }

    public Map<String, String> build(){
        return params;
    }


}
