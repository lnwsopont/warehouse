package com.muicsenior.warehouse.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.muicsenior.warehouse.R;
import com.muicsenior.warehouse.dao.Parcel;
import com.muicsenior.warehouse.models.CurentScanTaskModel;
import com.tamemo.Contextor;

/**
 * Created by Ta on 2017-10-25.
 */

public class ScanPanel extends RelativeLayout {

    View rootView;

    View layoutScanHasItem;
    View layoutScanEmpty;
    Button btnScan, btnClear;
    ListView parcelList;
    CurrentParcelTaskAdapter adapter;

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
        rootView = inflater.inflate(R.layout.view_scan_panel, this);

        layoutScanEmpty = rootView.findViewById(R.id.scan_empty);
        layoutScanHasItem = rootView.findViewById(R.id.scan_has_item);
        btnScan = (Button) rootView.findViewById(R.id.btn_scan);
        btnClear = (Button) rootView.findViewById(R.id.btn_clear);
        parcelList = (ListView) rootView.findViewById(R.id.lv_parcel);

        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CurentScanTaskModel.sharedInstance().clear();
                checkStateChange();
            }
        });

        adapter = new CurrentParcelTaskAdapter();
        parcelList.setAdapter(adapter);

        checkStateChange();
    }

    public void checkStateChange(){
        if(CurentScanTaskModel.sharedInstance().size() > 0){
            layoutScanEmpty.setVisibility(GONE);
            layoutScanHasItem.setVisibility(VISIBLE);
        }
        else{
            layoutScanEmpty.setVisibility(VISIBLE);
            layoutScanHasItem.setVisibility(GONE);
        }
        adapter.notifyDataSetChanged();
    }

    public void addQr(String qr){
        Toast.makeText(Contextor.getInstance().getContext(), qr, Toast.LENGTH_SHORT).show();
        CurentScanTaskModel.sharedInstance().add(qr);
        checkStateChange();
    }

    public void setOnScanListener(View.OnClickListener onScanListener){
        layoutScanEmpty.setOnClickListener(onScanListener);
        btnScan.setOnClickListener(onScanListener);
    }
}

class CurrentParcelTaskAdapter extends ArrayAdapter<String>{

    public CurrentParcelTaskAdapter() {
        super(Contextor.getInstance().getContext(), -1);
    }

    @Override
    public int getCount() {
        return CurentScanTaskModel.sharedInstance().size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Parcel parcel = CurentScanTaskModel.sharedInstance().get(position);

        ScanParcelListItem item = new ScanParcelListItem(Contextor.getInstance().getContext());
        item.name.setText(parcel.name);
        item.id.setText(parcel.id);
        item.status.setText(parcel.shelf.code == null ? "" : "in shelf: " + parcel.shelf.code);

        return item;
    }
}