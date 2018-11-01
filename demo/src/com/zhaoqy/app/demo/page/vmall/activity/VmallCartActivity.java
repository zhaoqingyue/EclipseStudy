/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: VmallCartActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.vmall.activity
 * @Description: 华为商城购物车
 * @author: zhaoqy
 * @date: 2015-12-17 下午9:08:27
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.vmall.activity;

import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class VmallCartActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mShop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vmall_cart);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mShop  = (TextView) findViewById(R.id.id_cart_shop);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("购物车");
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mShop.setOnClickListener(this);
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
		case R.id.id_cart_shop:
		{
			Intent intent = new Intent(mContext, VmallMainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		default:
			break;
		}
	}
}
