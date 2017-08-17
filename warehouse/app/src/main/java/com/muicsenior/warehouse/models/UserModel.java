package com.muicsenior.warehouse.models;

import com.muicsenior.warehouse.dao.User;
import com.tamemo.dao.JSON;
import com.tamemo.simplehttp.OnResponseJson;
import com.tamemo.simplehttp.Params;

/**
 * Created by Ta on 2017-08-17.
 */

public class UserModel extends HttpBaseModel {

    private User currentUser;

    public void login(String id, String password, final BaseCallback<User> callback) {
        connect().GET("http://api.mywarehouse.com/user/login", new Params().with("id", id).with("password", password), new OnResponseJson() {
            @Override
            public void onSuccess(JSON res) {
                boolean status = res.get("status", boolean.class);
                if(status){
                    currentUser = new User();
                    currentUser.id = res.get("data").get("id", String.class);
                    currentUser.firstName = res.get("data").get("first_name", String.class);
                    currentUser.lastName = res.get("data").get("last_name", String.class);
                    callback.success(currentUser);
                }
                else{
                    callback.fail(res.get("code", int.class), res.get("msg", String.class));
                }
            }

            @Override
            public void onFail(int statusCode, String message) {
                callback.fail(statusCode,message);
            }
        });
    }

    public void logout(){
        currentUser = null;
    }

    public boolean isLogin(){
        return currentUser != null;
    }

    public User getCurrentUser(){
        return currentUser;
    }

}
