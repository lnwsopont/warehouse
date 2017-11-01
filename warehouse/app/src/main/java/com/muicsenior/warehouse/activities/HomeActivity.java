package com.muicsenior.warehouse.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.muicsenior.warehouse.R;
import com.muicsenior.warehouse.views.ScanPanel;

public class HomeActivity extends AppCompatActivity {


    private final int REQ_CAM = 1001;

    ScanPanel scanPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        scanPanel = (ScanPanel) findViewById(R.id.scan_panel);
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


    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CAM && data != null) {
            String parcelCode = data.getStringExtra("parcelCode");
            scanPanel.addParcelCode(parcelCode);
        }
    }
}
