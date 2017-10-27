package com.muicsenior.warehouse.models;

import com.muicsenior.warehouse.dao.Parcel;
import com.muicsenior.warehouse.dao.Shelf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ta on 2017-10-27.
 */

public class CurentScanTaskModel extends BaseModel {

    List<String> qrCodes = new ArrayList<>();

    private static CurentScanTaskModel instance;

    public static CurentScanTaskModel sharedInstance(){
        if(instance == null){
            instance = new CurentScanTaskModel();
        }
        return instance;
    }

    public void add(String qr){
        qrCodes.add(qr);
    }

    public void clear(){
        qrCodes.clear();
    }

    public void clear(String qr){
        qrCodes.remove(qr);
    }

    public int size(){
        return qrCodes.size();
    }

    public Parcel get(int index){
        Parcel parcel = new Parcel();
        parcel.name = qrCodes.get(index);
        parcel.id = "12345";
        parcel.shelf = new Shelf();
        parcel.shelf.code = Math.random() > 0.5 ? "A1" : null;
        return parcel;
    }

}
