/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyCrankSettingActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 拦截设置
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class SafetyCrankSettingActivity extends Activity implements OnClickListener
{
	private Context        mContext;
	private ImageView      mBack;
	private TextView       mTitle;
	private RelativeLayout mBlack;
	private RelativeLayout mWhite;
	private RelativeLayout mModel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_crank_setting);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mBlack = (RelativeLayout) findViewById(R.id.crankset_black);
		mWhite = (RelativeLayout) findViewById(R.id.crankset_write);
		mModel =(RelativeLayout) findViewById(R.id.crankset_mode);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mBlack.setOnClickListener(this);
		mWhite.setOnClickListener(this);
		mModel.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("拦截设置");
		mBack.setVisibility(View.VISIBLE);
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
		case R.id.crankset_black:
		{
			Intent intent = new Intent(mContext, SafetyCrankSettingBlackListActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.crankset_write:
		{
			Intent intent1 = new Intent(mContext, SafetyCrankSettingWhiteListActivity.class);
			startActivity(intent1);
			break;			
		}
		case R.id.crankset_mode:
		{
			Intent intent2 = new Intent(mContext, SafetyCrankSettingModeActivity.class);
			startActivity(intent2);
			break;
		}
		default:
			break;
		}
	}
}
