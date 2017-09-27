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

    public void login(int id, String pin, final BaseCallback<User> callback) {
        Params params = new Params();
        params.with("id",id);
        params.with("pin",pin);
        connect().POST(ROOT + "/api/user/login",params, new OnResponseJson() {
            @Override
            public void onSuccess(JSON res) {
                if(res.get("login",boolean.class)){
                    currentUser = new User();
                    currentUser.id  = res.get("info").get("id",int.class);
                    currentUser.name  = res.get("info").get("name",String.class);
                    currentUser.tel  = res.get("info").get("tel",String.class);
                    currentUser.thumbUrl  = res.get("info").get("thumbUrl",String.class);
                    callback.success(currentUser);
                }
                else{
                    logout();
                    callback.fail(-1,null);
                }
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
