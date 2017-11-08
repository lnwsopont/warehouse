package com.muicsenior.warehouse.dao;

import android.util.Log;

/**
 * Created by Asus on 9/27/2017.
 */

public class Parcel extends BaseDao {

    public String id;
    public int customerId;
    // loading = wait for http connection
    // 1 = new parcel incoming waiting for check-in
    // 2 = parcel in shelf, want to check-out
    // 3 = parcel already check-out -> code expired
    public enum STATUS{LOADING, CHECK_IN, CHECK_OUT, EXPIRED}
    public STATUS status = STATUS.LOADING;
    public Shelf shelf = new Shelf();

    public void from(Parcel other){
        id = other.id;
        customerId = other.customerId;
        status = other.status;
        shelf = other.shelf;
    }

    @Override
    public boolean equals(Object obj) {
        if( ! (obj instanceof Parcel) ){
            return false;
        }
        return id.equals(((Parcel) obj).id);
    }
}
