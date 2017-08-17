package com.muicsenior.warehouse;

import android.app.Application;

import com.tamemo.Contextor;

/**
 * Created by Ta on 2017-08-10.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.getInstance().init(getApplicationContext());
        com.muicsenior.warehouse.libraries.Contextor.getInstance().init(getApplicationContext());
    }
}
