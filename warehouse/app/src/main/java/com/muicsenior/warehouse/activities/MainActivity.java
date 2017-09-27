package com.muicsenior.warehouse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.muicsenior.warehouse.R;
import com.muicsenior.warehouse.dao.Shelf;
import com.muicsenior.warehouse.models.BaseCallback;
import com.muicsenior.warehouse.models.ModelManager;
import com.muicsenior.warehouse.models.ParcelModel;
import com.muicsenior.warehouse.models.UserModel;

public class MainActivity extends AppCompatActivity {

    Button btnScan;
    TextView tvCustomerCode,tvParcelCode,tvShelfCode;
    View btnScan2;

    private final int REQ_CAM = 123;

    UserModel userModel;
    ParcelModel parcelModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userModel = ModelManager.get(UserModel.class);
        parcelModel = ModelManager.get(ParcelModel.class);

        btnScan2 = findViewById(R.id.btn_scan_2);

        tvCustomerCode = (TextView) findViewById(R.id.tv_customer_code);
        tvParcelCode = (TextView) findViewById(R.id.tv_parcel_code);
        tvShelfCode = (TextView) findViewById(R.id.tv_shelf_code);



        btnScan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CamActivity.class);
                startActivityForResult(intent,REQ_CAM);
            }
        });
        //Intent intent = new Intent(this, CamActivity.class);
       // startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

     //   if(!userModel.isLogin()){
      //      Intent intent = new Intent(this, LoginActivity.class);
     //       startActivity(intent);
//       }
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CAM)
        {
           String customerCode = data.getStringExtra("customerCode");
            String parcelCode = data.getStringExtra("parcelCode");
            tvCustomerCode.setText("customer id="+customerCode);
            tvParcelCode.setText("parcel id ="+parcelCode);

            parcelModel.getShelf(parcelCode, new BaseCallback<Shelf>() {
                @Override
                public void success(Shelf result) {
                    tvShelfCode.setText("place at shelf:"+ result.code);
                }

                @Override
                public void fail(int status, String message) {

                }
            });


        }
    }
}