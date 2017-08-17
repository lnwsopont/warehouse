package com.muicsenior.warehouse.models;

import com.muicsenior.warehouse.libraries.JSON;
import com.muicsenior.warehouse.libraries.Pair;
import com.muicsenior.warehouse.libraries.callbacker.CallbackerJSON;

/**
 * Created by Ta on 2017-08-10.
 */

public class TestApi extends HttpBaseModel{

    public void test(int x, final BaseCallback<String> callback){
        connect().GET("http://api.nainee.com/test/", Pair.attach("x",x).build(), new CallbackerJSON() {
            @Override
            public void success(JSON o) {
                String s = o.get("data").get(0).get("desc", String.class);
                callback.success(s);
            }

            @Override
            public void fail(int statusCode, String message) {
                callback.fail(statusCode,message);
            }
        });
    }

}
