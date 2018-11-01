/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: AppInfo.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.common
 * @Description: app信息
 * @author: zhaoqy
 * @date: 2015-11-17 下午4:25:55
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.common;

import java.util.UUID;

import com.zhaoqy.app.demo.oschina.util.StringUtils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppInfo 
{
	/**
	 * 获取App安装包信息
	 * @return
	 */
	public static PackageInfo getPackageInfo(Context context) 
	{
		PackageInfo info = null;
		try 
		{ 
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} 
		catch (NameNotFoundException e) 
		{    
			e.printStackTrace(System.err);
		} 
		if(info == null) info = new PackageInfo();
		return info;
	}
	
	/**
	 * 获取App唯一标识
	 * @return
	 */
	public static String getAppId(Context context) 
	{
		String uniqueID = AppProperty.getProperty(context, AppConfig.CONF_APP_UNIQUEID);
		if(StringUtils.isEmpty(uniqueID))
		{
			uniqueID = UUID.randomUUID().toString();
			AppProperty.setProperty(context, AppConfig.CONF_APP_UNIQUEID, uniqueID);
		}
		return uniqueID;
	}
}
