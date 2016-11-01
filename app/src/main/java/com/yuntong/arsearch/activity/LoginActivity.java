package com.yuntong.arsearch.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuntong.arsearch.R;
import com.yuntong.arsearch.app.MyApplication;
import com.yuntong.arsearch.base.BaseActivity;
import com.yuntong.arsearch.util.CommonUtil;
import com.yuntong.arsearch.util.Constants;
import com.yuntong.arsearch.util.JsonUtil;
import com.yuntong.arsearch.util.NormalPostRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements TextWatcher {

    private Context context = LoginActivity.this;
    private EditText mobileEdt, pwdEdt;
    private String mobile, pwd;
    private Button loginBtn, registerBtn;
    private TextView forgetTxt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        initToolbar("", R.color.color_transparent);
        mobileEdt = (EditText) findViewById(R.id.edt_phone_num);
        mobileEdt.addTextChangedListener(this);
        pwdEdt = (EditText) findViewById(R.id.edt_pwd);
        pwdEdt.addTextChangedListener(this);
        loginBtn = (Button) findViewById(R.id.btn_login);
        loginBtn.setTextColor(getResources().getColor(R.color.gray_hint));// 为Button中的字体设置颜色(在xml中设置颜色失效，我也不知道为什么，日了狗了)
        loginBtn.setOnClickListener(this);
        registerBtn = (Button) findViewById(R.id.btn_register);
        registerBtn.setOnClickListener(this);
        forgetTxt = (TextView) findViewById(R.id.tv_forget_pwd);
        forgetTxt.setOnClickListener(this);
    }

    @Override
    protected void bindView() {
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                mobile = mobileEdt.getText().toString();
                pwd = pwdEdt.getText().toString();
                if (mobileEdt.getText().toString().length() < 11 || !CommonUtil.isMobile(mobile)) {
                    showImgToast(context, "请输入合法手机号", R.drawable.icon_warning);
                    showImgToast(context, "账号或密码错误！", R.drawable.icon_warning);
                    showImgToast(context, "网络异常！", R.drawable.icon_not_net);
                    showImgToast(context, "账号不存在！", R.drawable.icon_null_account);
                    return;
                }
                if (pwdEdt.getText().toString().length() < 6) {
                    showImgToast(context, "请输入6-16位密码！", R.drawable.icon_pwd_prompt);
                    return;
                }
                doLogin();
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.tv_forget_pwd:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
        }
    }

    private void doLogin() {
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("name", mobile);
        map.put("password", pwd);
        map.put("deviceType","1");
        Request<JSONObject> request = new NormalPostRequest(Constants.LOGIN_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                if (JsonUtil.getStr(object,"status").equals("1")){
                    startActivity(new Intent(context, HomeActivity.class));

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showImgToast(context, "服务器异常！", R.drawable.icon_not_net);
            }
        }, map);
        requestQueue.add(request);
    }

    /** ==================================此处应有牛逼的分界线==================================== */

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mobileEdt.getText().toString().length() > 0 && pwdEdt.getText().toString().length() > 0) {
            loginBtn.setEnabled(true);
            loginBtn.setTextColor(getResources().getColor(R.color.color_white));// 为Button中的字体设置颜色
        } else {
            loginBtn.setEnabled(false);
            loginBtn.setTextColor(getResources().getColor(R.color.gray_hint));// 为Button中的字体设置颜色
        }
    }
}
