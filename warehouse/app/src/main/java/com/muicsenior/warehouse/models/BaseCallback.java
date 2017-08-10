package com.muicsenior.warehouse.models;

/**
 * Created by Ta on 2017-08-10.
 */

public abstract class BaseCallback<T> {
    public abstract void success(T result);
    public abstract void fail(int status, String message);
}
