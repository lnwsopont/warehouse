package com.muicsenior.warehouse.dao;

/**
 * Created by Asus on 9/27/2017.
 */

public class Parcel extends BaseDao {

    public String id;
    public String name;
    public int customerId;
    public enum STATUS{IN_SHELF,UNKNOWN}
    public STATUS status;
    public Shelf shelf;
}
