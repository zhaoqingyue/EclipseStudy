package com.zhaoqy.app.demo.notification;

import java.io.IOException;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;

public class NotificationCustomAcitivty  extends BaseNotificationActivity implements OnClickListener
{
	public final static String ACTION_BUTTON = "com.notifications.intent.action.ButtonClick";
	private NotificationCompat.Builder mBuilder;
	public  ButtonBroadcastReceiver    mReceiver;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mCustom;
	private TextView  mCustomBtn;
	public boolean    mIsPlay = false;
	private int       mNotifyId = 101;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_custom);
		
		initView();
		setListener();
		initData();
		initButtonReceiver();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mCustom = (TextView) findViewById(R.id.id_notification_show_custom_notification);
		mCustomBtn = (TextView) findViewById(R.id.id_notification_show_custom_notification_btn);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mCustom.setOnClickListener(this);
		mCustomBtn.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.notification_show_custom));
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
		case R.id.id_notification_show_custom_notification:
		{
			shwoNotify();
			break;
		}
		case R.id.id_notification_show_custom_notification_btn:
		{
			showButtonNotify();
			break;
		}
		default:
			break;
		}
	}

	public void shwoNotify() 
	{
		//先设定RemoteViews
		RemoteViews view_custom = new RemoteViews(getPackageName(), R.layout.view_notification_custom);
		//设置对应IMAGEVIEW的ID的资源图片
		view_custom.setImageViewResource(R.id.custom_icon, R.drawable.ic_launcher);
		view_custom.setTextViewText(R.id.tv_custom_title, "今日头条");
		view_custom.setTextViewText(R.id.tv_custom_content, "金州勇士官方宣布球队已经解雇了主帅马克-杰克逊，随后宣布了最后的结果。");
		mBuilder = new Builder(this);
		mBuilder.setContent(view_custom)
				.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
				.setWhen(System.currentTimeMillis())
				.setTicker("有新资讯").setPriority(Notification.PRIORITY_DEFAULT)
				.setOngoing(false)
				.setSmallIcon(R.drawable.ic_launcher);
		Notification notify = mBuilder.build();
		notify.contentView = view_custom;
		mNotificationManager.notify(mNotifyId, notify);
	}

	/**
	 * 带按钮的通知栏
	 */
	public void showButtonNotify() 
	{
		NotificationCompat.Builder mBuilder = new Builder(this);
		RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.view_notification_custom_button);
		mRemoteViews.setImageViewResource(R.id.custom_song_icon, R.drawable.menu_qq_icon_profile);
		mRemoteViews.setTextViewText(R.id.tv_custom_song_singer, "周杰伦");
		mRemoteViews.setTextViewText(R.id.tv_custom_song_name, "七里香");
		// 如果版本号低于（3。0），那么不显示按钮
		if (BaseTools.getSystemVersion() <= 9) 
		{
			mRemoteViews.setViewVisibility(R.id.ll_custom_button, View.GONE);
		} 
		else 
		{
			mRemoteViews.setViewVisibility(R.id.ll_custom_button, View.VISIBLE);
			if (mIsPlay) 
			{
				mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.drawable.notification_custom_btn_pause);
			} 
			else 
			{
				mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.drawable.notification_custom_btn_play);
			}
		}

		Intent buttonIntent = new Intent(ACTION_BUTTON);
		buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PREV_ID);
		PendingIntent intent_prev = PendingIntent.getBroadcast(this, 1, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_prev, intent_prev);
		buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);
		PendingIntent intent_paly = PendingIntent.getBroadcast(this, 2, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_play, intent_paly);
		buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_NEXT_ID);
		PendingIntent intent_next = PendingIntent.getBroadcast(this, 3, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_next, intent_next);

		mBuilder.setContent(mRemoteViews)
				.setContentIntent(getDefalutIntent(Notification.FLAG_ONGOING_EVENT))
				.setWhen(System.currentTimeMillis())
				.setTicker("正在播放").setPriority(Notification.PRIORITY_DEFAULT)
				.setOngoing(true)
				.setSmallIcon(R.drawable.menu_qq_icon_profile);
		Notification notify = mBuilder.build();
		notify.flags = Notification.FLAG_ONGOING_EVENT;
		mNotificationManager.notify(200, notify);
	}

	/** 带按钮的通知栏点击广播接收 */
	public void initButtonReceiver() 
	{
		mReceiver = new ButtonBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_BUTTON);
		registerReceiver(mReceiver, intentFilter);
	}

	public final static String INTENT_BUTTONID_TAG = "ButtonId";
	public final static int BUTTON_PREV_ID = 1;
	public final static int BUTTON_PALY_ID = 2;
	public final static int BUTTON_NEXT_ID = 3;

	/**
	 * 广播监听按钮点击时间
	 */
	public class ButtonBroadcastReceiver extends BroadcastReceiver 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			String action = intent.getAction();
			if (action.equals(ACTION_BUTTON)) 
			{
				int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
				switch (buttonId) 
				{
				case BUTTON_PREV_ID:
				{
					Toast.makeText(getApplicationContext(), "上一首", Toast.LENGTH_SHORT).show();
					break;
				}
				case BUTTON_PALY_ID:
				{
					String play_status = "";
					mIsPlay = !mIsPlay;
					if (mIsPlay) 
					{
						play_status = "开始播放";
						playBackgroundMusic();
					}
					else 
					{
						play_status = "已暂停";
						stopBackgroundMusic();
					}
					showButtonNotify();
					Toast.makeText(getApplicationContext(), play_status, Toast.LENGTH_SHORT).show();
					break;
				}
				case BUTTON_NEXT_ID:
				{
					Toast.makeText(getApplicationContext(), "下一首", Toast.LENGTH_SHORT).show();
					break;
				}
				default:
					break;
				}
			}
		}
	}

	@Override
	protected void onDestroy() 
	{
		if (mReceiver != null) 
		{
			unregisterReceiver(mReceiver);
		}
		super.onDestroy();
	}

	private MediaPlayer mplayer;

	/*
	 * 后台播放背景音
	 */
	private void playBackgroundMusic() 
	{
		if (mplayer == null) 
		{
			mplayer = new MediaPlayer();
			try 
			{
				AssetFileDescriptor afd = this.getAssets().openFd("angel.mp3");
				// 获取音乐数据源
				mplayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				afd.close();
				mplayer.setLooping(true); //设为循环播放
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		try 
		{
			if (mplayer.isPlaying()) 
			{
				return;
			}
			mplayer.prepare();
			mplayer.start();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * 停止播放背景音乐
	 */
	private void stopBackgroundMusic() 
	{
		try 
		{
			if (null != mplayer) 
			{
				if (mplayer.isPlaying()) 
				{
					mplayer.pause();
					mplayer.release();
					mplayer = null;
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
