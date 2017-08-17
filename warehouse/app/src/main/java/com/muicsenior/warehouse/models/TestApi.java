package com.muicsenior.warehouse.models;

import com.tamemo.dao.JSON;
import com.tamemo.simplehttp.OnResponseJson;
import com.tamemo.simplehttp.Params;

/**
 * Created by Ta on 2017-08-10.
 */

public class TestApi extends HttpBaseModel{

    public void test(int x, final BaseCallback<String> callback){
        connect().GET("http://api.nainee.com/test/", new Params().with("x", 1), new OnResponseJson() {
            @Override
            public void onSuccess(JSON res) {
            }
        });
    }

}
