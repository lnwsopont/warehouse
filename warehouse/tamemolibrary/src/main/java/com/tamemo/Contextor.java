package com.tamemo;

import android.content.Context;

/**
 * Created by ta on 11/2/2558.
 */
public class Contextor {

    private static Contextor instance;

    public static Contextor getInstance() {
        if (instance == null)
            instance = new Contextor();
        return instance;
    }

    private Context mContext;

    public void init(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

}
