package com.muicsenior.warehouse.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.muicsenior.warehouse.R;
import com.tamemo.Contextor;

/**
 * Created by Ta on 2017-10-25.
 */

public class ScanPanel extends RelativeLayout {
    public ScanPanel(Context context) {
        super(context);
        initInflate();
    }

    public ScanPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
    }

    public ScanPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScanPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
    }

    private void initInflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_scan_panel, this);
    }

    public void addQr(String qr){
        Toast.makeText(Contextor.getInstance().getContext(), qr, Toast.LENGTH_SHORT).show();
    }
}
