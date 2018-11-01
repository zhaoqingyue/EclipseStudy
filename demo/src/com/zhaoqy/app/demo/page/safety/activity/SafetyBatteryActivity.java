/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyBatteryActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 节电管理
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class SafetyBatteryActivity extends Activity implements OnClickListener 
{
	private BroadcastReceiver mBroadcast;
	private FrameLayout mFrameLayout;
	private ImageView mBack;
	private TextView  mTitle;
	private ImageView mFristNum;
	private ImageView mSecondNum;
	private ImageView mThirdNum;
	private ImageView mFourthNum;
	private int       mLevel;
	private int       mScale;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_battery);

		initView();
		initData();
		setListener();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mFrameLayout = (FrameLayout) findViewById(R.id.battery_anim);
		mFristNum = (ImageView) findViewById(R.id.battery_fristnum);
		mSecondNum = (ImageView) findViewById(R.id.battery_secondnum);
		mThirdNum = (ImageView) findViewById(R.id.battery_thirdnum);
		mFourthNum = (ImageView) findViewById(R.id.battery_fourthnum);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("360省电王");
		mBack.setVisibility(View.VISIBLE);
		getbatterylevel();
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
		default:
			break;
		}
	}

	//得到当前电量
	private void getbatterylevel() 
	{
		mBroadcast = new BroadcastReceiver() 
		{
			public void onReceive(Context context, Intent intent) 
			{
				if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) 
				{
					mLevel = intent.getIntExtra("level", 0);
					mScale = intent.getIntExtra("scale", 100);
					setbatteryAnmi();
				}
			}
		};
		registerReceiver(mBroadcast, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}

	//电池动画
	private void setbatteryAnmi() 
	{
		float f = (float) ((mLevel * 100) / mScale) / 100;
		Animation animation = new ScaleAnimation(1.0f, 1.0f, 1.0f, f, 0, 210);
		animation.setDuration(1000);
		animation.setFillAfter(true);
		animation.setAnimationListener(new AnimationListener() 
		{
			@Override
			public void onAnimationStart(Animation animation) 
			{
			}

			@Override
			public void onAnimationRepeat(Animation animation) 
			{
			}

			@Override
			public void onAnimationEnd(Animation animation) 
			{
				settime();
			}
		});
		mFrameLayout.startAnimation(animation);
	}

	private void settime() 
	{
		//预算估计 每分钟耗电0.09电量算出还能用的时间
		float minutetime = (float) (mLevel / 0.10);
		//估算能用的小时
		float hourtime = (float) (minutetime / 60);
		String Stringhour = hourtime + "";
		String[] array = new String[10];
		array = Stringhour.split("\\.");
		String xuanze = array[0];
		//判断拆分的第一个数组内元素个数是不是1，是着在前面加上0
		if (xuanze.length() == 1) 
		{
			String xuanze2 = new String();
			xuanze2 = "0" + xuanze;
			xuanze = xuanze2;
		}
		//得到要设置的1个数
		String frist = String.valueOf(xuanze.charAt(0));
		//得到要设置的2个数
		String second = String.valueOf(xuanze.charAt(1));
		//将小数点后面的部分前加上0.
		String qq = new String("0." + array[1]);
		int qwe = (int) (Float.valueOf(qq) * 60);
		String arrat1 = String.valueOf(qwe);
		//判断是否只有一个数，是一个数在前面加上一个"0"
		if (arrat1.length() == 1) 
		{
			String xuanze3 = new String();
			xuanze3 = "0" + xuanze;
			arrat1 = xuanze3;
		}
		//得到要设置的3个数
		String thrid = String.valueOf(arrat1.charAt(0));
		//得到要设置的4个数
		String fourth = String.valueOf(arrat1.charAt(1));
		//将四个数放在数组里面
		String[] stringarr = new String[4];
		stringarr[0] = frist;
		stringarr[1] = second;
		stringarr[2] = thrid;
		stringarr[3] = fourth;
		//设置图片
		for (int i = 0; i < stringarr.length; i++) 
		{
			if (stringarr[i].equals("0")) 
			{
				if (i == 0) 
				{
					mFristNum.setBackgroundResource(R.drawable.safety_clear_battery_number_0);
				}
				else if (i == 1) 
				{
					mSecondNum.setBackgroundResource(R.drawable.safety_clear_battery_number_0);
				} 
				else if (i == 2) 
				{
					mThirdNum.setBackgroundResource(R.drawable.safety_clear_battery_number_0);
				}
				else 
				{
					mFourthNum.setBackgroundResource(R.drawable.safety_clear_battery_number_0);
				}
			} 
			else if (stringarr[i].equals("1")) 
			{
				if (i == 0) 
				{
					mFristNum.setBackgroundResource(R.drawable.safety_clear_battery_number_1);
				} 
				else if (i == 1) 
				{
					mSecondNum.setBackgroundResource(R.drawable.safety_clear_battery_number_1);
				} 
				else if (i == 2) 
				{
					mThirdNum.setBackgroundResource(R.drawable.safety_clear_battery_number_1);
				}
				else 
				{
					mFourthNum.setBackgroundResource(R.drawable.safety_clear_battery_number_1);
				}
			}
			else if (stringarr[i].equals("2")) 
			{
				if (i == 0) 
				{
					mFristNum.setBackgroundResource(R.drawable.safety_clear_battery_number_2);
				} 
				else if (i == 1) 
				{
					mSecondNum.setBackgroundResource(R.drawable.safety_clear_battery_number_2);
				}
				else if (i == 2) 
				{
					mThirdNum.setBackgroundResource(R.drawable.safety_clear_battery_number_2);
				} 
				else 
				{
					mFourthNum.setBackgroundResource(R.drawable.safety_clear_battery_number_2);
				}
			} 
			else if (stringarr[i].equals("3")) 
			{
				if (i == 0) 
				{
					mFristNum.setBackgroundResource(R.drawable.safety_clear_battery_number_3);
				} 
				else if (i == 1) 
				{
					mSecondNum.setBackgroundResource(R.drawable.safety_clear_battery_number_3);
				} 
				else if (i == 2) 
				{
					mThirdNum.setBackgroundResource(R.drawable.safety_clear_battery_number_3);
				} 
				else 
				{
					mFourthNum.setBackgroundResource(R.drawable.safety_clear_battery_number_3);
				}
			} 
			else if (stringarr[i].equals("4")) 
			{
				if (i == 0) 
				{
					mFristNum.setBackgroundResource(R.drawable.safety_clear_battery_number_4);
				} 
				else if (i == 1) 
				{
					mSecondNum.setBackgroundResource(R.drawable.safety_clear_battery_number_4);
				} 
				else if (i == 2) 
				{
					mThirdNum.setBackgroundResource(R.drawable.safety_clear_battery_number_4);
				} 
				else 
				{
					mFourthNum.setBackgroundResource(R.drawable.safety_clear_battery_number_4);
				}
			} 
			else if (stringarr[i].equals("5")) 
			{
				if (i == 0) 
				{
					mFristNum.setBackgroundResource(R.drawable.safety_clear_battery_number_5);
				} 
				else if (i == 1) 
				{
					mSecondNum.setBackgroundResource(R.drawable.safety_clear_battery_number_5);
				} 
				else if (i == 2) 
				{
					mThirdNum.setBackgroundResource(R.drawable.safety_clear_battery_number_5);
				} 
				else 
				{
					mFourthNum.setBackgroundResource(R.drawable.safety_clear_battery_number_5);
				}
			} 
			else if (stringarr[i].equals("6")) 
			{
				if (i == 0) 
				{
					mFristNum.setBackgroundResource(R.drawable.safety_clear_battery_number_6);
				} 
				else if (i == 1) 
				{
					mSecondNum.setBackgroundResource(R.drawable.safety_clear_battery_number_6);
				} 
				else if (i == 2) 
				{
					mThirdNum.setBackgroundResource(R.drawable.safety_clear_battery_number_6);
				} 
				else 
				{
					mFourthNum.setBackgroundResource(R.drawable.safety_clear_battery_number_6);
				}
			} 
			else if (stringarr[i].equals("7")) 
			{
				if (i == 0) 
				{
					mFristNum.setBackgroundResource(R.drawable.safety_clear_battery_number_7);
				} 
				else if (i == 1) 
				{
					mSecondNum.setBackgroundResource(R.drawable.safety_clear_battery_number_7);
				} 
				else if (i == 2) 
				{
					mThirdNum.setBackgroundResource(R.drawable.safety_clear_battery_number_7);
				} 
				else 
				{
					mFourthNum.setBackgroundResource(R.drawable.safety_clear_battery_number_7);
				}
			} 
			else if (stringarr[i].equals("8")) 
			{
				if (i == 0) 
				{
					mFristNum.setBackgroundResource(R.drawable.safety_clear_battery_number_8);
				} 
				else if (i == 1) 
				{
					mSecondNum.setBackgroundResource(R.drawable.safety_clear_battery_number_8);
				} 
				else if (i == 2) 
				{
					mThirdNum.setBackgroundResource(R.drawable.safety_clear_battery_number_8);
				} 
				else 
				{
					mFourthNum.setBackgroundResource(R.drawable.safety_clear_battery_number_8);
				}
			} 
			else if (stringarr[i].equals("9")) 
			{
				if (i == 0) 
				{
					mFristNum.setBackgroundResource(R.drawable.safety_clear_battery_number_9);
				} 
				else if (i == 1) 
				{
					mSecondNum.setBackgroundResource(R.drawable.safety_clear_battery_number_9);
				}
				else if (i == 2) 
				{
					mThirdNum.setBackgroundResource(R.drawable.safety_clear_battery_number_9);
				} 
				else 
				{
					mFourthNum.setBackgroundResource(R.drawable.safety_clear_battery_number_9);
				}
			}
		}
	}
	
	protected void onDestroy() 
	{
		super.onDestroy();
		unregisterReceiver(mBroadcast);
	}
}
