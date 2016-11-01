package com.yuntong.arsearch.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yuntong.arsearch.R;
import com.yuntong.arsearch.app.MyApplication;
import com.yuntong.arsearch.base.BaseActivity;
import com.yuntong.arsearch.util.Constants;
import com.yuntong.arsearch.util.JsonUtil;
import com.yuntong.arsearch.util.NormalPostRequest;
import com.yuntong.arsearch.util.SpfUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 启动页
 */
public class StartUpActivity extends BaseActivity{
    private RequestQueue requestQueue;
    private LocationClient mLocationClient = null;
    private MyLocationListenner myListener;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_startup;
    }

    @Override
    protected void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLocationClient = new LocationClient(getApplicationContext());
                myListener = new MyLocationListenner();
                mLocationClient.registerLocationListener(myListener);
                mLocationClient.start();
                LocationClientOption option = new LocationClientOption();
                option.setAddrType("all");
                mLocationClient.setLocOption(option);
                getLogin();
            }
        },2000);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {

    }

    @Override
    protected void widgetClick(View v) {

    }


    private void getLogin(){
        requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        Request<JSONObject> request = new NormalPostRequest(Constants.HISTORYDATA_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                String a=JsonUtil.getStr(object,"status");
               if (a.equals("1")){
                   startActivity(new Intent(StartUpActivity.this,HomeActivity.class));
                   finish();
               }else {
                   startActivity(new Intent(StartUpActivity.this,LoginActivity.class));
               }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                startActivity(new Intent(StartUpActivity.this,LoginActivity.class));
            }
        }, map);
        requestQueue.add(request);
    }


    class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            SpfUtil.put(StartUpActivity.this,"lng",location.getLongitude());
            SpfUtil.put(StartUpActivity.this, "lat", location.getLatitude());
            if (location.getLatitude() > 0 &&location.getLongitude() > 0) {
                // 停止定位
                if (mLocationClient != null) {
                    if (myListener != null) {
                        // 移除定位监听函数
                        mLocationClient.unRegisterLocationListener(myListener);
                        myListener = null;
                    }
                    if (mLocationClient.isStarted()) {
                        // 停止百度定位
                        mLocationClient.stop();
                    }
                    mLocationClient = null;
                }
            }

        }

        @Override
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
