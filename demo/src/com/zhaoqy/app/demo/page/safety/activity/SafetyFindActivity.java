/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyFindActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 恶意广告拦截(查找系统软件图标并显示)
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import java.util.ArrayList;
import java.util.List;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.adapter.FindAdapter;
import com.zhaoqy.app.demo.page.safety.item.FindAppInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class SafetyFindActivity extends Activity implements OnClickListener
{
	public static ArrayList<FindAppInfo> mAppList;
	private Context     mContext;
	private ImageView   mBack;
	private TextView    mTitle;
	private TextView    mLoadText;
	private Gallery     mGallery;
	private ProgressBar mProgressBar ;
	private boolean     mIsRun = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_find);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mLoadText = (TextView) findViewById(R.id.tv_hint);
		mProgressBar = (ProgressBar) findViewById(R.id.pb_gallery);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("恶意广告拦截");
		mBack.setVisibility(View.VISIBLE);
		getInfo();
		mGallery = (Gallery) findViewById(R.id.gallery1);
		showNum();
		FindAdapter adapter = new FindAdapter(mContext, mAppList);
		mGallery.setAdapter(adapter);
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
	
	@SuppressWarnings("static-access")
	public void getInfo() 
	{
		mAppList = new ArrayList<FindAppInfo>();
		List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
		for (int i = 0,count=packages.size(); i < count; i++) 
		{
			PackageInfo packageInfo = packages.get(i);
			//判断是否为自己安装的软件非系统软件
			if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_SYSTEM) <= 0) 
			{
				FindAppInfo tmpInfo = new FindAppInfo();
				tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
				mAppList.add(tmpInfo);
			}
		}
	}
	
	protected void showNum() 
	{
		if (mAppList == null) 
		{
			return;
		}
		mGallery.setPadding(10, 10, 10, 10);
		mProgressBar.setMax(mAppList.size());
		new Thread(new Runnable() 
		{
			private int progress = 0;
			@Override
			public void run() 
			{
				while (mIsRun) 
				{
					int count = mAppList.size();
					while (progress < count) 
					{
						try 
						{
							Thread.sleep(10);
						}
						catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
						mHandler.sendEmptyMessage(progress++);
						if(progress == count)
						{
							Intent intent = new Intent(mContext, SafetyInterceptActivity.class);
							startActivity(intent);
							mIsRun = false;
							finish();
						}
					}
				}
			}
		}).start();
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() 
	{
		@Override
		public void handleMessage(android.os.Message msg) 
		{
			int what = msg.what;
			mProgressBar.setProgress(what + 1);
			mGallery.setSelection(what);
			mLoadText.setText((what + 1) + "/" + mAppList.size());
		}
	};
}
