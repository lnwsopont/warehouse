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
                    currentUser.id  = Integer.parseInt(res.get("info").get("emp_id",String.class));
                    currentUser.firstName  = res.get("info").get("emp_fname",String.class);
                    currentUser.lastName  = res.get("info").get("emp_lname",String.class);
                    currentUser.tel  = res.get("info").get("emp_tel",String.class);
                    currentUser.thumbUrl  = res.get("info").get("emp_avatar",String.class);
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
