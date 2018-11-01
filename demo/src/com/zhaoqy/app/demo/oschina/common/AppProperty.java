/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: AppProperty.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.common
 * @Description: 应用属性
 * @author: zhaoqy
 * @date: 2015-11-18 上午11:42:06
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.common;

import java.util.Properties;
import android.content.Context;

public class AppProperty 
{
	public static boolean containsProperty(Context context, String key)
	{
		Properties props = getProperties(context);
		 return props.containsKey(key);
	}
	
	public static void setProperties(Context context, Properties ps)
	{
		AppConfig.getAppConfig(context).set(ps);
	}

	public static Properties getProperties(Context context)
	{
		return AppConfig.getAppConfig(context).get();
	}
	
	public static void setProperty(Context context, String key, String value)
	{
		AppConfig.getAppConfig(context).set(key, value);
	}
	
	public static String getProperty(Context context, String key)
	{
		return AppConfig.getAppConfig(context).get(key);
	}
	
	public static void removeProperty(Context context,String...key)
	{
		AppConfig.getAppConfig(context).remove(key);
	}	
}
