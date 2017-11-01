package com.muicsenior.warehouse.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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
    TextView scanStatus;
    EditText qrManual;
    TextView qrDisplay;
    Button btnOk, btnManual;
    View manualInput;

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
        scanStatus = (TextView) findViewById(R.id.tv_scan_format_status);
        qrManual = (EditText) findViewById(R.id.et_qr_manual);
        qrDisplay = (TextView) findViewById(R.id.tv_qr);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnManual = (Button) findViewById(R.id.btn_manual);
        manualInput = findViewById(R.id.manual_input);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String parcelCode = currentQr.replace("wh::", "");

                Intent intent = new Intent();
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
                manualInput.setVisibility(View.VISIBLE);
            }
        });

        qrManual.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                onReceiveString("wh::" + qrManual.getText().toString());
                return false;
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

        if (qr.startsWith("wh::")) {

            qrDisplay.setText(qr);
            qrManual.setText(qr.replace("wh::", ""));

            currentQr = qr;
            scanStatus.setVisibility(View.GONE);
            btnOk.setEnabled(true);
            btnOk.setBackgroundColor(getResources().getColor(R.color.btn_ok));
        }
        else if(currentQr.isEmpty()){

            qrDisplay.setText(qr);
            qrManual.setText(qr);

            scanStatus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        qrEader.start();
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
