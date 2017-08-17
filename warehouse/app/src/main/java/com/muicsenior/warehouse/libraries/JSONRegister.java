package com.muicsenior.warehouse.libraries;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ta on 2/4/2015.
 */
public class JSONRegister {

    private static JSONRegister instance;

    public static JSONRegister getInstance(){
        if(instance == null) {
            instance = new JSONRegister();
        }
        return instance;
    }

    private Map<String, String> storage;

    public JSONRegister(){
        storage = new HashMap<String, String>();
    }

    public void put(String key, String val){
        storage.put(key, val);
    }

    public JSON get(String key){
        return storage.containsKey(key) ? new JSON(storage.get(key)) : new JSON();
    }

    public JSON pop(String key){
        JSON o = get(key);
        storage.remove(key);
        return o;
    }
}