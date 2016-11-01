package com.yuntong.arsearch.activity;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.yuntong.arsearch.R;
import com.yuntong.arsearch.app.MyApplication;
import com.yuntong.arsearch.base.BaseActivity;
import com.yuntong.arsearch.util.CommonUtil;
import com.yuntong.arsearch.util.TimeCount;

/**
 * 忘记密码
 */
public class ForgetPwdActivity extends BaseActivity implements TextWatcher {

    private Context context = ForgetPwdActivity.this;
    private EditText mobileEdt, verifyCodeEdt, newPwdEdt;
    private Button commitBtn, getCodeBtn;
    private TimeCount timeCount;
    private String mobile, verifyCode, newPwd;
    private RequestQueue requestQueue;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initData() {
//        requestQueue = MyApplication.getInstance().requestQueue;
    }

    @Override
    protected void initView() {
        initToolbar("重置密码", R.color.color_transparent);
        mobileEdt = (EditText) findViewById(R.id.edt_phone_num);
        mobileEdt.addTextChangedListener(this);
        verifyCodeEdt = (EditText) findViewById(R.id.edt_verifyCode);
        verifyCodeEdt.addTextChangedListener(this);
        newPwdEdt = (EditText) findViewById(R.id.edt_new_pwd);
        newPwdEdt.addTextChangedListener(this);
        commitBtn = (Button) findViewById(R.id.btn_commit);
        commitBtn.setTextColor(getResources().getColor(R.color.gray_hint));// 为Button中的字体设置颜色(在xml中设置颜色失效，我也不知道为什么，日了狗了)
        commitBtn.setOnClickListener(this);
        getCodeBtn = (Button) findViewById(R.id.btn_get_verifyCode);
        getCodeBtn.setOnClickListener(this);
    }

    @Override
    protected void bindView() {
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_verifyCode:
                mobile = mobileEdt.getText().toString();
                if(mobileEdt.getText().toString().length() < 11 || !CommonUtil.isMobile(mobile)){
                    showImgToast(context, "请输入11位手机号！", R.drawable.icon_eleven_phone_num);
                    showImgToast(context, "账号不存在！", R.drawable.icon_null_account);
                    showImgToast(context, "验证码错误！", R.drawable.icon_code_error);
                    return;
                }
                getVerifyCode(mobile);
                break;
            case R.id.btn_commit:
                mobile = mobileEdt.getText().toString();
                verifyCode = verifyCodeEdt.getText().toString();
                newPwd = newPwdEdt.getText().toString();
                // 一级判断，手机号小于11个数
                if(mobileEdt.getText().toString().length() < 11 || !CommonUtil.isMobile(mobile)){
                    showImgToast(context, "请输入合法手机号", R.drawable.icon_warning);
                    return;
                }
                // 二级判断，验证码小于6位数
                if(verifyCodeEdt.getText().toString().length() < 6){
                    showImgToast(context, "验证码错误！", R.drawable.icon_code_error);
                    return;
                }
                // 三级判断，输入的密码不符合规则
                if(newPwdEdt.getText().toString().length() < 6){
                    showImgToast(context, "请输入6-16位密码！", R.drawable.icon_pwd_prompt);
                    return;
                }
                onCommit();
                break;
        }
    }

    /** 获取验证码 */
    private void getVerifyCode(String mobile) {
        // 计时
        timeCount = new TimeCount(30000, 1000, getCodeBtn);
        timeCount.start();
        // 请求接口

    }

    /** 提交（重置密码） */
    private void onCommit() {
        // 请求接口
    }

    /** ==================================此处应有牛逼的分界线==================================== */

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if(mobileEdt.getText().toString().length()>0 && verifyCodeEdt.getText().toString().length()>0 && newPwdEdt.getText().length() > 0){
            commitBtn.setEnabled(true);
            commitBtn.setTextColor(getResources().getColor(R.color.color_white));// 为Button中的字体设置颜色
        }else{
            commitBtn.setEnabled(false);
            commitBtn.setTextColor(getResources().getColor(R.color.gray_hint));// 为Button中的字体设置颜色
        }
    }
}
