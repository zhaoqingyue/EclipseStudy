package com.zhaoqy.app.demo.register;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckUtil 
{
	/**
	 * 判断是否有网络
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) 
	{
		if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) 
		{
			return false;
		} 
		else 
		{
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity == null) 
			{
				Log.w("Utility", "couldn't get connectivity manager");
			} 
			else 
			{
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) 
				{
					for (int i = 0; i < info.length; i++) 
					{
						if (info[i].isAvailable()) 
						{
							Log.d("Utility", "network is available");
							return true;
						}
					}
				}
			}
		}
		Log.d("Utility", "network is not available");
		return false;
	}
	
	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) 
	{
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * 验证手机号
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) 
	{
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 验证是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) 
	{
		Pattern pattern = Pattern.compile("[0-9]*");
		java.util.regex.Matcher match = pattern.matcher(str);
		if (match.matches() == false) 
		{
			return false;
		} 
		else 
		{
			return true;
		}
	}
}
