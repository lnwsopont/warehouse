package com.muicsenior.warehouse.libraries.http;

/**
 * Created by ta on 12/02/2015.
 */
public interface HTTPRequestListener {

    public void onMessageReceived(int statusCode, final String message);
    public void onMessageError(int statusCode, final String message);

}
