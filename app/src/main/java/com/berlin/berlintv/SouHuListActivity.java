package com.berlin.berlintv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.berlin.berlintv.okhttp.OKHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by berlin on 2016/7/15 0015.
 */
public class SouHuListActivity extends AppCompatActivity{

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
        adapter = new MyAdapter(SouHuListActivity.this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SouHuListActivity.this, PlayActivity.class);
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
        titleName.setText("搜狐直播");
    }

    public void getDate() {
        OKHttpRequest okHttpRequest = new OKHttpRequest();
        okHttpRequest.setMyCallBack(new OKHttpRequest.MyCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(SouHuListActivity.this,"请求失败，请重试!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String s) {
                if (s==null){
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject!=null&&jsonObject.optInt("status")==200){
                        JSONArray jsonArray = jsonObject.optJSONObject("data").optJSONArray("columns").getJSONObject(0).optJSONArray("data_list");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            TVBean tvBean = new TVBean();
                            tvBean.setName(jsonObject1.optString("name"));
                            tvBean.setPath(jsonObject1.optString("liveUrl"));
                            tvBean.setImgurl(jsonObject1.optString("icoBigPic"));
                            list.add(tvBean);
                        }
                    }
                   adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        okHttpRequest.get("http://s1.api.tv.itc.cn/v6/mobile/channelPageData/list.json?cursor=0&partner=51&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=5.8.0&area_code=11&sysver=5.1.1&plat=6&channel_id=90020000&poid=1&page_size=20");
    }
}
