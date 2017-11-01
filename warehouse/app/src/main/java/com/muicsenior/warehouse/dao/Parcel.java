package com.muicsenior.warehouse.dao;

/**
 * Created by Asus on 9/27/2017.
 */

public class Parcel extends BaseDao {

    public String id;
    public int customerId;
    public enum STATUS{LOADING, IN_SHELF, UNKNOWN}
    public STATUS status = STATUS.LOADING;
    public Shelf shelf;

    public void from(Parcel other){
        id = other.id;
        customerId = other.customerId;
        status = other.status;
        shelf = other.shelf;
    }
}
