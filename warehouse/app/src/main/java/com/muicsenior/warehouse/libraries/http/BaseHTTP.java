package com.muicsenior.warehouse.libraries.http;

import com.muicsenior.warehouse.libraries.BaseDao;
import com.muicsenior.warehouse.libraries.Pair;
import com.squareup.okhttp.Call;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by ta on 12/02/2015.
 */
public class BaseHTTP extends HTTPEngine{

    public static BaseHTTP getInstance(){
        return (BaseHTTP) getInstance(false);
    }

    public static BaseHTTP getInstance(boolean createNew){
        if(createNew) {
            return new BaseHTTP();
        }
        if(instance == null) {
            instance = new BaseHTTP();
        }
        return (BaseHTTP) instance;
    }

    protected <T extends BaseDao> Call load(RequestMethod method, String url, Pair data, final HTTPEngineListener<T> listener, final Class<T> tClass){
        data = data == null ? Pair.noop() : data;
        return loadUrl(method, url, data.build(), listener, tClass);
    }

    protected <T extends BaseDao> Call load(RequestMethod method, String url, Pair data, final HTTPEngineListener<T> listener, final Type tType){
        data = data == null ? Pair.noop() : data;
        return loadUrl(method, url, data.build(), listener, tType);
    }

    public <T extends BaseDao> Call GET(String url, Pair data, final HTTPEngineListener<T> listener, final Class<T> tClass){
        url += "?";
        for(Map.Entry<String, String> entry : data.build().entrySet()) {
            url += entry.getKey() + (entry.getValue() == null ? "" : "=" + entry.getValue()) + "&";
        }
        url = url.substring(0, url.length() - 1);
        return load(RequestMethod.METHOD_GET, url, null, listener, tClass);
    }

    public <T extends BaseDao> Call GET(String url, Pair data, final HTTPEngineListener<T> listener, final Type tType){
        url += "?";
        for(Map.Entry<String, String> entry : data.build().entrySet()) {
            url += entry.getKey() + (entry.getValue() == null ? "" : "=" + entry.getValue()) + "&";
        }
        url = url.substring(0, url.length() - 1);
        return load(RequestMethod.METHOD_GET, url, null, listener, tType);
    }

    public <T extends BaseDao> Call POST(String url, Pair data, final HTTPEngineListener<T> listener, final Class<T> tClass){
        return load(RequestMethod.METHOD_POST, url, data, listener, tClass);
    }
}
