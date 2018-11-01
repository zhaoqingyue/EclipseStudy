package com.zhaoqy.app.demo.notification;

import android.app.Notification;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class NotificationProgressAcitivty extends BaseNotificationActivity implements OnClickListener
{
	private NotificationCompat.Builder mBuilder;
	private DownloadThread downloadThread;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mDefault;
	private TextView  mUnDefault;
	private TextView  mCustom;
	private TextView  mStart;
	private TextView  mPause;
	private TextView  mCancel;
	private int       notifyId = 102;
	private int       progress = 0;
	private boolean   isPause = false;
	private boolean   isCustom = false;
	private boolean   indeterminate = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_progress);
		
		initViews();
		setListener();
		initData();
		initNotify();
	}

	private void initViews() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		
		mDefault = (TextView) findViewById(R.id.id_notification_show_progress_default);
		mUnDefault = (TextView) findViewById(R.id.id_notification_show_progress_undefault);
		mCustom = (TextView) findViewById(R.id.id_notification_show_progress_custom);
		mStart = (TextView) findViewById(R.id.id_notification_show_progress_start);
		mPause = (TextView) findViewById(R.id.id_notification_show_progress_pause);
		mCancel = (TextView) findViewById(R.id.id_notification_show_progress_cancel);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mDefault.setOnClickListener(this);
		mUnDefault.setOnClickListener(this);
		mCustom.setOnClickListener(this);
		mStart.setOnClickListener(this);
		mPause.setOnClickListener(this);
		mCancel.setOnClickListener(this);
	}
	
	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.notification_show_progress));
	}

	private void initNotify() 
	{
		mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setWhen(System.currentTimeMillis())
				.setContentIntent(getDefalutIntent(0))
				.setNumber(1)
				.setPriority(Notification.PRIORITY_DEFAULT)
				.setOngoing(false)
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setSmallIcon(R.drawable.ic_launcher);
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
		case R.id.id_notification_show_progress_default:
		{
			downloadThread = null;
			isCustom = false;
			indeterminate = false;
			showProgressNotify();
			break;
		}
		case R.id.id_notification_show_progress_undefault:
		{
			downloadThread = null;
			isCustom = false;
			indeterminate = true;
			showProgressNotify();
			break;
		}
		case R.id.id_notification_show_progress_custom:
		{
			downloadThread = null;
			isCustom = true;
			indeterminate = false;
			showCustomProgressNotify("等待下载..");
			break;
		}
		case R.id.id_notification_show_progress_start:
		{
			startDownloadNotify();
			break;
		}
		case R.id.id_notification_show_progress_pause:
		{
			pauseDownloadNotify();
			break;
		}
		case R.id.id_notification_show_progress_cancel:
		{
			stopDownloadNotify();
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 显示带进度条通知栏
	 */
	public void showProgressNotify() 
	{
		mBuilder.setContentTitle("等待下载")
				.setContentText("进度:")
				.setTicker("开始下载");
		if(indeterminate)
		{
			mBuilder.setProgress(0, 0, true);
		}
		else
		{
			//这个方法是显示进度条  设置为true就是不确定的那种进度条效果
			mBuilder.setProgress(100, progress, false); 
		}
		mNotificationManager.notify(notifyId, mBuilder.build());
	}
	
	/**
	 * 显示自定义的带进度条通知栏
	 */
	private void showCustomProgressNotify(String status) 
	{
		RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.view_notification_custom_progress);
		mRemoteViews.setImageViewResource(R.id.custom_progress_icon, R.drawable.ic_launcher);
		mRemoteViews.setTextViewText(R.id.tv_custom_progress_title, "今日头条");
		mRemoteViews.setTextViewText(R.id.tv_custom_progress_status, status);
		if(progress >= 100 || downloadThread == null)
		{
			mRemoteViews.setProgressBar(R.id.custom_progressbar, 0, 0, false);
			mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.GONE);
		}
		else
		{
			mRemoteViews.setProgressBar(R.id.custom_progressbar, 100, progress, false);
			mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.VISIBLE);
		}
		mBuilder.setContent(mRemoteViews)
				.setContentIntent(getDefalutIntent(0))
				.setTicker("头条更新");
		Notification nitify = mBuilder.build();
		nitify.contentView = mRemoteViews;
		mNotificationManager.notify(notifyId, nitify);
	}
	
	/**
	 * 开始下载
	 */
	public void startDownloadNotify() 
	{
		isPause = false;
		if (downloadThread != null && downloadThread.isAlive()) 
		{
			//downloadThread.start();
		}
		else
		{
			downloadThread = new DownloadThread();
			downloadThread.start();
		}
	}

	/**
	 * 暂停下载
	 */
	public void pauseDownloadNotify() 
	{
		isPause = true;
		if(!isCustom)
		{
			mBuilder.setContentTitle("已暂停");
			setNotify(progress);
		}
		else
		{
			showCustomProgressNotify("已暂停");
		}
	}

	/**
	 * 取消下载
	 */
	public void stopDownloadNotify() 
	{
		if (downloadThread != null) 
		{
			downloadThread.interrupt();
		}
		downloadThread = null;
		if(!isCustom)
		{
			mBuilder.setContentTitle("下载已取消").setProgress(0, 0, false);
			mNotificationManager.notify(notifyId, mBuilder.build());
		}
		else
		{
			showCustomProgressNotify("下载已取消");
		}
	}

	/**
	 * 设置下载进度
	 */
	public void setNotify(int progress) 
	{
		mBuilder.setProgress(100, progress, false); 
		mNotificationManager.notify(notifyId, mBuilder.build());
	}

	/**
	 * 下载线程
	 */
	class DownloadThread extends Thread 
	{
		@Override
		public void run() 
		{
			int now_progress = 0;
			while (now_progress <= 100) 
			{
				if(downloadThread == null)
				{
					break;
				}
				if (!isPause) 
				{
					progress = now_progress;
					if(!isCustom)
					{
						mBuilder.setContentTitle("下载中");
						if(!indeterminate)
						{
							setNotify(progress);
						}
					}
					else
					{
						showCustomProgressNotify("下载中");
					}
					now_progress += 10;
				}
				try 
				{
					Thread.sleep(1 * 1000);
				} 
				catch (InterruptedException e) 
				{
				}
			}
			// When the loop is finished, updates the notification
			if(downloadThread != null)
			{
				if(!isCustom)
				{
					mBuilder.setContentText("下载完成").setProgress(0, 0, false);
					mNotificationManager.notify(notifyId, mBuilder.build());
				}
				else
				{
					showCustomProgressNotify("下载完成");
				}
			}
		}
	}
}
