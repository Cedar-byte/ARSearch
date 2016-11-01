package com.yuntong.arsearch.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuntong.arsearch.R;
import com.yuntong.arsearch.app.AppManager;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    Context c;// 上下文对象
    private boolean screenHorizontal;  //是否允许横屏
    protected Toast mToast;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏锁定
        if(screenHorizontal){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        AppManager.getInstance().addActivity(this);// 将本Activity添加到堆栈中
        c = this;
        int layoutId = getLayoutId();// 设置界面
        if (layoutId != -1) {
            setContentView(layoutId);
        }
        // 2.初始化数据
        initData();
        // 3.初始化控件（M）
        initView();
        // 4.为控件注册监听器(C)
        bindView();
        // 检查网络
//        if(!isNetConnected()){
//            showToast("网络连接异常，请检查网络");
//            return;
//        }
    }

//    protected boolean isNetConnected(){
//        if (c != null) {
//            ConnectivityManager mConnectivityManager = (ConnectivityManager) c
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
//            if (mNetworkInfo != null) {
//                return mNetworkInfo.isAvailable();
//            }
//        }
//        return false;
//    }

    /** 布局文件ID */
    protected abstract int getLayoutId();

    /** 定义初始化数据的抽象方法 */
    protected abstract void initData();

    /** 定义初始化界面的抽象方法 */
    protected abstract void initView();

    /** 定义数据和控件发生关系的抽象方法 */
    protected abstract void bindView();

    /** 设置是否允许横屏 */
    public void setScreenHorizontal(boolean screenHorizontal) {
        this.screenHorizontal = screenHorizontal;
    }

    /** 定义控件的点击事件 */
    protected abstract void widgetClick(View v);

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    /** ==================================此处应有牛逼的分界线==================================== */

    /** Toast */
    protected void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    protected void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    /** 图文Toast */
    protected void showImgToast(Context c, String s, int imgId){
        View view = getLayoutInflater().inflate(R.layout.toast_bg_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_toast);
        tv.setText(s);
        ImageView iv = (ImageView) view.findViewById(R.id.img_toast);
        iv.setImageResource(imgId);
        Toast toast = new Toast(c);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    /** Toolbar */
    protected void initToolbar(String s, int colorId){
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
        toolbar.setBackgroundColor(getResources().getColor(colorId));
        TextView tv = (TextView) findViewById(R.id.tv_title);
        tv.setText(s);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
