package com.yuntong.arsearch.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast工具栏
 */
public class ToastUtil {

    public static void showToast(Context c,String content){
        Toast toast = Toast.makeText(c, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);//
        toast.show();
    }
}
