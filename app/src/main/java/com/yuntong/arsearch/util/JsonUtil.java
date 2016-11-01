package com.yuntong.arsearch.util;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Title: JsonUtil.java<br>
 * Description: Json帮助类<br>
 * Copyright (c) 融创信息版权所有 2013	<br>
 * Create DateTime: 2013-6-7 下午5:31:55 <br>
 * @author ln
 */
public class JsonUtil {
	/**
	 * 在json对象中根据key找value
	 * @return value
	 */
	public static boolean isMobileNO(String mobiles) {

		String telRegex = "[1][358]\\d{9}";
		if (TextUtils.isEmpty(mobiles)) return false;
		else return mobiles.matches(telRegex);
	}
	public static String getStr(JSONObject jsonObj, String key) {
		try {
			return jsonObj.getString(key);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 在json对象中根据key找json对象
	 * @param jsonObj json对象
	 * @param key 键
	 * @return json对象
	 */
	public static JSONObject getJsonObj(JSONObject jsonObj, String key) {
		try {
			return (JSONObject) jsonObj.get(key);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 转JSON对象
	 *
	 * @param json2
	 * @return json对象
	 */
	public static JSONObject convertJsonObj(String json2) {
		try {
			return new JSONObject(json2);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 在json对象中根据key找json数组对象
	 * @param jsonObj json对象
	 * @param
	 * @return json数组对象
	 */
	public static JSONArray convertJsonArry(JSONObject jsonObj, String jsonStr) {
		try {
			return jsonObj.getJSONArray(jsonStr);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 在json数组对象中根据key找json对象
	 * @param jsonArry json数组对象  index 下标
	 * @param
	 * @return json对象
	 */
	public static JSONObject convertJsonObj(JSONArray jsonArry, int index) {
		try {
			return (JSONObject) jsonArry.opt(index);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * json对象put
	 */
	public static void putJson(JSONObject jsonObj, String jsonKey, Object jsonValue) {
		try {
			jsonObj.put(jsonKey, jsonValue);
		} catch (Exception e) {
		}
	}
	
	/**
	 * 在json数组对象中根据key找json对象
	 * @param jsonArry json数组对象  index 下标
	 * @param
	 * @return json对象
	 */
	public static JSONArray convertJsonArr2(JSONArray jsonArry, int index) {
		try {
			return (JSONArray) jsonArry.opt(index);
		} catch (Exception e) {
		}
		return null;
	}




	public static JSONObject toJson(List<String> list){
		String jsonStr="{"+"attachmentList"+":"+"[";
		String[] toBeStored = list.toArray(new String[list.size()]);

		for (int i = 0 ; i<list.size();i++){
			if (i!=list.size()-1){
				jsonStr=jsonStr+toBeStored[i]+",";

			}else {
				jsonStr=jsonStr+toBeStored[i]+"]"+"}";
				;
			}
		}
		try {
			return new JSONObject(jsonStr);
		} catch (JSONException e) {
		}
		return null;
	}
}
