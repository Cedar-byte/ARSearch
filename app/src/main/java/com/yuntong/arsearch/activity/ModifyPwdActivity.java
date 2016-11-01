package com.yuntong.arsearch.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuntong.arsearch.R;
import com.yuntong.arsearch.app.MyApplication;
import com.yuntong.arsearch.base.BaseActivity;
import com.yuntong.arsearch.util.Constants;
import com.yuntong.arsearch.util.NormalPostRequest;
import com.yuntong.arsearch.util.SpfUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ModifyPwdActivity extends BaseActivity {

    private EditText oldEdt, newEdt, confirmEdt;
    private Button confirmBtn;
    private String userPwd, oldPwd, newPwd, confirmPwd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_pwd;
    }

    @Override
    protected void initData() {
        userPwd = (String) SpfUtil.get(ModifyPwdActivity.this, "password", "");
    }

    @Override
    protected void initView() {
        oldEdt = (EditText) findViewById(R.id.edt_old_pwd);
        newEdt = (EditText) findViewById(R.id.edt_new_pwd);
        confirmEdt = (EditText) findViewById(R.id.edt_confirm_pwd);
        confirmBtn = (Button) findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(this);
    }

    @Override
    protected void bindView() {
        initToolbar("修改密码", R.color.color_green);
    }

    @Override
    protected void widgetClick(View v) {
        oldPwd = oldEdt.getText().toString();
        newPwd = newEdt.getText().toString();
        confirmPwd = confirmEdt.getText().toString();
        if (!oldPwd.equals(userPwd)) {
            showImgToast(ModifyPwdActivity.this, "密码错误", R.drawable.icon_warning);
            return;
        }
        if(!confirmPwd.equals(newPwd)){
            showImgToast(ModifyPwdActivity.this, getResources().getString(R.string.different_pwd), R.drawable.icon_not_equal);
            return;
        }
        if(oldEdt.getText().toString().length() < 6
                || newEdt.getText().toString().length() < 6
                || confirmEdt.getText().toString().length() < 6){
            showImgToast(ModifyPwdActivity.this, "请输入6-16位密码！", R.drawable.icon_not_equal);
            return;
        }
        onModify();
    }

    private void onModify() {
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("oldPwd",oldPwd);
        map.put("newPwd",newPwd);
        Request<JSONObject> request = new NormalPostRequest(Constants.LOGIN_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, map);
        requestQueue.add(request);
    }
}
