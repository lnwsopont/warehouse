package com.muicsenior.warehouse.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.muicsenior.warehouse.R;
import com.muicsenior.warehouse.models.ModelManager;
import com.muicsenior.warehouse.models.UserModel;
import com.muicsenior.warehouse.views.ScanPanel;
import com.muicsenior.warehouse.views.UserPanel;

public class HomeActivity extends AppCompatActivity {


    private final int REQ_CAM = 1001;

    ScanPanel scanPanel;
    UserPanel userPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        scanPanel = (ScanPanel) findViewById(R.id.scan_panel);
        userPanel = (UserPanel) findViewById(R.id.user_panel);
        userPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileActivity();
            }
        });
        scanPanel.setOnScanListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamActivity();
            }
        });
    }

    private void openCamActivity(){
        Intent intent = new Intent(this, CamActivity.class);
        startActivityForResult(intent, REQ_CAM);
    }
    private void openProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CAM && data != null) {
            String parcelCode = data.getStringExtra("parcelCode");
            scanPanel.addParcelCode(parcelCode);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (!ModelManager.get(UserModel.class).isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
