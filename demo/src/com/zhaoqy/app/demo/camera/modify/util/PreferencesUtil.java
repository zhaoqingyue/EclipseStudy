package com.zhaoqy.app.demo.camera.modify.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil 
{
	public static String PREFERENCE_NAME = "sp";
	
	private PreferencesUtil()
	{
	}

	public static boolean putString(Context context, String key, String value) 
	{
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public static String getString(Context context, String key, String defaultValue) 
    {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getString(key, defaultValue);
	}
}
