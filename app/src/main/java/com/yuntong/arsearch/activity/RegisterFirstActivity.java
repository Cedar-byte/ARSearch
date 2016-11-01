package com.yuntong.arsearch.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuntong.arsearch.R;
import com.yuntong.arsearch.base.BaseActivity;
import com.yuntong.arsearch.util.CommonUtil;

public class RegisterFirstActivity extends BaseActivity implements TextWatcher {

    private Context context = RegisterFirstActivity.this;
    private EditText mobileEdt;
    private LinearLayout lin;
    private ImageView iv;
    private TextView agreementTv, tv;
    private Button getCodeBtn;
    private boolean isSelect = true;
    private String mobile;
    private Intent i;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_first;
    }

    @Override
    protected void initData() {
        i = new Intent();
    }

    @Override
    protected void initView() {
        initToolbar("注册", R.color.color_transparent);
        mobileEdt = (EditText) findViewById(R.id.edt_phone_num);
        mobileEdt.addTextChangedListener(this);
        lin = (LinearLayout) findViewById(R.id.linear_img);
        lin.setOnClickListener(this);
        iv = (ImageView) findViewById(R.id.img_gou);
        iv.setOnClickListener(this);
        agreementTv = (TextView) findViewById(R.id.tv_user_agreement);
        agreementTv.setOnClickListener(this);
        tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(this);
        getCodeBtn = (Button) findViewById(R.id.btn_get_verifyCode);
        getCodeBtn.setTextColor(getResources().getColor(R.color.gray_hint));// 为Button中的字体设置颜色(在xml中设置颜色失效，我也不知道为什么，日了狗了)
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
                if (!CommonUtil.isMobile(mobile)) { // 如果当前输入的手机号不合规
                    showImgToast(context, "请输入合法手机号", R.drawable.icon_warning);
                    return;
                }
                if (!isSelect) { // 如果没有勾选同意用户协议
                    showNotAgreedDialog();
                    return;
                }
                getVerifyCode();
                break;
            case R.id.tv:
            case R.id.linear_img:
            case R.id.img_gou:// 本来这个id可以不用写进来的，但是只使用上面两个不好使，我也不知道为什么，鬼知道SDK经历了什么，日了狗了
                if (isSelect) {
                    iv.setVisibility(View.INVISIBLE);
                    isSelect = false;
                } else {
                    iv.setVisibility(View.VISIBLE);
                    isSelect = true;
                }
                break;
            case R.id.tv_user_agreement:
                i.setClass(context, AgreementActivity.class);
                startActivity(i);
                break;
        }
    }
    /** 获取验证码 */
    private void getVerifyCode() {
        i.setClass(context, RegisterSecondActivity.class);
        i.putExtra("mobile", mobile);
        startActivity(i);
    }

    /** 未同意用户协议 */
    private void showNotAgreedDialog() {
        final Dialog dialog = new Dialog(context, R.style.my_dialog_style);
        dialog.setCancelable(true);// 可以用“返回键”取消
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bg_not_agreed, null);
        Button confirm = (Button) view.findViewById(R.id.btn_confirm);
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        dialog.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /** 手机号已注册 */
    private void showRegisteredDialog(){
        final Dialog dialog = new Dialog(context, R.style.my_dialog_style);
        dialog.setCancelable(true);// 可以用“返回键”取消
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bg_registered, null);
        Button cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        Button loginBtn = (Button) view.findViewById(R.id.btn_login);
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        dialog.show();
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFirstActivity.this.finish();
            }
        });
    }

    /** ==================================此处应有牛逼的分界线==================================== */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if (mobileEdt.getText().toString().length() == 11) {
            getCodeBtn.setEnabled(true);
            getCodeBtn.setTextColor(ContextCompat.getColor(context, R.color.color_white));// 为Button中的字体设置颜色
        } else {
            getCodeBtn.setEnabled(false);
            getCodeBtn.setTextColor(ContextCompat.getColor(context, R.color.gray_hint));
        }
    }
}
