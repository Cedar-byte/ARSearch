package com.yuntong.arsearch.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yuntong.arsearch.R;
import com.yuntong.arsearch.base.BaseActivity;

public class RegisterThirdActivity extends BaseActivity implements TextWatcher {

    private Context context = RegisterThirdActivity.this;
    private EditText pwdEdt;
    private Button completeBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_third;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        initToolbar("注册", R.color.color_transparent);
        pwdEdt = (EditText) findViewById(R.id.edt_pwd);
        pwdEdt.addTextChangedListener(this);
        completeBtn = (Button) findViewById(R.id.btn_complete_register);
        completeBtn.setTextColor(ContextCompat.getColor(context, R.color.gray_text));
        completeBtn.setOnClickListener(this);
    }

    @Override
    protected void bindView() {

    }

    @Override
    protected void widgetClick(View v) {
        if(pwdEdt.getText().toString().length() < 6){
            showImgToast(context, "请输入6-16位密码！", R.drawable.icon_pwd_prompt);
            return;
        }
        onRegister();
    }

    /** 完成注册 */
    private void onRegister() {
        startActivity(new Intent(context, HomeActivity.class));
    }

    /** ==================================此处应有牛逼的分界线==================================== */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if(pwdEdt.getText().toString().length() > 0){
            completeBtn.setEnabled(true);
            completeBtn.setTextColor(getResources().getColor(R.color.color_white));
        }else{
            completeBtn.setEnabled(false);
            completeBtn.setTextColor(getResources().getColor(R.color.gray_text));
        }
    }
}
