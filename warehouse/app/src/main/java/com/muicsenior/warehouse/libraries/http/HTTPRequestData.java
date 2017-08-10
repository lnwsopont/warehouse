package com.muicsenior.warehouse.libraries.http;

/**
 * Created by ta on 12/02/2015.
 */

import com.squareup.okhttp.Call;

import java.util.Map;

public class HTTPRequestData {

    public String url;
    public HTTPEngine.RequestMethod method;
    public Map<String, String> postData;
    public Call call;

}
