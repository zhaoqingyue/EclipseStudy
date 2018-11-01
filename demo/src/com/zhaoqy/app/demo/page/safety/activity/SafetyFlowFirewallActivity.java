/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyFlowFirewallActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 联网防火墙
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.TrafficStats;
import android.os.Bundle;
import android.widget.ListView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.adapter.FlowFirewallAdapter;

public class SafetyFlowFirewallActivity extends Activity
{
	private FlowFirewallAdapter mAdapter;
	private Context             mContext;
	private ListView            mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_flow_firewall);
		mContext = this;
		
		initView();
		getApp();
	}
	
	private void initView() 
	{
		mListView = (ListView) findViewById(R.id.flow_firewall_listview);
	}
	
	public void getApp() 
	{
		final PackageManager pm = getPackageManager();
		List<ResolveInfo>  trafficlists = new ArrayList<ResolveInfo>();
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		final List<ResolveInfo> lists = pm.queryIntentActivities(intent, 0);
		for(ResolveInfo info : lists)
		{
			if(TrafficStats.getUidRxBytes(info.activityInfo.applicationInfo.uid) != -1 
				&& TrafficStats.getUidTxBytes(info.activityInfo.applicationInfo.uid) != -1)
			{
				trafficlists.add(info);
			}
		}	
		mAdapter = new FlowFirewallAdapter(mContext, trafficlists);
		mListView.setAdapter(mAdapter);
	}
}
