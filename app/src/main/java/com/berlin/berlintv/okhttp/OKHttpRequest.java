package com.berlin.berlintv.okhttp;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by berlin on 2016/7/15 0015.
 */
public class OKHttpRequest {

    private OkHttpClient okHttpClient;
    private Handler handler;
    private MyCallBack myCallBack;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public OKHttpRequest() {
        okHttpClient = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }

    public void setMyCallBack(MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
    }

    public void get(String url) {

        okHttpClient.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
                if (myCallBack != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            myCallBack.onFailure(call, e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                if (myCallBack != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            myCallBack.onResponse(s);
                        }
                    });
                }
            }
        });
    }

    public void post(String url, Map<String, String> data) {

        String jsonbody = new JSONObject(data).toString();
        try {
           okHttpClient.newCall(new Request.Builder().url(url).post(RequestBody.create(JSON, jsonbody)).build()).enqueue(new Callback() {
                @Override
                public void onFailure(final Call call, final IOException e) {
                    if (myCallBack != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                myCallBack.onFailure(call, e);
                            }
                        });
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String s = response.body().string();
                    if (myCallBack != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                myCallBack.onResponse(s);
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface MyCallBack {
        void onFailure(Call call, IOException e);

        void onResponse(String s);
    }
}
