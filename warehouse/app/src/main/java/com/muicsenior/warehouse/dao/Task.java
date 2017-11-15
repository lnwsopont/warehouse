package com.muicsenior.warehouse.dao;

/**
 * Created by Asus on 11/15/2017.
 */

public class Task extends BaseDao {
    public int id;
    public String desc;

    public Task() {
        this.id = 0;
        this.desc = "";
    }

    public Task(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
