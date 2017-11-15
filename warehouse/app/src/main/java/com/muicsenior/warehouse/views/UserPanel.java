package com.muicsenior.warehouse.views;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muicsenior.warehouse.R;
import com.muicsenior.warehouse.activities.ProfileActivity;

/**
 * Created by Ta on 2017-10-25.
 */

public class UserPanel extends RelativeLayout {

    View rootView;

    ImageView thumb;
    View userProfile;


    public UserPanel(Context context) {
        super(context);
        initInflate();
    }

    public UserPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
    }

    public UserPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UserPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
    }

    private void initInflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.view_user_panel, this);
        userProfile = rootView.findViewById(R.id.user_profile);

    }




    private void initInstance(){
    }

    public void setThumb(String url){

    }
}
