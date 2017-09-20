package com.muicsenior.warehouse.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.muicsenior.warehouse.R;
import com.tamemo.simplehttp.OnResponseString;
import com.tamemo.simplehttp.SimpleHttp;

public class IncActivity extends AppCompatActivity {

    TextView value;
    EditText newValue;
    EditText session;
    Button btn, fetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inc);

        value = (TextView) findViewById(R.id.tv_value);
        newValue = (EditText) findViewById(R.id.new_value);
        session = (EditText) findViewById(R.id.session);

        btn = (Button) findViewById(R.id.btn);
        fetch = (Button) findViewById(R.id.fetch);

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleHttp.session(getSessionId()).GET("http://r.nainee.com/inc.php", new OnResponseString() {
                    @Override
                    public void onSuccess(String message) {
                        value.setText(message);
                    }
                });
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = newValue.getText().toString();
                SimpleHttp.session(getSessionId()).GET("http://r.nainee.com/inc.php?x=" + val, new OnResponseString() {
                    @Override
                    public void onSuccess(String message) {
                        value.setText(message);
                    }
                });
            }
        });


    }

    private int getSessionId() {
        return Integer.parseInt(session.getText().toString());
    }
}
