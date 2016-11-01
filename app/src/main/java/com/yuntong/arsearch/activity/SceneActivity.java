package com.yuntong.arsearch.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuntong.arsearch.R;
import com.yuntong.arsearch.app.MyApplication;
import com.yuntong.arsearch.base.BaseActivity;
import com.yuntong.arsearch.util.Constants;
import com.yuntong.arsearch.util.JsonUtil;
import com.yuntong.arsearch.util.NormalPostRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SceneActivity extends BaseActivity {

    private FrameLayout frameLayout;
    private WebView webView;
    private WebSettings webSettings;
    private String sceneId, panoId, id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scene;
    }

    @Override
    protected void initData() {
        webView = new WebView(getApplicationContext());
        webSettings = webView.getSettings();
        id = getIntent().getExtras().getString(Constants.EXTRAS_KEY_POI_ID);
        if (id != null) {
            sceneId = id.split("/")[0];
            panoId = id.split("/")[1];
        }
    }

    @Override
    protected void initView() {
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.addView(webView);
    }

    @Override
    protected void bindView() {
        setHistory();// 浏览记录
        onWebSet();
        initJs();
//        tv2.setText(getIntent().getExtras().getString(Constants.EXTRAS_KEY_POI_TITILE));
//        tv3.setText(getIntent().getExtras().getString(Constants.EXTRAS_KEY_POI_DESCR ));
    }

    @Override
    protected void widgetClick(View v) {

    }

    private void setHistory() {
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("sceneId", sceneId);
        Request<JSONObject> request = new NormalPostRequest(Constants.SETHISTORY_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                if (JsonUtil.getStr(object, "status").equals("1")){
                    Log.i("666", "onResponse: " + "成功添加");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showImgToast(SceneActivity.this, "服务器异常！", R.drawable.icon_not_net);
            }
        }, map);
        requestQueue.add(request);
    }

    private void onWebSet() {
        webSettings.setJavaScriptEnabled(true); //设置WebView属性，能够执行Javascript脚本
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setAllowFileAccess(true);//设置可以访问文件
        webSettings.setBuiltInZoomControls(true);//设置支持缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        webSettings.setDomStorageEnabled(true);// 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);//开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
        webSettings.setUseWideViewPort(true);
        loadWeb();
    }

    private void loadWeb() {
        webView.loadUrl("http://t.panocooker.com/pano/worksView?id=" + panoId); //加载需要显示的网页
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//            }
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                if(webView != null){
//                    webView.loadUrl("javascript:Pano.isWeb=false"); //加载需要显示的网页
//                    webView.loadUrl("javascript:Pano.createPano" + "(" + panoId + ")");
//                }
////                dialog.dismiss();
//            }
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
//            }
//        });
    }

    private void initJs() {
        JSDebug jsDebug = new JSDebug();
        webView.addJavascriptInterface(jsDebug, "jsforDebug");
    }

    /** 与js交互调试用 */
    public class JSDebug {

        public JSDebug() {}

        @JavascriptInterface
        public void showToast(String s) {
            showToast(s);
        }
    }
}
