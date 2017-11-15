package com.muicsenior.warehouse.activities;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.muicsenior.warehouse.R;
import com.muicsenior.warehouse.dao.Parcel;
import com.muicsenior.warehouse.dao.Task;
import com.muicsenior.warehouse.dao.User;
import com.muicsenior.warehouse.models.BaseCallback;
import com.muicsenior.warehouse.models.CurrentScanTaskModel;
import com.muicsenior.warehouse.models.ModelManager;
import com.muicsenior.warehouse.models.ParcelModel;
import com.muicsenior.warehouse.models.UserModel;
import com.muicsenior.warehouse.views.ScanParcelListItem;
import com.muicsenior.warehouse.views.TaskItem;
import com.tamemo.Contextor;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    TextView userName,userId;
    ListView taskList;

    List<Task> tasks;
    UserTaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        User user = ModelManager.get(UserModel.class).getCurrentUser();
        userName = (TextView) findViewById(R.id.user_profile_name);
        userId = (TextView) findViewById(R.id.user_profile_id);

        taskList = (ListView) findViewById(R.id.task_list);
        adapter = new UserTaskAdapter(this);
        taskList.setAdapter(adapter);

        ModelManager.get(UserModel.class).loadTasks(new BaseCallback<List<Task>>() {
            @Override
            public void success(List<Task> result) {
                tasks = result;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void fail(int status, String message) {
                Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

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

    class UserTaskAdapter extends ArrayAdapter<String> {


        public UserTaskAdapter(@NonNull Context context) {
            super(context, -1);
        }

        @Override
        public int getCount() {
            return tasks == null ? 1 : tasks.size();
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TaskItem item = new TaskItem(ProfileActivity.this);
            if(tasks == null){
                item.id.setText("");
                item.desc.setText("loading...");
            }
            else {
                item.id.setText("" + tasks.get(position).id);
                item.desc.setText(tasks.get(position).desc);
            }
            return item;
        }


    }
}
