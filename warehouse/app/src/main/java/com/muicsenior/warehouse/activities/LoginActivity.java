package com.muicsenior.warehouse.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.muicsenior.warehouse.R;
import com.muicsenior.warehouse.dao.User;
import com.muicsenior.warehouse.models.BaseCallback;
import com.muicsenior.warehouse.models.ModelManager;
import com.muicsenior.warehouse.models.UserModel;

public class LoginActivity extends AppCompatActivity {

    EditText userId, pin;
    Button btnLogin;
    TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userId = (EditText) findViewById(R.id.user_id);
        pin = (EditText) findViewById(R.id.user_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        txtStatus = (TextView) findViewById(R.id.txt_status);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStatus.setText("connecting...");
                btnLogin.setEnabled(false);

                ModelManager.get(UserModel.class).login(Integer.parseInt(userId.getText().toString()), pin.getText().toString(), new BaseCallback<User>() {
                    @Override
                    public void success(User result) {
                        Toast.makeText(LoginActivity.this, "login success !", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void fail(int status, String message) {
                        txtStatus.setText("user id or password error !!");
                        btnLogin.setEnabled(true);
                    }
                });
            }
        });
    }
}
