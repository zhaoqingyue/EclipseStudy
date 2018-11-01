/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: CloudLoginActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.cloud.activity
 * @Description: 360Cloud登录
 * @author: zhaoqy
 * @date: 2015-12-9 上午11:28:01
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.cloud.activity;

import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CloudLoginActivity extends Activity implements OnClickListener
{
	private Button mLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_360cloud_login);
		
		initView();
		setListener();
	}

	private void initView() 
	{
		mLogin = (Button) findViewById(R.id.id_cloud_login);
	}
	
	private void setListener() 
	{
		mLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_cloud_login:
		{
			Intent intent = new Intent(this, CloudMainActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
		
	}
}
