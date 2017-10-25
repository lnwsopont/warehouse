package com.muicsenior.warehouse.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.muicsenior.warehouse.R;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class CamActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    private Camera camera;

    private QREader qrEader;
    TextView txt;
    EditText qrManual;
    TextView qrDisplay;
    Button btnOk, btnManual;

    String currentQr = "";

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, 1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        requestCameraPermission();

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 100);

        txt = (TextView) findViewById(R.id.txt);
        qrManual = (EditText) findViewById(R.id.et_qr_manual);
        qrDisplay = (TextView) findViewById(R.id.tv_qr);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnManual = (Button) findViewById(R.id.btn_manual);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerCode, parcelCode;
                String[] s = currentQr.replace("wh::", "").split(":");
                Intent intent = new Intent();

                if (s.length != 2) {
                    return;
                }
                customerCode = s[0];
                parcelCode = s[1];

                intent.putExtra("customerCode", customerCode);
                intent.putExtra("parcelCode", parcelCode);
                intent.putExtra("qr", currentQr);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrDisplay.setVisibility(View.GONE);
                qrManual.setVisibility(View.VISIBLE);
            }
        });

        surfaceView = (SurfaceView) findViewById(R.id.camera_view);

        int w;
        qrEader = new QREader.Builder(this, surfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onReceiveString(data);
                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .width(w = surfaceView.getWidth())
                .height(w)
                .build();

    }

    private void onReceiveString(String qr) {
        qrDisplay.setText(qr);
        qrManual.setText(qr);
        if (qr.startsWith("wh::")) {
            currentQr = qr;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Init and Start with SurfaceView
        // -------------------------------
        qrEader.initAndStart(surfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Cleanup in onPause()
        // --------------------
        qrEader.releaseAndCleanup();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
