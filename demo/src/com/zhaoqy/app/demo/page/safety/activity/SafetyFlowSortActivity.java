/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyFlowSortActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.activity
 * @Description: 流量排行榜
 * @author: zhaoqy
 * @date: 2015-12-14 下午3:19:29
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.adapter.FlowSortAdapter;
import com.zhaoqy.app.demo.page.safety.item.TrafficInfo;

public class SafetyFlowSortActivity extends Activity 
{
	private List<TrafficInfo> mTrafficInfo;
	private FlowSortAdapter   mAdapter;
	private Context           mContext;
	private ListView          mListView;
	private LinearLayout      mNoUse;
	private LinearLayout      mUseList;
	private Timer             mTimer;
	private TimerTask         mTimerTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_flow_sort);
		mContext = this;
		
		initView();
	    initData();
	}

	private void initView() 
	{
		mListView = (ListView) findViewById(R.id.flow_sort_listview1);
		mNoUse = (LinearLayout) findViewById(R.id.flow_sort_nouseflow);
		mUseList = (LinearLayout) findViewById(R.id.flow_sort_listview);
	}
	
	private void initData() 
	{
		mTrafficInfo = new ArrayList<TrafficInfo>();
		mAdapter = new FlowSortAdapter(mContext, mTrafficInfo);
		initTraficInfo();
		mListView.setAdapter(mAdapter);
	}
	
	public void initTraficInfo() 
	{
		mTrafficInfo.clear();
		//获得一个包管理器
		final PackageManager pm = getPackageManager();
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		final List<ResolveInfo> resolveInfo = pm.queryIntentActivities(intent, 0);
		for (ResolveInfo info : resolveInfo) 
		{
			String name = info.loadLabel(pm).toString();
			Drawable icon = info.loadIcon(pm);
			String packageName = info.activityInfo.packageName;
			int uid = 0;
			try 
			{
				PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
				uid = packageInfo.applicationInfo.uid;
				long received = TrafficStats.getUidRxBytes(uid);
				long transmitted = TrafficStats.getUidTxBytes(uid);
				if (received == -1 && transmitted == -1 ) 
				{
					mNoUse.setVisibility(View.GONE);
					mUseList.setVisibility(View.VISIBLE);
				}
				else
				{
					mNoUse.setVisibility(View.VISIBLE);
					mUseList.setVisibility(View.GONE);
					TrafficInfo trafficInfo = new TrafficInfo();  
					trafficInfo.setName(name);  
					trafficInfo.setIcon(icon);  
					trafficInfo.setUid(uid);  
					mTrafficInfo.add(trafficInfo);
				}
			}
			catch (NameNotFoundException e) 
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onStart() 
	{
		mTimer = new Timer();
		mTimerTask = new TimerTask() 
		{
			@Override
			public void run() 
			{
				Message msg = Message.obtain();
				mHandler.sendMessage(msg);
			}
		};
		mTimer.schedule(mTimerTask, 1000, 3000);
		super.onStart();
	}

	@Override
	protected void onStop() 
	{
		if (mTimer != null) 
		{
			mTimer.cancel();
			mTimer = null;
			mTimerTask = null;
		}
		super.onStop();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			mAdapter.notifyDataSetChanged();
		}
	};
}
