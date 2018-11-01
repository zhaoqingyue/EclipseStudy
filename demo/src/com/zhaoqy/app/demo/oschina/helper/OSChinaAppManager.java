/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaAppManager.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.manager
 * @Description: 应用程序Activity管理类：用于Activity管理和应用程序退出
 * @author: zhaoqy
 * @date: 2015-11-17 下午2:16:49
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.helper;

import java.util.Stack;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

public class OSChinaAppManager 
{
	private static Stack<Activity>   mActivityStack;
	private static OSChinaAppManager mInstance;
	
	private OSChinaAppManager()
	{
	}
	
	public static OSChinaAppManager getAppManager()
	{
		if(mInstance == null)
		{
			mInstance = new OSChinaAppManager();
		}
		return mInstance;
	}
	
	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity)
	{
		if(mActivityStack == null)
		{
			mActivityStack = new Stack<Activity>();
		}
		mActivityStack.add(activity);
	}
	
	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity()
	{
		Activity activity = mActivityStack.lastElement();
		return activity;
	}
	
	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity()
	{
		Activity activity = mActivityStack.lastElement();
		finishActivity(activity);
	}
	
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity)
	{
		if(activity!=null)
		{
			mActivityStack.remove(activity);
			activity.finish();
			activity=null;
		}
	}
	
	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls)
	{
		for (Activity activity : mActivityStack) 
		{
			if(activity.getClass().equals(cls) )
			{
				finishActivity(activity);
			}
		}
	}
	
	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity()
	{
		for (int i = 0, size = mActivityStack.size(); i < size; i++)
		{
            if (null != mActivityStack.get(i))
            {
            	mActivityStack.get(i).finish();
            }
	    }
		mActivityStack.clear();
	}
	
	/**
	 * 退出应用程序
	 */
	@SuppressWarnings("deprecation")
	public void AppExit(Context context) 
	{
		try 
		{
			finishAllActivity();
			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} 
		catch (Exception e) 
		{	
		}
	}
}
