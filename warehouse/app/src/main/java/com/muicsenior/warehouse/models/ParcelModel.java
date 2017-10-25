package com.muicsenior.warehouse.models;

import android.util.Log;

import com.muicsenior.warehouse.dao.Parcel;
import com.muicsenior.warehouse.dao.Shelf;
import com.tamemo.dao.JSON;
import com.tamemo.simplehttp.OnResponseJson;

/**
 * Created by Asus on 9/20/2017.
 */

public class ParcelModel extends HttpBaseModel {
    public void getShelf(String parcelCode, final BaseCallback<Shelf> callback) {
        final Shelf shelf = new Shelf();

        connect().GET(ROOT + "/api/parcel/" + parcelCode + "/shelf", new OnResponseJson() {
                    @Override
                    public void onSuccess(JSON res) {
                        shelf.code = res.get("code", String.class);
                        callback.success(shelf);
                    }
                }
        );
    }

    public void getInfo(String parcelCode, final BaseCallback<Parcel> callback) {
        final Parcel parcel = new Parcel();
        connect().GET(ROOT + "/api/parcel/" + parcelCode, new OnResponseJson() {
            @Override
            public void onSuccess(JSON res) {
                Log.i("aaa", res.toString());
                parcel.id = res.get("id", String.class);
                parcel.name = res.get("name", String.class);
                parcel.customerId = res.get("customer_id", int.class);
                String status = res.get("status", String.class);
                if ("inshelf".equals(status)) {
                    parcel.status = Parcel.STATUS.IN_SHELF;
                } else {
                    parcel.status = Parcel.STATUS.UNKNOWN;
                }
                callback.success(parcel);
            }

        });


    }


}