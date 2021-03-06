/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: NetUtils.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.util
 * @Description: 网络Utils
 * @author: zhaoqy
 * @date: 2015-11-18 下午2:53:57
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils 
{
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;
	
	/**
	 * 检测网络是否可用
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) 
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取当前网络类型
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public static int getNetworkType(Context context) 
	{
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) 
		{
			return netType;
		}		
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) 
		{
			String extraInfo = networkInfo.getExtraInfo();
			if(!StringUtils.isEmpty(extraInfo))
			{
				if (extraInfo.toLowerCase().equals("cmnet")) 
				{
					netType = NETTYPE_CMNET;
				} 
				else 
				{
					netType = NETTYPE_CMWAP;
				}
			}
		} 
		else if (nType == ConnectivityManager.TYPE_WIFI) 
		{
			netType = NETTYPE_WIFI;
		}
		return netType;
	}
}
