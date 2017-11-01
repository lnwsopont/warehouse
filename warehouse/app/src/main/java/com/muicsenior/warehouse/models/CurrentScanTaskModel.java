package com.muicsenior.warehouse.models;

import com.muicsenior.warehouse.dao.Parcel;
import com.muicsenior.warehouse.dao.Shelf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ta on 2017-10-27.
 */

public class CurrentScanTaskModel extends BaseModel {

    List<Parcel> parcels = new ArrayList<>();

    private static CurrentScanTaskModel instance;

    public static CurrentScanTaskModel sharedInstance(){
        if(instance == null){
            instance = new CurrentScanTaskModel();
        }
        return instance;
    }

    public void add(String parcelCode){
        final Parcel parcel = new Parcel();
        parcel.id = parcelCode;
        parcels.add(parcel);
    }

    public void clear(){
        parcels.clear();
    }

    public void clear(int index){
        parcels.remove(index);
    }

    public int size(){
        return parcels.size();
    }

    public Parcel get(int index){
        return parcels.get(index);
    }

}
