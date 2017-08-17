package com.muicsenior.warehouse.libraries.http;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.muicsenior.warehouse.libraries.BaseDao;
import com.muicsenior.warehouse.libraries.Contextor;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ta on 12/02/2015.
 */
public class HTTPEngine{

    protected static HTTPEngine instance;

    public static HTTPEngine getInstance(){
        if(instance == null)
            instance = new HTTPEngine();
        return instance;
    }

    public enum RequestMethod{
        METHOD_GET,
        METHOD_POST
    }

    private Context mContext;
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

    protected HTTPEngine(){
        mContext = Contextor.getInstance().getContext();
    }

    protected <T extends BaseDao> Call loadUrl(RequestMethod method, String url, Map<String, String> postData, final HTTPEngineListener<T> listener, final Class<T> tClass){
        Request request;
        if(method == RequestMethod.METHOD_POST) {
            Map<String, String> postParams = new HashMap<String, String>();
            if(postData != null) {
                for(Map.Entry<String, String> entry : postData.entrySet()) {
                    postParams.put(entry.getKey(), entry.getValue());
                }
            }
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, mapToPostString(postParams));

            request = new Request.Builder().url(url).post(body).build();
        }
        else {
            request = new Request.Builder().url(url).build();
        }

        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        Call call = client.newCall(request);

        HTTPRequestData data = new HTTPRequestData();
        data.url = url;
        data.method = method;
        data.postData = postData;
        data.call = call;

        Log.i("aaa", "url:" + url);

        new HTTPRequestTask(new HTTPRequestListener(){
            @Override
            public void onMessageReceived(int statusCode, String message){
                if(listener != null) {
                    String resp = message;
                    Log.i("aaa", "onMessageReceived res:" + tClass.toString() + " / " + message);
                    try {
                        T data = gson.fromJson(resp, tClass);
                        Log.i("aaa", "onMessageReceived done:");
                        listener.onResponse(data, resp);
                    } catch(Exception e) {
                        Log.i("aaa", "onMessageReceived fail:");
                        e.printStackTrace();
                        try {
                            listener.onResponse(null, resp);
                        } catch(Exception e2) {
                            listener.onResponse(null, "");
                        }
                    }
                }
            }

            @Override
            public void onMessageError(int statusCode, String message){
                if(listener != null) {
                    HTTPEngineException error = new HTTPEngineException("Cannot load data", statusCode);
                    listener.onFailure(null, error.getMessage(), error);
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);

        return call;
    }

    protected <T extends BaseDao> Call loadUrl(RequestMethod method, String url, Map<String, String> postData, final HTTPEngineListener<T> listener, final Type tType){
        Request request;
        if(method == RequestMethod.METHOD_POST) {
            Map<String, String> postParams = new HashMap<String, String>();
            if(postData != null) {
                for(Map.Entry<String, String> entry : postData.entrySet()) {
                    postParams.put(entry.getKey(), entry.getValue());
                }
            }
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, mapToPostString(postParams));

            request = new Request.Builder().url(url).post(body).build();
        }
        else {
            request = new Request.Builder().url(url).build();
        }

        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        Call call = client.newCall(request);

        HTTPRequestData data = new HTTPRequestData();
        data.url = url;
        data.method = method;
        data.postData = postData;
        data.call = call;

        Log.i("aaa", "url:" + url);

        new HTTPRequestTask(new HTTPRequestListener(){
            @Override
            public void onMessageReceived(int statusCode, String message){
                if(listener != null) {
                    String resp = message;
                    Log.i("aaa", "onMessageReceived res:" + tType.getClass() + " / " + message);
                    try {
                        T data = gson.fromJson(resp, tType);
                        Log.i("aaa", "onMessageReceived done:");
                        listener.onResponse(data, resp);
                    } catch(IndexOutOfBoundsException e) {
                        Log.i("aaa", "onMessageReceived fail:");
                        e.printStackTrace();
                        try {
                            listener.onResponse(null, resp);
                        } catch(Exception e2) {
                            listener.onResponse(null, "");
                        }
                    }
                }
            }

            @Override
            public void onMessageError(int statusCode, String message){
                if(listener != null) {
                    HTTPEngineException error = new HTTPEngineException("Cannot load data", statusCode);
                    listener.onFailure(null, error.getMessage(), error);
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);

        return call;
    }

    public <T extends BaseDao> Call loadPostUrl(String url, Map<String, String> postData, final HTTPEngineListener<T> listener, final Class<T> tClass){
        return loadUrl(RequestMethod.METHOD_POST, url, postData, listener, tClass);
    }

    public <T extends BaseDao> Call loadGetUrl(String url, final HTTPEngineListener<T> listener, final Class<T> tClass){
        return loadUrl(RequestMethod.METHOD_GET, url, null, listener, tClass);
    }

    protected String mapToPostString(Map<String, String> data){
        StringBuilder content = new StringBuilder();
        for(Map.Entry<String, String> entry : data.entrySet()) {
            if(content.length() > 0) {
                content.append('&');
            }
            try {
                content.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch(UnsupportedEncodingException e) {
                throw new AssertionError(e);
            }
        }
        return content.toString();
    }

    ///////////////////////////////////////////
    // Warning:
    // Doesn't work for test, just a pattern
    ///////////////////////////////////////////

    public Call loadBlogList(int blogCategoryId, int beforeId, final HTTPEngineListener<BaseDao> listener){
        Map<String, String> postData = new HashMap<String, String>();
        if(blogCategoryId >= 0)
            postData.put("blog_category_id", blogCategoryId + "");
        if(beforeId > 0)
            postData.put("before_id", beforeId + "");
        return loadPostUrl("http://nuuneoi.com/api/blog/list", postData, listener, BaseDao.class);
    }
}