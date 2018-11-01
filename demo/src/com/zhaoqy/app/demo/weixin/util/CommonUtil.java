package com.zhaoqy.app.demo.weixin.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.zhaoqy.app.demo.R;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class CommonUtil 
{
	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) 
	{
		try 
		{
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return "";
		}
	}

	private static float sDensity = 0;

	/**
	 * DP转换为像素
	 * 
	 * @param context
	 * @param nDip
	 * @return
	 */
	public static int dipToPixel(Context context, int nDip) 
	{
		if (sDensity == 0) 
		{
			final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			sDensity = dm.density;
		}
		return (int) (sDensity * nDip);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static Date stringToDate(String str) 
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Date date = null;
		try 
		{
			//Fri Feb 24 00:00:00 CST 2012
			date = format.parse(str);
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 发送文字通知
	 * 
	 * @param context
	 * @param Msg
	 * @param Title
	 * @param content
	 * @param i
	 */
	@SuppressWarnings("deprecation")
	public static void sendText(Context context, String Msg, String Title, String content, Intent i) 
	{
		NotificationManager mn = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher, Msg, System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, Title, content, contentIntent);
		mn.notify(0, notification);
	}
	
	/**
	 * 检测网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) 
	{
		if (context != null) 
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) 
			{
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() 
	{
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	static String getStrng(Context context, int resId) 
	{
		return context.getResources().getString(resId);
	}

	public static String getTopActivity(Context context) 
	{
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}
}
