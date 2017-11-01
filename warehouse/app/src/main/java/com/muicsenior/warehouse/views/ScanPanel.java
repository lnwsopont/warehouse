package com.muicsenior.warehouse.views;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.muicsenior.warehouse.R;
import com.muicsenior.warehouse.dao.Parcel;
import com.muicsenior.warehouse.models.BaseCallback;
import com.muicsenior.warehouse.models.CurrentScanTaskModel;
import com.muicsenior.warehouse.models.ParcelModel;
import com.tamemo.Contextor;
import com.tamemo.simplehttp.SimpleHttp;

/**
 * Created by Ta on 2017-10-25.
 */

public class ScanPanel extends RelativeLayout {

    View rootView;

    View layoutScanHasItem;
    View layoutScanEmpty;
    View btnScan;
    Button btnClear;
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
        btnScan = rootView.findViewById(R.id.btn_scan);
        btnClear = (Button) rootView.findViewById(R.id.btn_clear);
        parcelList = (ListView) rootView.findViewById(R.id.lv_parcel);

        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Confirm?")
                        .setMessage("do you want to remove parcels")
                        .setPositiveButton("Remove All", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CurrentScanTaskModel.sharedInstance().clear();
                                checkStateChange();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.btn_disable));

            }
        });

        adapter = new CurrentParcelTaskAdapter();
        parcelList.setAdapter(adapter);

        checkStateChange();
    }

    public void checkStateChange() {
        if (CurrentScanTaskModel.sharedInstance().size() > 0) {
            layoutScanEmpty.setVisibility(GONE);
            layoutScanHasItem.setVisibility(VISIBLE);
        } else {
            layoutScanEmpty.setVisibility(VISIBLE);
            layoutScanHasItem.setVisibility(GONE);
        }
        adapter.notifyDataSetChanged();
    }

    public void addParcelCode(String parcelCode) {
        Toast.makeText(Contextor.getInstance().getContext(), "Loading Parcel.... ", Toast.LENGTH_SHORT).show();
        CurrentScanTaskModel.sharedInstance().add(parcelCode);
        checkStateChange();
    }

    public void setOnScanListener(View.OnClickListener onScanListener) {
        layoutScanEmpty.setOnClickListener(onScanListener);
        btnScan.setOnClickListener(onScanListener);
    }


    public void renderParcelItem(ScanParcelListItem item, Parcel parcel) {
        item.id.setText("id: " + parcel.id);

        item.statusIn.setVisibility(View.GONE);
        item.statusOut.setVisibility(View.GONE);
        item.statusLoading.setVisibility(View.VISIBLE);

        if (parcel.status == Parcel.STATUS.IN_SHELF) {
            item.statusIn.setVisibility(View.VISIBLE);
            item.statusLoading.setVisibility(View.GONE);
        }

        if (parcel.status == Parcel.STATUS.UNKNOWN) {
            item.statusOut.setVisibility(View.VISIBLE);
            item.statusLoading.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }


    class CurrentParcelTaskAdapter extends ArrayAdapter<String> {

        ParcelModel parcelModel;

        public CurrentParcelTaskAdapter() {
            super(Contextor.getInstance().getContext(), -1);
            parcelModel = new ParcelModel();
        }

        @Override
        public int getCount() {
            return CurrentScanTaskModel.sharedInstance().size();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            final Parcel parcel = CurrentScanTaskModel.sharedInstance().get(position);
            final ScanParcelListItem item = new ScanParcelListItem(Contextor.getInstance().getContext());

            renderParcelItem(item, parcel);

            if (parcel.status == Parcel.STATUS.LOADING) {
                parcelModel.getInfo(parcel.id, new BaseCallback<Parcel>() {
                    @Override
                    public void success(Parcel result) {
                        parcel.from(result);
                        renderParcelItem(item, parcel);
                    }

                    @Override
                    public void fail(int status, String message) {

                    }
                });
            }

            return item;
        }


    }

}