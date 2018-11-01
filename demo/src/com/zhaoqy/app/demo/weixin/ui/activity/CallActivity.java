package com.zhaoqy.app.demo.weixin.ui.activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class CallActivity extends FragmentActivity 
{
	protected boolean      isInComingCall;
	protected String       username;
	protected CallingState callingState = CallingState.CANCED;
	protected String       callDruationText;
	protected String       msgid;
	protected AudioManager audioManager;
	protected SoundPool    soundPool;
	protected Ringtone     ringtone;
	protected int          outgoing;

	@Override
	protected void onCreate(Bundle arg0) 
	{
		super.onCreate(arg0);
		audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
	}

	/**
	 * 播放拨号响铃
	 */
	protected int playMakeCallSounds() 
	{
		try 
		{
			//最大音量
			//float audioMaxVolumn = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
			//当前音量
			//float audioCurrentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_RING);
			//float volumnRatio = audioCurrentVolumn / audioMaxVolumn;
			audioManager.setMode(AudioManager.MODE_RINGTONE);
			audioManager.setSpeakerphoneOn(false);

			// 播放
			int id = soundPool.play(outgoing, // 声音资源
					                0.3f,     // 左声道
					                0.3f,     // 右声道
					                1,        // 优先级，0最低
					                -1,       // 循环次数，0是不循环，-1是永远循环
					                1);       // 回放速度，0.5-2.0之间。1为正常速度
			return id;
		} 
		catch (Exception e) 
		{
			return -1;
		}
	}

	//打开扬声器
	protected void openSpeakerOn() 
	{
		try 
		{
			if (!audioManager.isSpeakerphoneOn())
			{
				audioManager.setSpeakerphoneOn(true);
			}
			audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	//关闭扬声器
	protected void closeSpeakerOn() 
	{
		try 
		{
			if (audioManager != null) 
			{
				//int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
				if (audioManager.isSpeakerphoneOn())
				{
					audioManager.setSpeakerphoneOn(false);
				}
				audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
				//audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, curVolume, AudioManager.STREAM_VOICE_CALL);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * 保存通话消息记录
	 * @param type 0：音频，1：视频
	 */
	protected void saveCallRecord(int type) 
	{
	}

	enum CallingState 
	{
		CANCED, 
		NORMAL, 
		REFUESD, 
		BEREFUESD, 
		UNANSWERED, 
		OFFLINE,
		NORESPONSE, BUSY
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		if (soundPool != null)
		{
			soundPool.release();
		}
		if (ringtone != null && ringtone.isPlaying())
		{
			ringtone.stop();
		}
		audioManager.setMode(AudioManager.MODE_NORMAL);
		audioManager.setMicrophoneMute(false);
	}
}
