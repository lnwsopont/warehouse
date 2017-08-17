package com.muicsenior.warehouse;

import android.app.Application;

import com.tamemo.Contextor;
import com.tamemo.simplehttp.Connection;
import com.tamemo.simplehttp.OnResponse;
import com.tamemo.simplehttp.Params;
import com.tamemo.simplehttp.Response;
import com.tamemo.simplehttp.Session;
import com.tamemo.simplehttp.SimpleHttp;

/**
 * Created by Ta on 2017-08-10.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.getInstance().init(getApplicationContext());
        test();
    }

    private void test(){
        SimpleHttp.session(1).mock(true, new Session.PreProcess() {
            @Override
            public boolean handle(Connection.Method method, String url, Params params, OnResponse res) {
                if(url.equals("http://api.mywarehouse.com/user/login")){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    res.onSuccess(new Response("{\n" +
                            "\tstatus: false,\n" +
                            "\tcode: 200,\n" +
                            "\tmsg: \"success\",\n" +
                            "\tdata: {\n" +
                            "\t\tid: \"1\",\n" +
                            "\t\tfirst_name: \"John\",\n" +
                            "\t\tlast_name: \"Wick\"\n" +
                            "\t}\n" +
                            "}"));
                    return true;
                }
                return false;
            }
        });
    }
}
