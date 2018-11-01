/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyClearAutoStartActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 应用自启动
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.adapter.SafetyClearAutostartAdapter;
import com.zhaoqy.app.demo.page.safety.item.Autostart;

public class SafetyClearAutoStartActivity extends Activity implements OnClickListener
{
	static final String BOOT_START_PERMISSION = "android.permission.RECEIVE_BOOT_COMPLETED";
	private SafetyClearAutostartAdapter mAdapter;
	private List<Autostart> mAutostartInfo;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mNum;
	private ListView  mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_clear_auto_start);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mNum = (TextView) findViewById(R.id.clear_autostart_num);
		mListView = (ListView) findViewById(R.id.clear_autostart_listview);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("自动启动的软件");
		mBack.setVisibility(View.VISIBLE);
		
		getautostartInfo();
		mAdapter = new SafetyClearAutostartAdapter(mContext, mAutostartInfo);
		mNum.setText(String.valueOf(mAutostartInfo.size()));
		mListView.setAdapter(mAdapter);
	}
	
	private void getautostartInfo() 
	{
		List<ApplicationInfo> packages = getPackageManager().getInstalledApplications(0);
		mAutostartInfo = new ArrayList<Autostart>(packages.size());
		Iterator<ApplicationInfo> appInfoIterator = packages.iterator();
		while (appInfoIterator.hasNext()) 
		{
			ApplicationInfo app = (ApplicationInfo) appInfoIterator.next();
			//查找安装的package是否有开机启动权限
			if (PackageManager.PERMISSION_GRANTED == getPackageManager().checkPermission(BOOT_START_PERMISSION, app.packageName)) 
			{
				Autostart info = new Autostart();
				info.setApp_name(getPackageManager().getApplicationLabel(app).toString());
				info.setApp_Icon(getPackageManager().getApplicationIcon(app));
				mAutostartInfo.add(info);
			}
		}
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
}
