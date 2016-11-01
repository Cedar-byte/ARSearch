package com.yuntong.arsearch.util;

/**
 * 存放应用中的常量
 */
public class Constants {

    public static final String EXTRAS_KEY_POI_ID = "id";
    public static final String EXTRAS_KEY_POI_TITILE = "title";
    public static final String EXTRAS_KEY_POI_DESCR = "description";

    public static final String BASE_URL = "http://172.16.180.95:8080/yt-c-web/";

    public static final String SCENELIST_URL = BASE_URL + "scene/index";// 主页
    public static final String LOGIN_URL = BASE_URL + "login/doLogin";// 登录接口
    public static final String HISTORYDATA_URL = BASE_URL + "browseController/queryByPageList";// 浏览记录
    public static final String SETHISTORY_URL = BASE_URL + "scene/browse";// 增加浏览记录
}
