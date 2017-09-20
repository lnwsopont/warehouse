package com.muicsenior.warehouse.models;

import com.muicsenior.warehouse.dao.Shelf;
import com.tamemo.dao.JSON;
import com.tamemo.simplehttp.OnResponseJson;

/**
 * Created by Asus on 9/20/2017.
 */

public class ParcelModel extends HttpBaseModel {
    public void getShelf(String parcelCode, final BaseCallback<Shelf>callback) {
       final Shelf shelf = new Shelf();
        parcelCode="1234";
        connect().GET("http://api.mywarehouse.com/parcel/" + parcelCode + "/shelf", new OnResponseJson() {
                    @Override
                    public void onSuccess(JSON res) {
                        shelf.code = res.get("code",String.class);
                        callback.success(shelf);
                    }
                }
        );
    }


}
