package com.yuntong.arsearch.app;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.PersistentCookieStore;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 应用入口
 * 注意：在Application类中一定不能出现"private"，否则在应用覆盖安装时程序会崩溃，报unable to instantiate application异常
 */
public class MyApplication extends Application {

    /** 单例对象 */
    public static MyApplication instance;

    /** 应用实例对象 */
    public static MyApplication myApplication;

    /** 获取app对象 */
    public static MyApplication getInstance() {
        return instance;
    }

//    /**
//     * 请求队列
//     */
    public RequestQueue requestQueue;

    public DisplayMetrics displayMetrics;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        instance = this;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        CookieStore cookieStore = new PersistentCookieStore(this);
        httpclient.setCookieStore(cookieStore);
        HttpStack httpStack = new HttpClientStack(httpclient);
        this.requestQueue = Volley.newRequestQueue(this, httpStack);
    }

    /**
     * @Description 获取屏幕宽高
     * @param isGetWidth 判断是获取宽还是高
     */
    public int getScreenWidthOrHeight(Context content, Boolean isGetWidth){
        if(displayMetrics==null){
            displayMetrics = content.getApplicationContext().getResources().getDisplayMetrics(); // 用于获取手机屏幕的宽高
        }
        return isGetWidth?displayMetrics.widthPixels:displayMetrics.heightPixels;
    }
}
