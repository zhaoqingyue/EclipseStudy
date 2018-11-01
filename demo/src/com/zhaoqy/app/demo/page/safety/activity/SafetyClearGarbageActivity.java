/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyClearGarbageActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 垃圾清理
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
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
public class SafetyClearGarbageActivity extends TabActivity implements OnClickListener, OnTabChangeListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
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
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView0 = inflater.inflate(R.layout.view_safety_clear_garbage0, null);
		mView1 = inflater.inflate(R.layout.view_safety_clear_garbage1, null);
		mView2 = inflater.inflate(R.layout.view_safety_clear_garbage2, null);	
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mTabHost.setOnTabChangedListener(this);
	}

	private void initData() 
	{
		mTitle.setText("垃圾清理");
		mBack.setVisibility(View.VISIBLE);
		
		mTabHost = getTabHost();
		mTabWidget = getTabWidget();
		Intent intent = new Intent(mContext, SafetyClearGarbageContentActivity.class);
		mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(mView0).setContent(intent));
		mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(mView1).setContent(intent));
		mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator(mView2).setContent(intent));
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
		default:
			break;
		}
	}

	@Override
	public void onTabChanged(String tabId) 
	{
	}
}
