package com.berlin.berlintv;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by berlin on 2016/7/15 0015.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        Button local_btn = (Button) findViewById(R.id.main_local_btn);
        local_btn.setOnClickListener(this);
        Button souhu_btn = (Button) findViewById(R.id.main_souhu_btn);
        souhu_btn.setOnClickListener(this);
        Button zhanqi_btn = (Button) findViewById(R.id.main_zhanqi_btn);
        zhanqi_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.main_local_btn:
                intent = new Intent(this,LocalActivity.class);
                break;
            case R.id.main_souhu_btn:
                intent = new Intent(this,SouHuListActivity.class);
                break;
            case R.id.main_zhanqi_btn:
                intent = new Intent(this,ZhanQiListActivity.class);
                break;
        }
        startActivity(intent);
    }
}
