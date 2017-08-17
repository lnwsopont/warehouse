package com.muicsenior.warehouse.activities;

import android.graphics.PointF;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.muicsenior.warehouse.R;

import java.io.IOException;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class CamActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    private Camera camera;

    private QREader qrEader;
    TextView txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        txt = (TextView) findViewById(R.id.txt);

        txt.setText("test");

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
                        txt.setText("QREader Value : " + data);
                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(surfaceView.getHeight())
                .width(surfaceView.getWidth())
                .build();


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
