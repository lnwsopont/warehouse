package com.muicsenior.warehouse.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muicsenior.warehouse.R;

/**
 * Created by Asus on 11/15/2017.
 */

public class TaskItem extends LinearLayout {

    View rootView;
    public TextView id,desc;

    public TaskItem(Context context) {
        super(context);
        initInflate();
    }

    public TaskItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initInflate();
    }

    public TaskItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TaskItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
    }

    private void initInflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.view_task_item, this);

        id = (TextView) rootView.findViewById(R.id.task_id);
        desc = (TextView) rootView.findViewById(R.id.task_desc);

    }
}
