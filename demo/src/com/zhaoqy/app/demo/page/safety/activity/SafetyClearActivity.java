/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyClearActActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 手机清理
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class SafetyClearActivity extends Activity implements OnClickListener
{
	private Context        mContext;
	private ImageView      mBack;
	private TextView       mTitle;
	private TextView       mRight;
	private Button         mClear;
	private Button         mSucesss;
	private Button         mShare;
	private ImageView      mClose;
	private ImageView      mClearMan;
	private ImageView      mGarbage1;
	private ImageView      mGarbage2;
	private Animation      mAnimation;
	private LinearLayout   mClearTip1;
	private LinearLayout   mClearTip2;
	private RelativeLayout mClearSucesss;
	private RelativeLayout mClearing;
	private RelativeLayout mClaerMemory;
	private RelativeLayout mClaerAutoStart;
	private RelativeLayout mClaerGarbage;
	private RelativeLayout mClaerPrivacy;
	private RelativeLayout mClaerPackge;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_clear_act);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mRight = (TextView) findViewById(R.id.id_title_right_text);
		mClearMan = (ImageView) findViewById(R.id.clear_main_clearman);
		mGarbage1 = (ImageView) findViewById(R.id.clear_main_garbage1);
		mGarbage2 = (ImageView) findViewById(R.id.clear_main_garbage2);
		mSucesss = (Button) findViewById(R.id.clear_success_btn_success);
		mShare = (Button) findViewById(R.id.clear_success_btn_share);
		mClose = (ImageView) findViewById(R.id.clear_main_return2);
		mClear = (Button) findViewById(R.id.clear_main_bigclear);
		mClearTip1 = (LinearLayout) findViewById(R.id.clear_main_tv);
		mClearTip2 = (LinearLayout) findViewById(R.id.clear_main_tv2);
		mClearSucesss = (RelativeLayout) findViewById(R.id.clear_success);
		mClearing = (RelativeLayout) findViewById(R.id.clear_main_clearing);
		mClaerMemory = (RelativeLayout) findViewById(R.id.clear_main_ll_memory);
		mClaerAutoStart = (RelativeLayout) findViewById(R.id.clear_main_ll_autostart);
		mClaerGarbage = (RelativeLayout) findViewById(R.id.clear_main_ll_garbage);
		mClaerPrivacy = (RelativeLayout) findViewById(R.id.clear_main_ll_privacy);
		mClaerPackge = (RelativeLayout) findViewById(R.id.clear_main_ll_package);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mRight.setOnClickListener(this);
		mClear.setOnClickListener(this);
		mSucesss.setOnClickListener(this);
		mShare.setOnClickListener(this);
		mClose.setOnClickListener(this);
		mClaerMemory.setOnClickListener(this);
		mClaerAutoStart.setOnClickListener(this);
		mClaerGarbage.setOnClickListener(this);
		mClaerPrivacy.setOnClickListener(this);
		mClaerPackge.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("手机清理");
		mBack.setVisibility(View.VISIBLE);
		mRight.setText("定时");
		mRight.setTextColor(0xFF45C01A);
		mRight.setVisibility(View.VISIBLE);
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
		case R.id.id_title_right_text:
		{
			Intent intent = new Intent(mContext, SafetyClearTimeActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.clear_main_ll_memory:
		{
			Intent intent = new Intent(mContext, SafetyClearMemoryActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.clear_main_ll_autostart:
		{
			Intent intent = new Intent(mContext, SafetyClearAutoStartActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.clear_main_ll_garbage:
		{
			Intent intent = new Intent(mContext, SafetyClearGarbageActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.clear_main_ll_privacy:
		{
			Intent intent = new Intent(mContext, SafetyClearPrivacyActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.clear_main_ll_package:
		{
			Intent intent = new Intent(mContext, SafetyClearPackageActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.clear_main_bigclear:
		{
			mClearTip1.setVisibility(View.GONE);		
			mClear.setVisibility(View.GONE);
			mClearing.setVisibility(View.VISIBLE);
			garbageShowAnim();
			showAnim();
			break;
		}
		case R.id.clear_main_return2:
		{
			finish();			
			break;
		}
		case R.id.clear_success_btn_success:
		{
			finish();
			break;
		}
		case R.id.clear_success_btn_share:
		{
			break;
		}
		default:
			break;
		}
	}

	private void showAnim()
	{
		mAnimation=AnimationUtils.loadAnimation(mContext, R.anim.safety_clear);
		mClearMan.startAnimation(mAnimation);
		mAnimation.setAnimationListener(new AnimationListener() 
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
				mClearMan.setImageResource(R.drawable.safety_clean_finish);
				mClearTip2.setVisibility(View.VISIBLE);
				mClearSucesss.setVisibility(View.VISIBLE);
				mClearing.setVisibility(View.GONE);
				mGarbage1.setVisibility(View.GONE);	
				mGarbage2.setVisibility(View.GONE);
			}
		});
	}
	
	private void garbageShowAnim()
	{
		mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.safety_garbage);
		mGarbage2.startAnimation(mAnimation);
		mGarbage1.startAnimation(mAnimation);
	}
}
