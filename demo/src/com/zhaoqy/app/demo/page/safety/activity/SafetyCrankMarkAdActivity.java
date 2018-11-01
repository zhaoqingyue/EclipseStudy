/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyCrankMarkAdActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 标记广告推销
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class SafetyCrankMarkAdActivity extends Activity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mAdd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_crank_tabhost_mark_ad);
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mAdd = (TextView) findViewById(R.id.addblacklist);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mAdd.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("广告推销");
		mBack.setVisibility(View.VISIBLE);
		mAdd.setText("标记最近陌生号码");
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
