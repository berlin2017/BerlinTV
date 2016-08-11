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
public class ZhanQiListActivity extends AppCompatActivity {

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
        adapter = new MyAdapter(ZhanQiListActivity.this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getPath(list.get(position));
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
                Toast.makeText(ZhanQiListActivity.this, "请求失败，请重试!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String s) {
                if (s == null) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject != null && jsonObject.optInt("code") == 0) {
                        JSONArray jsonArray = jsonObject.optJSONObject("data").optJSONArray("rooms");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            final TVBean tvBean = new TVBean();
                            tvBean.setName(jsonObject1.optString("title"));
                            tvBean.setImgurl(jsonObject1.optString("bpic"));
                            tvBean.setId(Integer.parseInt(jsonObject1.optString("uid")));
                            list.add(tvBean);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        okHttpRequest.get("http://www.zhanqi.tv/api/static/live.hots/20-1.json");
    }

    public void getPath(TVBean tvBean) {
        OKHttpRequest okHttpRequest1 = new OKHttpRequest();
        okHttpRequest1.setMyCallBack(new OKHttpRequest.MyCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(ZhanQiListActivity.this, "请求失败，请重试!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject o = new JSONObject(s).optJSONObject("data").optJSONArray("videos").getJSONObject(0);

                    Intent intent = new Intent(ZhanQiListActivity.this, PlayActivity.class);
                    intent.putExtra("url", o.optJSONObject("flashvars").optString("VideoUrl") + o.optString("playUrl"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        okHttpRequest1.get("http://www.zhanqi.tv/api/static/video.anchor_hots/" + tvBean.getId() + "-20-1.json?ver=2.7.1&os=3");
    }
}
