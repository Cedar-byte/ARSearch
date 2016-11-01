package com.yuntong.arsearch.activity;


import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yuntong.arsearch.R;
import com.yuntong.arsearch.base.BaseActivity;
import com.yuntong.arsearch.util.TimeCount;

public class RegisterSecondActivity extends BaseActivity implements TextWatcher {

    private Context context = RegisterSecondActivity.this;
    private TextView tvShowMob;
    private EditText codeEdt;
    private Button sendCodeBtn, timeBtn;
    private TimeCount timeCount;
    private String mobile, mob1, mob2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_second;
    }

    @Override
    protected void initData() {
        mobile = getIntent().getStringExtra("mobile");
        mob1 = mobile.substring(0,3);
        mob2 = mobile.substring(7,11);
    }

    @Override
    protected void initView() {
        initToolbar("注册", R.color.color_transparent);
        tvShowMob = (TextView) findViewById(R.id.tv_show_mobile);
        codeEdt = (EditText) findViewById(R.id.edt_verifyCode);
        codeEdt.addTextChangedListener(this);
        sendCodeBtn = (Button) findViewById(R.id.btn_send_verifyCode);
        sendCodeBtn.setTextColor(getResources().getColor(R.color.gray_hint));
        sendCodeBtn.setOnClickListener(this);
        timeBtn = (Button) findViewById(R.id.btn_timecount);
        timeBtn.setOnClickListener(this);
        timeCount = new TimeCount(60000, 1000, timeBtn);
    }

    @Override
    protected void bindView() {
        timeCount.start();
        tvShowMob.setText(getResources().getString(R.string.verifyCode_to_number) + mob1 + "****" + mob2);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_verifyCode:
                showImgToast(context, "验证码超时！", R.drawable.icon_code_error);
                showImgToast(context, "验证码错误！", R.drawable.icon_code_error);
                startActivity(new Intent(context, RegisterThirdActivity.class));
                break;
            case R.id.btn_timecount:
                timeCount.start();
                getVerifyCode();
                break;
        }
    }

    /** 获取验证码 */
    private void getVerifyCode() {
    }

    /** ==================================此处应有牛逼的分界线==================================== */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if(codeEdt.getText().toString().length() == 6){
            sendCodeBtn.setEnabled(true);
            sendCodeBtn.setTextColor(getResources().getColor(R.color.color_white));
        }else{
            sendCodeBtn.setEnabled(false);
            sendCodeBtn.setTextColor(getResources().getColor(R.color.gray_hint));
        }
    }
}
