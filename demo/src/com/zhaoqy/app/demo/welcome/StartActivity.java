package com.zhaoqy.app.demo.welcome;

import com.zhaoqy.app.demo.MainActivity;
import com.zhaoqy.app.demo.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class StartActivity extends Activity 
{
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		mContext = this;
		mHandler.sendEmptyMessageDelayed(0, 1000);
	}
	
	@SuppressLint("HandlerLeak") 
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			if (getFirst())
			{
				setFirst();
				Intent intent = new Intent(mContext, WelcomeActivity.class);
				startActivity(intent);
				finish();
			}
			else
			{
				Intent intent = new Intent(mContext, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}
	};
	
	private boolean getFirst()
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = getSharedPreferences("sp", mContext.MODE_PRIVATE);
		return sp.getBoolean("first", true);
	}
	
	private void setFirst()
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = getSharedPreferences("sp", mContext.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("first", false);
		editor.commit();
	}
}
