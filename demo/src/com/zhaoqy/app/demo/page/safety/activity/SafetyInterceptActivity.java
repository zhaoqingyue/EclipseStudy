/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyInterceptActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 恶意广告拦截
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.adapter.InterceptAdapter;
import com.zhaoqy.app.demo.page.safety.item.AppInfo;
import com.zhaoqy.app.demo.page.safety.util.AdManagerUtil;
import com.zhaoqy.app.demo.page.safety.view.PopMenu;
import dalvik.system.DexFile;

public class SafetyInterceptActivity extends Activity implements OnClickListener, OnItemClickListener
{
	public  static ArrayList<AppInfo> mAppList;
	private InterceptAdapter mAdapter;
	private Context     mContext;
	private ImageView   mBack;
	private TextView    mTitle;
	private ImageView   mRight;
	private ImageView   mLoad;
	private TextView    mNum;  
	private View        mPopView;
	private PopupWindow mPop;
	private View        mDetect;
	private ListView    mListView;
	private AppInfo     mAppInfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_intercept);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mRight = (ImageView) findViewById(R.id.id_title_right_img);
		mLoad = (ImageView) findViewById(R.id.ad_main_load);
		mNum = (TextView) findViewById(R.id.ad_main_num);
		mListView = (ListView) findViewById(R.id.ad_main_lv);
		mDetect = findViewById(R.id.ad_main_ll2);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mRight.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mDetect.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("恶意广告拦截");
		mBack.setVisibility(View.VISIBLE);
		mRight.setVisibility(View.VISIBLE);
		mRight.setBackgroundResource(R.drawable.safety_intercept_menu);
		
		startAnim();
		initpop();
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
			mLoad.setVisibility(View.GONE);
			stopAnim();
			mListView.setVisibility(View.VISIBLE);
			mAdapter = new InterceptAdapter(mContext, mAppList);
			mListView.setAdapter(mAdapter);
			mNum.setText(mAppList.size()+"");
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
	
	private void initpop() 
	{
		mPopView = LayoutInflater.from(mContext).inflate(R.layout.view_safety_intercept_pop, null);
		mPop = PopMenu.getPopupWindow(mPopView);
		mPop.setFocusable(true);
		Bitmap bitmap = null;
		mPop.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
		mPop.setAnimationStyle(R.style.safety_intercept_animation);
		TextView tv1 = (TextView) mPopView.findViewById(R.id.PopMenu_bt1);
		TextView tv2 = (TextView) mPopView.findViewById(R.id.PopMenu_bt2);
		TextView tv3 = (TextView) mPopView.findViewById(R.id.PopMenu_bt3);
		tv1.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(mContext, SafetyInterceptSettingActivity.class);
				startActivity(intent);
				mPop.dismiss();
			}
		});
		tv2.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(mContext, SafetyInterceptReportActivity.class);
				startActivity(intent);
				mPop.dismiss();
			}
		});
		tv3.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				mPop.dismiss();
			}
		});
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		if (SafetyInterceptDetailActivity.mIsUninstall) 
		{
			getInfo();
			mAdapter = new InterceptAdapter(mContext, mAppList);
			mListView.setAdapter(mAdapter);
			mNum.setText(mAppList.size() + "");
			mAdapter.notifyDataSetChanged();
		}
	}

	//获取手机内所有应用程序的应用图标，应用名称，包名等
	@SuppressWarnings("static-access")
	public void getInfo() 
	{
		mAppList = new ArrayList<AppInfo>();
		List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) 
		{
			PackageInfo packageInfo = packages.get(i);
			//判断是否为自己安装的软件非系统软件
			if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_SYSTEM) <= 0) 
			{
				String packageName = packageInfo.packageName;
				mAppInfo = new AppInfo();
				StringBuffer buffer = new StringBuffer();
				int count = 0;
				try 
				{
					//获得某个程序的APK路径
					String path = getPackageManager().getApplicationInfo(packageName, 0).sourceDir;
					DexFile dexFile = new DexFile(path);
					Enumeration<String> enu = dexFile.entries();
					while (enu.hasMoreElements()) 
					{
						//获取某个应用的所有包名
						String pm = enu.nextElement();
						for (int j = 0; j < AdManagerUtil.adPackage.size(); j++) 
						{
							if (pm.equals(AdManagerUtil.adPackage.get(j))) 
							{
								buffer.append(AdManagerUtil.adName.get(j)).append("\n");
								count++;
								mAppInfo.adName = buffer.toString();
								mAppInfo.count = count;
							}
						}
					}
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				} 
				if (!buffer.toString().equals("")) 
				{
					mAppInfo.appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
					mAppInfo.packageName = packageInfo.packageName;
					mAppInfo.versionName = packageInfo.versionName;
					mAppInfo.versionCode = packageInfo.versionCode;
					mAppInfo.appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
					mAppList.add(mAppInfo);
				}
			}
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) {
		case R.id.id_title_left_img: 
		{
			finish();
			break;
		}
		case R.id.id_title_right_img:
		{
			if (mPop.isShowing()) 
			{
				mPop.dismiss();
			} 
			else 
			{
				mPop.showAtLocation(v, Gravity.RIGHT | Gravity.TOP, 20, 100);
			}
			break;
		}
		case R.id.ad_main_ll2:
		{
			Intent intent = new Intent(mContext, SafetyInterceptDetectActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		AppInfo info = mAppList.get(position);
		Intent intent = new Intent(mContext, SafetyInterceptDetailActivity.class);
		intent.putExtra("position", position);
		intent.putExtra("adname", info.getAdName());
		intent.putExtra("count", info.getCount());
		startActivity(intent);
	}
}
