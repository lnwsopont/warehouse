package com.muicsenior.warehouse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.muicsenior.warehouse.R;
import com.muicsenior.warehouse.dao.Parcel;
import com.muicsenior.warehouse.dao.Shelf;
import com.muicsenior.warehouse.models.BaseCallback;
import com.muicsenior.warehouse.models.ModelManager;
import com.muicsenior.warehouse.models.ParcelModel;
import com.muicsenior.warehouse.models.UserModel;
import com.squareup.picasso.Picasso;

public class MainmenuActivity extends AppCompatActivity {

    Button btnScanner,btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        btnScanner = (Button) findViewById(R.id.btn_scanner);
        btnProfile = (Button) findViewById(R.id.btn_profile);

        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainmenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
