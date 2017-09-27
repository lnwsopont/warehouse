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
    EditText qrCode;

    private void requestCameraPermission(){
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
        qrCode=(EditText) findViewById(R.id.qr);

        surfaceView = (SurfaceView) findViewById(R.id.camera_view);
        /*surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                camera = Camera.open();
                Camera.Parameters param;
                param = camera.getParameters();
                param.setPreviewSize(352, 288);
                camera.setParameters(param);
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                if (surfaceHolder.getSurface() == null) {
                    return;
                }

                try {
                    camera.stopPreview();
                } catch (Exception e) {

                }

                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();
                } catch (Exception e) {

                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        });*/


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
                .height(surfaceView.getHeight())
                .width(surfaceView.getWidth())
                .build();


    }

    private void onReceiveString(String msg){
        qrCode.setText("QR");
        if(msg.startsWith("wh::")) {
            String customerCode, parcelCode;
            String[] s =msg.replace("wh::", "").split(":");
            Intent intent = new Intent();

            if(s.length!=2)
            {
                return;
            }
            customerCode = s[0];
            parcelCode = s[1];

            intent.putExtra("customerCode",customerCode);
            intent.putExtra("parcelCode",parcelCode);
            intent.putExtra("msg", msg);
            setResult(RESULT_OK, intent);
            finish();
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
