/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyFlowActActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 流量监控
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
public class SafetyFlowActivity extends TabActivity implements OnClickListener, OnTabChangeListener
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
		setContentView(R.layout.activity_safety_flow);
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
		mView0 = inflater.inflate(R.layout.view_safety_flow_tabhost0, null);
		mView1 = inflater.inflate(R.layout.view_safety_flow_tabhost1, null);
		mView2 = inflater.inflate(R.layout.view_safety_flow_tabhost2, null);	
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mRight.setOnClickListener(this);
		mTabHost.setOnTabChangedListener(this);
	}

	private void initData() 
	{
		mTitle.setText("流量监控");
		mBack.setVisibility(View.VISIBLE);
		mRight.setText("设置");
		mRight.setTextColor(0xFF45C01A);
		mRight.setVisibility(View.VISIBLE);
		
		mTabHost = getTabHost();
		mTabWidget = getTabWidget();
		mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(mView0).setContent(new Intent(mContext, SafetyFlowMonitorActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(mView1).setContent(new Intent(mContext, SafetyFlowFirewallActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator(mView2).setContent(new Intent(mContext, SafetyFlowSortActivity.class)));
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
			Intent intent  = new Intent(mContext, SafetyFlowSettingActivity.class);
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
				mTitle.setText("流量监控");
			} 
			else if (mTabHost.getCurrentTab() == 1) 
			{
				mTitle.setText("联网防火墙");
			}
			else if (mTabHost.getCurrentTab() == 2) 
			{
				mTitle.setText("本月流量排行");
			}
		}
	}
}
