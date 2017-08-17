package com.muicsenior.warehouse.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.muicsenior.warehouse.R;
import com.muicsenior.warehouse.libraries.HttpConnector;
import com.muicsenior.warehouse.libraries.JSON;
import com.muicsenior.warehouse.libraries.Pair;
import com.muicsenior.warehouse.libraries.callbacker.CallbackerJSON;
import com.muicsenior.warehouse.libraries.callbacker.CallbackerString;
import com.muicsenior.warehouse.models.BaseCallback;
import com.muicsenior.warehouse.models.TestApi;
import com.tamemo.simplehttp.OnResponseJson;
import com.tamemo.simplehttp.SimpleHttp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView list1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleHttp.GET("http://api.nainee.com/test", new OnResponseJson() {
            @Override
            public void onSuccess(com.tamemo.dao.JSON res) {
                Toast.makeText(MainActivity.this, res.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    1
            );
        }

        Intent intent = new Intent(MainActivity.this, CamActivity.class);
        intent.putExtra("id", 123);
        startActivityForResult(intent, 666);

        if(true)return;

        TestApi api = new TestApi();
        api.test(2, new BaseCallback<String>() {
            @Override
            public void success(String result) {
                
            }

            @Override
            public void fail(int status, String message) {

            }
        });

        list1 = (ListView) findViewById(R.id.list1);

        custom();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 666){
            String msg = data.getStringExtra("msg");
            Toast.makeText(this, "msg: " + msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void basicConnect() {
        HttpConnector.connect().POST("http://api.nainee.com/test/", Pair.attach("x",1).with("y",2).build(), new CallbackerString() {
            @Override
            public void success(String message) {
                Toast.makeText(MainActivity.this,message.substring(0,100),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void fail(int statusCode, String message) {
                Toast.makeText(MainActivity.this,statusCode + ":" + message,Toast.LENGTH_SHORT).show();
            }
        });

        HttpConnector.connect().GET("http://api.nainee.com/test/", new CallbackerJSON() {
            @Override
            public void success(JSON o) {
                String s = o.get("data").get(0).get("desc", String.class);
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void custom(){
        final MyAdapter adapter = new MyAdapter(this);
        list1.setAdapter(adapter);
        list1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.remove(position);
                return false;
            }
        });

        /*
        list1.setOnItemLongClickListener {
            adapter.remove(position)
            return false
        }
        */
    }

    private void basic() {
        String[] src = {"a","b","c","d","e","f","g","h","i","j","k"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.template_list1,src);
        list1.setAdapter(adapter);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,"pos = " + position,Toast.LENGTH_SHORT).show();
            }
        });
    }
}

class MyAdapter extends ArrayAdapter<String>{

    Context mContext;
    List<Integer> data = new ArrayList<>();

    public MyAdapter(@NonNull Context context) {
        super(context, -1);
        mContext = context;
        for(int i = 1; i <= 100; i++){
            data.add(i);
        }
    }

    public void remove(int position){
        data.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.template_list1,parent,false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.txt_title);
        title.setText( "" + data.get(position) );

        TextView detail = (TextView) convertView.findViewById(R.id.txt_detail);
        detail.setText( "this is position " + data.get(position) );

        return convertView;

        /*
        TextView txt;
        if(convertView == null){
            txt = new TextView(mContext);
            txt.setText("this is position " + (position + 1) + " from new view");
        }
        else{
            txt = (TextView) convertView;
            txt.setText("this is position " + (position + 1) + " from reuse");
        }
        return txt;
        */
    }
}