/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyInterceptReportActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 我要举报
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.adapter.InterceptReportAdapter;
import com.zhaoqy.app.demo.page.safety.item.ReportInfo;

public class SafetyInterceptReportActivity extends Activity implements OnClickListener, OnItemClickListener 
{
	private InterceptReportAdapter mAdapter;
	private ArrayList<ReportInfo>  mAppList;
	private Context    mContext;
	private ImageView  mBack;
	private TextView   mTitle;
	private Button     mReport;
	private Button     mDialogShare;
	private Button     mDialogBack;
	private ListView   mListView;
	private ImageView  mLoad;
	private ReportInfo mReportInfo;
	private int        mCount = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_intercept_report);
		mContext = this;

		initView();
		initData();
		setListener();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mLoad = (ImageView) findViewById(R.id.ad_report_load);
		mReport = (Button) findViewById(R.id.ad_report_bt);
		mListView = (ListView) findViewById(R.id.ad_jubao_lv);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mReport.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("我要举报");
		mBack.setVisibility(View.VISIBLE);
		startAnim();
		new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				getInfo();
				mHandler.sendEmptyMessageDelayed(1, 500);
			}
		}).start();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() 
	{
		public void handleMessage(android.os.Message msg) 
		{
			msg.what = 1;
			stopAnim();
			mLoad.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			mAdapter = new InterceptReportAdapter(mContext, mAppList);
			mListView.setAdapter(mAdapter);
		};
	};

	private void startAnim() 
	{
		Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.safety_intercept_load);
		mLoad.startAnimation(anim);
	}

	private void stopAnim() 
	{
		mLoad.clearAnimation();
		mLoad.setVisibility(View.GONE);
	}

	@SuppressLint("ShowToast")
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
		case R.id.ad_report_bt:
		{
			if (mCount > 0) 
			{
				showDialog();
				for (int i = 0; i < mAppList.size(); i++) 
				{
					if (!mAppList.get(i).isChecked()) 
					{
						mAppList.get(i).setIcon(getResources().getDrawable(R.drawable.safety_crank_tabhost_sms_reported));
						mAppList.get(i).setReport(true);
						mAdapter.notifyDataSetChanged();
					}
				}
				mReport.setText("请选择要举报的软件");
				mCount = 0;
			} 
			else 
			{
				Toast.makeText(this, "请选择要举报的软件", 0).show();
			}
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		mReportInfo = mAppList.get(position);
		if (mReportInfo.isChecked()) 
		{
			mCount++;
			if (!mReportInfo.isReport) 
			{
				mReportInfo.setIcon(getResources().getDrawable(R.drawable.safety_intercept_checkbox_checked));
				mAdapter.notifyDataSetChanged();
				mReport.setText("请选择要举报的软件" + "(" + mCount + ")");
				mReportInfo.setChecked(false);
			}
		} 
		else 
		{
			if (mCount > 0) 
			{
				mCount--;
			}
			if (!mReportInfo.isReport) 
			{
				mReportInfo.setIcon(getResources().getDrawable(R.drawable.safety_intercept_checkbox_disable_checked));
				mAdapter.notifyDataSetChanged();
				if (mCount == 0) 
				{
					mReport.setText("请选择要举报的软件");
				} 
				else 
				{
					mReport.setText("请选择要举报的软件" + "(" + mCount + ")");
				}
				mReportInfo.setChecked(true);
			}
		}
	}

	//获取手机内所有应用程序的应用图标，应用名称
	public void getInfo() 
	{
		mAppList = new ArrayList<ReportInfo>();
		List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) 
		{
			PackageInfo packageInfo = packages.get(i);
			mReportInfo = new ReportInfo();
			mReportInfo.appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
			mReportInfo.appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
			mReportInfo.Checked = true;
			mReportInfo.isReport = false;
			mReportInfo.Icon = getResources().getDrawable(R.drawable.safety_intercept_checkbox_disable_checked);
			mAppList.add(mReportInfo);
		}
	}

	public void showDialog() 
	{
		final Dialog dialog = new Dialog(mContext, R.style.safety_intercept_report_dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_safety_intercept_report);
		mDialogShare = (Button) window.findViewById(R.id.ad_report_dialog_share);
		mDialogBack = (Button) window.findViewById(R.id.ad_report_dialog_fanhui);
		mDialogShare.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dialog.dismiss();
			}
		});
		mDialogBack.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dialog.dismiss();
			}
		});
	}
}
