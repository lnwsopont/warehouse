package com.muicsenior.warehouse.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.muicsenior.warehouse.R;
import com.muicsenior.warehouse.dao.User;
import com.muicsenior.warehouse.models.ModelManager;
import com.muicsenior.warehouse.models.UserModel;

public class ProfileActivity extends AppCompatActivity {
    TextView userName,userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        User user = ModelManager.get(UserModel.class).getCurrentUser();
        userName = (TextView) findViewById(R.id.user_profile_name);
        userId = (TextView) findViewById(R.id.user_profile_id);

        userName.setText(user.firstName + " " + user.lastName);
        userId.setText("id: " + user.id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.logout){
            Toast.makeText(this, "Log Out Successful", Toast.LENGTH_SHORT).show();
            ModelManager.get(UserModel.class).logout();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
