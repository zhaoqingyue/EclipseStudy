package com.zhaoqy.app.demo.notification;

import java.io.File;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.about.AboutActivity;

public class NotificationActivity extends BaseNotificationActivity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mShow;
	private TextView  mShowAlway;
	private TextView  mShowIntentAct;
	private TextView  mShowIntentApk;
	private TextView  mClearOne;
	private TextView  mClearAll;
	private TextView  mShowCustom;
	private TextView  mShowProgress;
	private NotificationCompat.Builder mBuilder;
	private int notifyId = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		mContext = this;
		
		initView();
		setListener();
		initData();
		initNotify();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mShow = (TextView) findViewById(R.id.id_notification_show);
		mShowAlway = (TextView) findViewById(R.id.id_notification_show_alway);
		mShowIntentAct = (TextView)findViewById(R.id.id_notification_show_intent_action);
		mShowIntentApk = (TextView)findViewById(R.id.id_notification_show_intent_apk);
		mClearOne = (TextView) findViewById(R.id.id_notification_show_clear_one);
		mClearAll = (TextView) findViewById(R.id.id_notification_show_clear_all);
		mShowCustom = (TextView) findViewById(R.id.id_notification_show_custom);
		mShowProgress = (TextView) findViewById(R.id.id_notification_show_progress);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mShow.setOnClickListener(this);
		mShowAlway.setOnClickListener(this);
		mShowIntentAct.setOnClickListener(this);
		mClearOne.setOnClickListener(this);
		mClearAll.setOnClickListener(this);
		mShowIntentApk.setOnClickListener(this);
		mShowCustom.setOnClickListener(this);
		mShowProgress.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.main_item7));
	}
	
	private void initNotify()
	{
		mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle("测试标题")
				.setContentText("测试内容")
				.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
				.setNumber(1)                              //显示数量
				.setTicker("测试通知来啦")                   //通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())       //通知产生的时间，会在通知信息里显示
				.setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
				//.setAutoCancel(true)                     //设置这个标志当用户单击面板就可以让通知将自动取消  
				.setOngoing(false)                         //ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_VIBRATE) //向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
				.setSmallIcon(R.drawable.ic_launcher);
	}
	
	/**
	 * 显示通知栏
	 */
	public void showNotify()
	{
		mBuilder.setContentTitle("通知栏")
				.setContentText("测试内容")
				.setNumber(1)
				.setTicker("通知栏来啦"); 
		mNotificationManager.notify(notifyId, mBuilder.build());
	}
	
	/**
	 * 显示常驻通知栏
	 */
	public void showAlwayNotify()
	{
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, getIntent(), 0);  
		mBuilder.setSmallIcon(R.drawable.ic_launcher)
				.setTicker("常驻通知栏来了")
				.setContentTitle("常驻通知栏")
				.setContentText("使用cancel()方法才可以把我去掉哦")
				.setContentIntent(pendingIntent);
		Notification mNotification = mBuilder.build();
		mNotification.icon = R.drawable.ic_launcher;
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;
		mNotification.defaults = Notification.DEFAULT_VIBRATE;
		mNotification.when = System.currentTimeMillis(); 
		mNotificationManager.notify(notifyId, mNotification);
	}
	
	/**
	 * 显示通知栏点击跳转到指定Activity
	 */
	public void showIntentActivityNotify()
	{
		mBuilder.setAutoCancel(true)
				.setContentTitle("点击跳转到指定Activity")
				.setContentText("点击跳转")
				.setTicker("快来点我啊");
		Intent resultIntent = new Intent(this, AboutActivity.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(pendingIntent);
		mNotificationManager.notify(notifyId, mBuilder.build());
	}
	
	/**
	 * 显示通知栏点击打开Apk
	 */
	public void showIntentApkNotify()
	{
		mBuilder.setAutoCancel(true)
				.setContentTitle("下载完成")
				.setContentText("点击安装")
				.setTicker("下载完成！");
		Intent apkIntent = new Intent();
		apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		apkIntent.setAction(android.content.Intent.ACTION_VIEW);
		//注意：这里的这个APK是放在assets文件夹下，获取路径不能直接读取的，要通过COYP出去在读或者直接读取自己本地的PATH，这边只是做一个跳转APK，实际打不开的
		String apk_path = "file:///android_asset/Ktv.apk";
		Uri uri = Uri.fromFile(new File(apk_path));
		apkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
		PendingIntent contextIntent = PendingIntent.getActivity(this, 0,apkIntent, 0);
		mBuilder.setContentIntent(contextIntent);
		mNotificationManager.notify(notifyId, mBuilder.build());
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		case R.id.id_notification_show:
		{
			showNotify();
			break;
		}
		case R.id.id_notification_show_alway:
		{
			showAlwayNotify();
			break;
		}
		case R.id.id_notification_show_intent_action:
		{
			showIntentActivityNotify();
			break;
		}
		case R.id.id_notification_show_intent_apk:
		{
			showIntentApkNotify();
			break;
		}
		case R.id.id_notification_show_clear_one:
		{
			clearNotify(notifyId);
			break;
		}
		case R.id.id_notification_show_clear_all:
		{
			clearAllNotify();
			break;
		}
		case R.id.id_notification_show_custom:
		{
			Intent intent = new Intent(mContext, NotificationCustomAcitivty.class);
			startActivity(intent);
			break;
		}
		case R.id.id_notification_show_progress:
		{
			Intent intent = new Intent(mContext, NotificationProgressAcitivty.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
