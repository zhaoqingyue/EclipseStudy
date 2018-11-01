/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyCrankActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.activity
 * @Description: 骚扰拦截
 * @author: zhaoqy
 * @date: 2015-12-10 下午5:06:05
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

@SuppressWarnings("deprecation")
public class SafetyCrankActivity extends TabActivity implements OnClickListener, OnTabChangeListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mRight;
	private TabHost   mTabHost;
	private TabWidget mTabWidget;
	private View      mView0; 
	private View      mView1; 
	private View      mView2; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_clear_garbage);
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
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView0 = inflater.inflate(R.layout.view_safety_crank_tabhost0, null);
		mView1 = inflater.inflate(R.layout.view_safety_crank_tabhost1, null);
		mView2 = inflater.inflate(R.layout.view_safety_crank_tabhost2, null);	
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mRight.setOnClickListener(this);
		mTabHost.setOnTabChangedListener(this);
	}

	private void initData() 
	{
		mTitle.setText("垃圾短息");
		mBack.setVisibility(View.VISIBLE);
		mRight.setText("设置");
		mRight.setTextColor(0xFF45C01A);
		mRight.setVisibility(View.VISIBLE);
		
		mTabHost = getTabHost();
		mTabWidget = getTabWidget();
		mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(mView0).setContent(new Intent(mContext, SafetyCrankTabhostSmsActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(mView1).setContent(new Intent(mContext, SafetyCrankTabhostCallActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator(mView2).setContent(new Intent(mContext, SafetyCrankTabhostMarkActivity.class)));
		mTabWidget.setOrientation(LinearLayout.HORIZONTAL);
		mTabHost.setCurrentTab(0);
		mTabHost.setup();
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
			Intent intent  = new Intent(mContext, SafetyCrankSettingActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onTabChanged(String tabId) 
	{
		for (int i=0; i<mTabWidget.getChildCount(); i++) 
		{
			if (mTabHost.getCurrentTab() == 0) 
			{
				mTitle.setText("垃圾短息");
			} 
			else if (mTabHost.getCurrentTab() == 1) 
			{
				mTitle.setText("骚扰电话");
			}
			else if (mTabHost.getCurrentTab() == 2) 
			{
				mTitle.setText("标记管理");
			}
		}
	}
}
