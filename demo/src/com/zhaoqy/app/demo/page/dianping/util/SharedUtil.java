/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SharedUtil.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.dianping.util
 * @Description: 共享变量
 * @author: zhaoqy
 * @date: 2015-12-16 下午3:09:18
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.dianping.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class SharedUtil 
{
	private static final String FILE_NAME = "dazhongdianping";
	private static final String MODE_NAME = "welcome";

	public static boolean isFirstStart(Context context)
	{
		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getBoolean(MODE_NAME, true);
	}
	
	public static void putIsFirstStart(Context context,boolean isFirst)
	{
		Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
		editor.putBoolean(MODE_NAME, isFirst);
		editor.commit();
	}
	
	public static void putCityName(Context context,String cityName)
	{
		Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
		editor.putString("cityName", cityName);
		editor.commit();
	}
	
	public static String getCityName(Context context)
	{	
		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString("cityName", "选择城市");	
	}
}
