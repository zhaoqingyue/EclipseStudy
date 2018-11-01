/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyCrankSettingWhiteListActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 白名单
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class SafetyCrankSettingWhiteListActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mAdd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_crank_setting_white);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mAdd = (TextView) findViewById(R.id.addwhitelist);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mAdd.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("白名单");
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
		case R.id.addwhitelist:
		{
			getwhereto();
			break;
		}
		default:
			break;
		}
	}
	
	@SuppressLint("InlinedApi")
	void getwhereto() 
	{
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		window.setContentView(R.layout.view_safety_crank_setting_black_white);
		TextView showtowhite =(TextView) window.findViewById(R.id.crank_to_showtv);
		showtowhite.setText("添加到白名单");
		TextView smsadd = (TextView) window.findViewById(R.id.crank_addsmstoblacklist);
		TextView calladd = (TextView) window.findViewById(R.id.crank_addcalltoblacklist);
		smsadd.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dialog.dismiss();
			}
		});
		calladd.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dialog.dismiss();
			}
		});
	}
}
