package com.muicsenior.warehouse;

import android.app.Application;

import com.muicsenior.warehouse.libraries.Contextor;

/**
 * Created by Ta on 2017-08-10.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.getInstance().init(getApplicationContext());
    }
}
