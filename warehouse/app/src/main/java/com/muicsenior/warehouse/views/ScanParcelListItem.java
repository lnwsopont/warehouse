package com.muicsenior.warehouse.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muicsenior.warehouse.R;

/**
 * Created by Ta on 2017-10-25.
 */

public class ScanParcelListItem extends RelativeLayout {

    View rootView;
    public TextView id, statusIn, statusOut, statusLoading;

    public ScanParcelListItem(Context context) {
        super(context);
        initInflate();
    }

    public ScanParcelListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
    }

    public ScanParcelListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScanParcelListItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
    }

    private void initInflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.view_scan_parcel_list_item, this);

        id = (TextView) rootView.findViewById(R.id.tv_parcel_id);
        statusIn = (TextView) rootView.findViewById(R.id.tv_parcel_status_in);
        statusOut = (TextView) rootView.findViewById(R.id.tv_parcel_status_out);
        statusLoading = (TextView) rootView.findViewById(R.id.tv_parcel_status_loading);
    }
}
