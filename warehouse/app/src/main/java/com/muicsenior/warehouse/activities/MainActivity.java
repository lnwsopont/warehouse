package com.muicsenior.warehouse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.muicsenior.warehouse.R;
import com.muicsenior.warehouse.models.ModelManager;
import com.muicsenior.warehouse.models.UserModel;

public class MainActivity extends AppCompatActivity {

    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userModel = ModelManager.get(UserModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!userModel.isLogin()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}