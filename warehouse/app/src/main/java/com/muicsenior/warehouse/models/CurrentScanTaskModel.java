package com.muicsenior.warehouse.models;

import android.util.Log;

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

    public boolean add(String parcelCode){
        final Parcel parcel = new Parcel();
        parcel.id = parcelCode;

        if(parcels.contains(parcel)){
            return false;
        }

        parcels.add(parcel);
        return true;
    }
    public void submit(int index,BaseCallback <Boolean> result){
        String[] arr ={parcels.get(index).id};
        ParcelModel parcelModel = new ParcelModel();
        parcelModel.submit(arr ,result);
    }
    public void submit(BaseCallback <Boolean> result){
        String[] arr = new String[parcels.size()];
        int i = 0;
        for(Parcel p: parcels){
            arr[i++] = p.id;
        }
        ParcelModel parcelModel = new ParcelModel();
        parcelModel.submit(arr ,result);
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
