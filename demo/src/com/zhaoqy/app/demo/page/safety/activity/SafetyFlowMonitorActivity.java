/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyFlowMonitorActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 流量监控
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import android.app.Activity;
import android.app.Dialog;
import android.net.TrafficStats;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.util.TrafficDataUtil;

public class SafetyFlowMonitorActivity extends Activity implements OnClickListener
{
	private Button      mAlign;
	private Button      mSure;
	private Button      mCancel;
	private TextView    mUsed;    //本月已用流量
	private TextView    mResidue; //本月剩余流量
	private TextView    mPercent; //本月剩余流量百分比
	private ProgressBar mProgressBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_flow_monitor);
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mAlign = (Button) findViewById(R.id.flow_correct);
		mUsed = (TextView) findViewById(R.id.flow_thismonth_usedflow);
		mResidue = (TextView) findViewById(R.id.flow_thismmonth_residue);
		mProgressBar = (ProgressBar) findViewById(R.id.flow_progress);
		mPercent = (TextView) findViewById(R.id.flow_monitoring_residue);
	}
	
	private void setListener() 
	{
		mAlign.setOnClickListener(this);
	}

	private void initData() 
	{
		//2g/3g总接收数据大小
		long received = TrafficStats.getMobileRxBytes();
		//2g/3g总上传数据大小
		long transmitted = TrafficStats.getMobileRxBytes();
		long total = received + transmitted;
		//通过工具类的方法将流量转换成字符串，加上单位
		String totalStr = TrafficDataUtil.getTrafficData(total);
		mUsed.setText(totalStr);
		//本月总共流量，字节为单位
		long thisMonthAllFlow = 100 * (1024 * 1024);
		mResidue.setText(TrafficDataUtil.getTrafficData(thisMonthAllFlow - total));
		long progress = getProgress(total, thisMonthAllFlow - total);
		mProgressBar.setProgress((int) progress);
		double s = (float) (thisMonthAllFlow - total) / (float) thisMonthAllFlow;
		String str = String.valueOf((s * 100));
		String[] strarray = str.split("\\.");
		mPercent.setText(strarray[0] + "%");
	}
	
	private long getProgress(long usedFlow, long allFlow) 
	{
		long a = (usedFlow);
		long b = (allFlow);
		long c = a / b * 100;
		return c;
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.flow_correct:
		{
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
			Window window = dialog.getWindow();
			window.setContentView(R.layout.dialog_safety_flow_monitor);
			mSure = (Button) window.findViewById(R.id.flow_monitoring_dialog_ok);
			mCancel = (Button) window.findViewById(R.id.flow_monitoring_dialog_no);
			final EditText thismonth_used_et = (EditText) window.findViewById(R.id.flow_monitoring_dialog_et);
			mSure.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					mUsed.setText(thismonth_used_et.getText().toString() + "M");
					long thisMonthAllFlow = 100 * (1024 * 1024);
					long used = Long.valueOf(thismonth_used_et.getText().toString()) * 1024 * 1024;
					mResidue.setText(TrafficDataUtil.getTrafficData(thisMonthAllFlow - used));
					double s = (float) (thisMonthAllFlow - used) / (float) thisMonthAllFlow;
					String str = String.valueOf((s * 100));
					String[] strarray = str.split("\\.");
					mPercent.setText(strarray[0] + "%");
					dialog.dismiss();
				}
			});
			mCancel.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					dialog.dismiss();
				}
			});
			break;
		}
		default:
			break;
		}
	}
}
