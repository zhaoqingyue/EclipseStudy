/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaAboutActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 关于oschina
 * @author: zhaoqy
 * @date: 2015-11-17 下午2:21:35
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class OSChinaAboutActivity extends OSChinaBaseActivity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mVersion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oschina_about);
		
		initView();
		initData();
	}

	private void initView() 
	{
		mVersion = (TextView)findViewById(R.id.about_version);
		mBack = (ImageView) findViewById(R.id.about_back);
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		try 
		{ 
			//获取客户端版本信息
        	PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
    		mVersion.setText("版本：" + info.versionName);
        } 
		catch (NameNotFoundException e) 
		{    
			e.printStackTrace(System.err);
		}    
	}

	@Override
	public void onClick(View v) 
	{
		finish();
	}
}
