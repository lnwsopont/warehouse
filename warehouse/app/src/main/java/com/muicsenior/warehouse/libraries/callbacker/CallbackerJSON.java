package com.muicsenior.warehouse.libraries.callbacker;


import com.muicsenior.warehouse.libraries.JSON;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ta on 19/2/2015.
 */
public abstract class CallbackerJSON extends AbstractCallback{

    protected Callbackable root = null;

    public CallbackerJSON(){
        this.root = null;
    }

    public CallbackerJSON(Callbackable root){
        this.root = root;
    }

    /*
     * =============================================
     *  SUCCESS
     * =============================================
     */

    public abstract void success(JSON o);

    //call from interface then parse to JSON before used
    public void success(String o){
        success(new JSON(o));
    }

    public CallbackerJSON run(){
        success(new JSON());
        return this;
    }

    public CallbackerJSON run(JSON o){
        success(o);
        return this;
    }

    /*
     * =============================================
     *  FAIL
     * =============================================
     */

    public void fail(int statusCode, String message){

        if(root != null) {
            root.fail(statusCode, message);
            return;
        }

        try {
            new JSONObject(message);
            fail(new JSON(message));
        }
        catch(JSONException e) {
            fail(new JSON());
        }
        catch(Exception e) {
            fail(new JSON());
        }
    }

    public void fail(JSON o){
        if(root != null) {
            root.fail(-1, o.toString());
        }
    }

    /*
     * =============================================
     *  no connection
     * =============================================
     */

    public void noResponse(){
        noConnection();
    }

    public void noConnection(){
        if(root != null) {
            root.noResponse();
        }
    }


}
