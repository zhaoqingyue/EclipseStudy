/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: VideoCallActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.weixin.ui.activity
 * @Description: 视频通话
 * @author: zhaoqy
 * @date: 2015-12-1 下午4:21:58
 * @version: V1.0
 */

package com.zhaoqy.app.demo.weixin.ui.activity;

import java.util.UUID;

import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class VideoCallActivity extends CallActivity implements OnClickListener
{
	private SurfaceView   mLocalSurface;
	private SurfaceHolder mLocalSurfaceHolder;
	private SurfaceView   mOppositeSurface;
	private SurfaceHolder mOppositeSurfaceHolder;
	private Handler       mHandler = new Handler();
	private Button        mRefuse;
	private Button        mAnswer;
	private Button        mHangup;
	private ImageView     mMuteImage;
	private ImageView     mHandsFreeImage;
	private TextView      mNick;
	private TextView      mCallState;
	private Chronometer   mChronometer;
	private View          mComingBtnContainer;
	private View          mVoiceContronl;
	private View          mRootContainer;
	private View          mTopContainer;
	private View          mBottomContainer;
	private boolean       mIsMuteState;
	private boolean       mIsHandsfreeState;
	private int           mStreamID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weixin_video_call);
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mCallState = (TextView) findViewById(R.id.tv_call_state);
		mComingBtnContainer = findViewById(R.id.ll_coming_call);
		mRootContainer = findViewById(R.id.root_layout);
		mRefuse = (Button) findViewById(R.id.btn_refuse_call);
		mAnswer = (Button) findViewById(R.id.btn_answer_call);
		mHangup = (Button) findViewById(R.id.btn_hangup_call);
		mMuteImage = (ImageView) findViewById(R.id.iv_mute);
		mHandsFreeImage = (ImageView) findViewById(R.id.iv_handsfree);
		mNick = (TextView) findViewById(R.id.tv_nick);
		mChronometer = (Chronometer) findViewById(R.id.chronometer);
		mVoiceContronl = findViewById(R.id.ll_voice_control);
		mTopContainer = findViewById(R.id.ll_top_container);
		mBottomContainer = findViewById(R.id.ll_bottom_container);
	}
	
	private void setListener() 
	{
		mRefuse.setOnClickListener(this);
		mAnswer.setOnClickListener(this);
		mHangup.setOnClickListener(this);
		mMuteImage.setOnClickListener(this);
		mHandsFreeImage.setOnClickListener(this);
		mRootContainer.setOnClickListener(this);
	}
	
	private void initData() 
	{
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | 
							 WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		msgid = UUID.randomUUID().toString();
		//获取通话是否为接收方向的
		isInComingCall = getIntent().getBooleanExtra("isComingCall", false);
		username = getIntent().getStringExtra("username");

		//设置通话人
		mNick.setText(username);

		//显示本地图像的surfaceview
		mLocalSurface = (SurfaceView) findViewById(R.id.local_surface);
		mLocalSurface.setZOrderMediaOverlay(true);
		mLocalSurface.setZOrderOnTop(true);
		mLocalSurfaceHolder = mLocalSurface.getHolder();

		//显示对方图像的surfaceview
		mOppositeSurface = (SurfaceView) findViewById(R.id.opposite_surface);
		mOppositeSurfaceHolder = mOppositeSurface.getHolder();
		mLocalSurfaceHolder.addCallback(new localCallback());
		mOppositeSurfaceHolder.addCallback(new oppositeCallback());

		//设置通话监听
		addCallStateListener();

		if (!isInComingCall) 
		{
			//拨打电话
			soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
			outgoing = soundPool.load(this, R.raw.outgoing, 1);

			mComingBtnContainer.setVisibility(View.INVISIBLE);
			mHangup.setVisibility(View.VISIBLE);
			String st = getResources().getString(R.string.weixin_connected_to_each_other);
			mCallState.setText(st);

			mHandler.postDelayed(new Runnable() 
			{
				public void run() 
				{
					mStreamID = playMakeCallSounds();
				}
			}, 300);
		} 
		else 
		{ 
			//有电话进来
			mVoiceContronl.setVisibility(View.INVISIBLE);
			mLocalSurface.setVisibility(View.INVISIBLE);
			Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			audioManager.setMode(AudioManager.MODE_RINGTONE);
			audioManager.setSpeakerphoneOn(true);
			ringtone = RingtoneManager.getRingtone(this, ringUri);
			ringtone.play();
		}
	}

	/**
	 * 本地SurfaceHolder callback
	 */
	class localCallback implements SurfaceHolder.Callback 
	{
		@Override
		public void surfaceCreated(SurfaceHolder holder) {}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {}
	}

	/**
	 * 对方SurfaceHolder callback
	 */
	class oppositeCallback implements SurfaceHolder.Callback 
	{
		@SuppressWarnings("deprecation")
		@Override
		public void surfaceCreated(SurfaceHolder holder) 
		{
			holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {}
	}

	/**
	 * 设置通话状态监听
	 */
	void addCallStateListener() 
	{
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.btn_refuse_call: //拒绝接听
		{
			if (ringtone != null)
			{
				ringtone.stop();
			}
			try 
			{
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace();
				saveCallRecord(1);
				finish();
			}
			callingState = CallingState.REFUESD;
			break;
		}
		case R.id.btn_answer_call: //接听电话
		{
			mComingBtnContainer.setVisibility(View.INVISIBLE);
			mHangup.setVisibility(View.VISIBLE);
			mVoiceContronl.setVisibility(View.VISIBLE);
			mLocalSurface.setVisibility(View.VISIBLE);
			if (ringtone != null)
			{
				ringtone.stop();
			}

			if (isInComingCall) 
			{
				try 
				{
					openSpeakerOn();
					mHandsFreeImage.setImageResource(R.drawable.weixin_icon_speaker_on);
					mIsHandsfreeState = true;
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					saveCallRecord(1);
					finish();
				}
			}
			break;
		}
		case R.id.btn_hangup_call: //挂断电话
		{
			if (soundPool != null)
			{
				soundPool.stop(mStreamID);
			}
			try 
			{
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				saveCallRecord(1);
				finish();
			}
			break;
		}
		case R.id.iv_mute: //静音开关
		{
			if (mIsMuteState) 
			{
				//关闭静音
				mMuteImage.setImageResource(R.drawable.weixin_icon_mute_normal);
				audioManager.setMicrophoneMute(false);
				mIsMuteState = false;
			} 
			else 
			{
				//打开静音
				mMuteImage.setImageResource(R.drawable.weixin_icon_mute_on);
				audioManager.setMicrophoneMute(true);
				mIsMuteState = true;
			}
			break;
		}
		case R.id.iv_handsfree: //免提开关
		{
			if (mIsHandsfreeState) 
			{
				//关闭免提
				mHandsFreeImage.setImageResource(R.drawable.weixin_icon_speaker_normal);
				closeSpeakerOn();
				mIsHandsfreeState = false;
			} 
			else 
			{
				//打开免提
				mHandsFreeImage.setImageResource(R.drawable.weixin_icon_speaker_on);
				openSpeakerOn();
				mIsHandsfreeState = true;
			}
			break;
		}
		case R.id.root_layout:
		{
			if (callingState == CallingState.NORMAL) 
			{
				if (mBottomContainer.getVisibility() == View.VISIBLE) 
				{
					mBottomContainer.setVisibility(View.GONE);
					mTopContainer.setVisibility(View.GONE);

				} 
				else 
				{
					mBottomContainer.setVisibility(View.VISIBLE);
					mTopContainer.setVisibility(View.VISIBLE);
				}
			}
			break;
		}
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		mOppositeSurface = null;
	}

	@Override
	public void onBackPressed() 
	{
		callDruationText = mChronometer.getText().toString();
		saveCallRecord(1);
		finish();
	}
}
