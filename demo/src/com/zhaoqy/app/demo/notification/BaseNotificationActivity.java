package com.zhaoqy.app.demo.notification;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

public class BaseNotificationActivity extends Activity 
{
	public NotificationManager mNotificationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		initService();
	}

	/**
	 * 初始化要用到的系统服务
	 */
	private void initService() 
	{
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}
	
	/** 
	 * 清除当前创建的通知栏 
	 */
	public void clearNotify(int notifyId)
	{
		mNotificationManager.cancel(notifyId);
	}
	
	/**
	 * 清除所有通知栏
	 * */
	public void clearAllNotify() 
	{
		mNotificationManager.cancelAll();
	}
	
	/**
	 * @获取默认的pendingIntent,为了防止2.3及以下版本报错
	 * @flags属性:  
	 * 在顶部常驻:Notification.FLAG_ONGOING_EVENT  
	 * 点击去除： Notification.FLAG_AUTO_CANCEL 
	 */
	public PendingIntent getDefalutIntent(int flags)
	{
		PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);
		return pendingIntent;
	}
}
