/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyPersonalActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.activity
 * @Description: 隐私空间
 * @author: zhaoqy
 * @date: 2015-12-10 下午5:07:00
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SafetyPersonalActivity extends Activity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mTitle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_personal);
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("隐私空间");
		mBack.setVisibility(View.VISIBLE);
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
