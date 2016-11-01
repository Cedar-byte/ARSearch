package com.yuntong.arsearch.app;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


public class AppManager {
	
	private static AppManager instance;//单例
		
	private Stack<Activity> allActivities;// 1.堆栈：用于保存所有的Activity
	
	private static Integer loc = 1;//锁

	private static Map<String,Activity> destoryMap = new HashMap<>();// 保存需要指定finish掉的activity

	/** 私有化本类 */
	private AppManager(){
		allActivities = new Stack<>();
	}
	
	/** 本类的唯一入口 */
	public static AppManager getInstance(){
		if(instance == null){
			synchronized (loc) {
				return instance != null ? instance : (instance = new AppManager());
			}
		}
		return instance;
	}
	
	/** 将Activity保存到堆栈中 */
	public void addActivity(Activity activity){
		allActivities.add(activity);
	}
	
	/** 干掉所有的Activity */
	public void finishAllActivity(){
		for (int i = 0, size = allActivities.size(); i < size; i++) {
			if (null != allActivities.get(i)) {
				allActivities.get(i).finish();
			}
		}
		allActivities.clear();
	}
	
	/** 程序退出 */
	public void appExit(Context context) {
		try {
			// 1.退出所有的Activity
			finishAllActivity();
			// 2.重启包    Unable to instantiate application com.yuntong.here.app.MyApplication:
			ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
			manager.restartPackage(context.getPackageName());
			// 3.系统退出
			System.exit(0);
		} catch (Exception e) {
		}
	}

	/**
	 * 添加到销毁队列
	 *
	 * @param activity 要销毁的activity
	 */

	public void addDestoryActMap(Activity activity, String activityName) {
		destoryMap.put(activityName,activity);
	}

	/** 销毁指定Activity */
	public void destoryActivity(String activityName) {
		Set<String> keySet=destoryMap.keySet();
		for (String key:keySet){
			destoryMap.get(key).finish();
		}
	}
}
