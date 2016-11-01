package com.yuntong.arsearch.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.yuntong.arsearch.R;


/**
 * 按钮计时
 */
public class TimeCount extends CountDownTimer {
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private TextView view;

    public TimeCount(long millisInFuture, long countDownInterval, TextView view) {
        super(millisInFuture, countDownInterval);
        this.view=view;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTick(long millisUntilFinished) {
        view.setClickable(false);//防止重复点击
        view.setBackground(null);
        view.setTextColor(0xFF43BB96);// 设置颜色
        view.setText(millisUntilFinished / 1000 +"s");
    }

    @Override
    public void onFinish() {
        view.setText("获取验证码");
        view.setClickable(true);
        view.setBackgroundResource(R.drawable.selector_btn_bg_green);
        view.setTextColor(0xFFFFFFFF);// 设置颜色
    }
}
