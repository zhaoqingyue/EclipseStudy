package com.zhaoqy.app.demo.login.qq;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtils 
{
	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Context context) 
	{
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	@SuppressWarnings("deprecation")
	public static int getScreenHeight(Context context) 
	{
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	public static float getScreenDensity(Context context) 
	{
		try 
		{
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(dm);
			return dm.density;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 1.0f;
	}
}
