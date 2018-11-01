/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: VoiceCallActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.weixin.ui.activity
 * @Description: 语音通话
 * @author: zhaoqy
 * @date: 2015-12-1 下午4:48:47
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class VoiceCallActivity extends CallActivity implements OnClickListener 
{
	private View        mComingContainer;
	private View        mVoiceContronl;
	private Button      mHangup;
	private Button      mRefuse;
	private Button      mAnswer;
	private ImageView   mMuteImage;
	private ImageView   mHandsFreeImage;
	private TextView    mCallState;
	private TextView    mNick;
	private Chronometer mChronometer;
	private Handler     mHandler = new Handler();
	private String      mTips;
	private boolean     mIsMuteState;
	private boolean     mIsHandsfreeState;
	private int         mStreamID;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weixin_voice_call);
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mComingContainer = findViewById(R.id.ll_coming_call);
		mRefuse = (Button) findViewById(R.id.btn_refuse_call);
		mAnswer = (Button) findViewById(R.id.btn_answer_call);
		mHangup = (Button) findViewById(R.id.btn_hangup_call);
		mMuteImage = (ImageView) findViewById(R.id.iv_mute);
		mHandsFreeImage = (ImageView) findViewById(R.id.iv_handsfree);
		mCallState = (TextView) findViewById(R.id.tv_call_state);
		mNick = (TextView) findViewById(R.id.tv_nick);
		mChronometer = (Chronometer) findViewById(R.id.chronometer);
		mVoiceContronl = findViewById(R.id.ll_voice_control);
	}
	
	private void setListener() 
	{
		mRefuse.setOnClickListener(this);
		mAnswer.setOnClickListener(this);
		mHangup.setOnClickListener(this);
		mMuteImage.setOnClickListener(this);
		mHandsFreeImage.setOnClickListener(this);
	}
	
	private void initData() 
	{
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | 
							 WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		//注册语音电话的状态的监听
		addCallStateListener();
		msgid = UUID.randomUUID().toString();
		username = getIntent().getStringExtra("username");
		//语音电话是否为接收的
		isInComingCall = getIntent().getBooleanExtra("isComingCall", false);

		//设置通话人
		mNick.setText(username);
		if (!isInComingCall) 
		{
			//拨打电话
			soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
			outgoing = soundPool.load(this, R.raw.outgoing, 1);
			mComingContainer.setVisibility(View.INVISIBLE);
			mHangup.setVisibility(View.VISIBLE);
			mTips = getResources().getString(R.string.weixin_connected_to_each_other);
			mCallState.setText(mTips);
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
			Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			audioManager.setMode(AudioManager.MODE_RINGTONE);
			audioManager.setSpeakerphoneOn(true);
			ringtone = RingtoneManager.getRingtone(this, ringUri);
			ringtone.play();
		}
	}

	/**
	 * 设置电话监听
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
				saveCallRecord(0);
				finish();
			}
			callingState = CallingState.REFUESD;
			break;
		}
		case R.id.btn_answer_call: //接听电话
		{
			mComingContainer.setVisibility(View.INVISIBLE);
			mHangup.setVisibility(View.VISIBLE);
			mVoiceContronl.setVisibility(View.VISIBLE);
			if (ringtone != null)
			{
				ringtone.stop();
			}
			closeSpeakerOn();
			if (isInComingCall) 
			{
				try 
				{
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					saveCallRecord(0);
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
				saveCallRecord(0);
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
				mHandsFreeImage.setImageResource(R.drawable.weixin_icon_speaker_on);
				openSpeakerOn();
				mIsHandsfreeState = true;
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
	}

	@Override
	public void onBackPressed() 
	{
		callDruationText = mChronometer.getText().toString();
		saveCallRecord(0);
		finish();
	}
}
