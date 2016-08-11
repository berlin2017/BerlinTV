package com.berlin.berlintv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LocalActivity extends AppCompatActivity {

    private ListView listView;
    private List<TVBean> list = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        initTitle();
        getDate();
        adapter = new MyAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LocalActivity.this, PlayActivity.class);
                intent.putExtra("url", list.get(position).getPath());
                startActivity(intent);
            }
        });
    }
    private void initTitle() {
        ImageView back = (ImageView) findViewById(R.id.title_back_img);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("本地直播");
    }

    public void getDate() {
        try {
            String info = "";
            InputStream inputStream = getResources().openRawResource(R.raw.list3);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String s = "";
            while ((s = bufferedReader.readLine()) != null) {
                Log.i("info", info);
                info += s;
            }
            Gson gson = new Gson();
            JSONArray json = new JSONArray(info);
            if (info == null) {
                return;
            }
            for (int i = 0; i < json.length(); i++) {
                list.add(gson.fromJson(json.optJSONObject(i).toString(), TVBean.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
