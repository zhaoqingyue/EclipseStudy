package com.zhaoqy.app.demo.login.email.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil 
{
	public static boolean getIsRdPwd(Context context)
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = context.getSharedPreferences("sp", context.MODE_PRIVATE);
		return sp.getBoolean("isrdpwd", false);
	}
	
	public static void setIsRdPwd(Context context, boolean isrdpwd)
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = context.getSharedPreferences("sp", context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("isrdpwd", isrdpwd);
		editor.commit();
	}
	
	public static String getEmail(Context context)
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = context.getSharedPreferences("sp", context.MODE_PRIVATE);
		return sp.getString("email", "");
	}
	
	public static void setEmail(Context context, String email)
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = context.getSharedPreferences("sp", context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("email", email);
		editor.commit();
	}
	
	public static String getPwd(Context context)
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = context.getSharedPreferences("sp", context.MODE_PRIVATE);
		return sp.getString("pwd", "");
	}
	
	public static void setPwd(Context context, String pwd)
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = context.getSharedPreferences("sp", context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("pwd", pwd);
		editor.commit();
	}
}
