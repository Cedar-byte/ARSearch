package com.yuntong.arsearch.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间转换工具类
 */
public class TimeUtil {

    //时间戳---年月日
    public static String getTimetoYMD(long l){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String date = sdf.format(new Date(l));
        return date;
    }
    //时间戳---时分
    public static String getTimetotime(long l){
        SimpleDateFormat sdf = new SimpleDateFormat("hh.mm");
        String date = sdf.format(new Date(l));
        return date;
    }
    //时间戳---年月日时分
    public static String getTimeto(long l){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh.mm");
        String date = sdf.format(new Date(l));
        return date;
    }

    //时间戳---年
    public static String getTimetoY(long l){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String date = sdf.format(new Date(l));
        return date;
    }

    //时间戳---月
    public static String getTimetoM(long l){
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        String date = sdf.format(new Date(l));
        return date;
    }

    //时间戳---日
    public static String getTimetoD(long l){
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String date = sdf.format(new Date(l));
        return date;
    }

    //获取系统的年份
    public static int getSysYear(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        return year;
    }

}
