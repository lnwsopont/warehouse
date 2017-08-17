package com.muicsenior.warehouse.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ta on 2017-08-17.
 */

public class ModelManager {

    private static Map<String, BaseModel> register = new HashMap<>();

    public static <T extends BaseModel> T get(Class<T> c) {
        String name = c.getName().toString();
        if (!register.containsKey(name)) {
            try {
                register.put(name, c.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (T) register.get(name);
    }

}
