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
                parcel.id = res.get("info").get("parcel_id", String.class);
                parcel.shelf.code = res.get("info").get("shelf_code", String.class);
                //parcel.customerId = res.get("customer_id", int.class);
                String status = res.get("info").get("parcel_status", String.class);

                if ("1".equals(status)) {
                    parcel.status = Parcel.STATUS.CHECK_IN;
                } else if ("2".equals(status)) {
                    parcel.status = Parcel.STATUS.CHECK_OUT;
                } else {
                    parcel.status = Parcel.STATUS.EXPIRED;
                }
                callback.success(parcel);
            }

        });


    }


}
