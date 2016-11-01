package com.yuntong.arsearch.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuntong.arsearch.R;
import com.yuntong.arsearch.base.BaseActivity;
import com.yuntong.arsearch.util.DataCleanManager;

import java.io.File;

/**
 * 设置
 */
public class SettingUpActivity extends BaseActivity{

    private Context context = SettingUpActivity.this;
    private RelativeLayout rel_modify, rel_clear, rel_logout;
    private TextView cacheTxt;
    private long num, num_;// 缓存大小

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settingup;
    }

    @Override
    protected void initData() {
        try {
            num = DataCleanManager.getFolderSize(new File("/storage/emulated/0/com.yuntong.arsearch/cache"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        rel_modify = (RelativeLayout) findViewById(R.id.rel_modify_pwd);
        rel_modify.setOnClickListener(this);
        rel_clear = (RelativeLayout) findViewById(R.id.rel_clear_cache);
        rel_clear.setOnClickListener(this);
        rel_logout = (RelativeLayout) findViewById(R.id.rel_logout);
        rel_logout.setOnClickListener(this);
        cacheTxt = (TextView) findViewById(R.id.tv_cache_num);
    }

    @Override
    protected void bindView() {
        initToolbar("设置",R.color.color_green);
        cacheTxt.setText(DataCleanManager.getFormatSize(num));
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.rel_modify_pwd:
                startActivity(new Intent(this, ModifyPwdActivity.class));
                break;
            case R.id.rel_clear_cache:
                clearCache();
                break;
            case R.id.rel_logout:
                showExitDialog();
                break;
            default:
                break;
        }
    }

    private void onLogout() {

    }

    private void showExitDialog() {
        final Dialog dialog = new Dialog(context, R.style.my_dialog_style);
        dialog.setCancelable(true);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bg_logout, null);
        Button logoutBtn = (Button) view.findViewById(R.id.btn_logout);
        Button cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        dialog.show();
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogout();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void clearCache() {
        DataCleanManager.cleanInternalCache(this);
        DataCleanManager.cleanCustomCache("/storage/emulated/0/com.yuntong.arsearch/cache");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    num_ = DataCleanManager.getFolderSize(new File("/storage/emulated/0/com.yuntong.arsearch/cache"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String s = DataCleanManager.getFormatSize(num_);
                cacheTxt.setText(s);
            }
        });
        showImgToast(SettingUpActivity.this, getResources().getString(R.string.clear_cache_success), R.drawable.icon_clear);
    }
}
