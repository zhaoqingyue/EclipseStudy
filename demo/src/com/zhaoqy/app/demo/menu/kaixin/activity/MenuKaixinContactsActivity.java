/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinContactsActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 手机通讯录类(暂时只存在界面,没有任何数据)
 * @author: zhaoqy
 * @date: 2015-11-9 下午7:42:21
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class MenuKaixinContactsActivity extends Activity implements OnClickListener
{
	private ImageView  mBack;
	private TextView   mTitle;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_contacts);
		
		initView();
		setListener();
		initData();
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
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("手机联系人");
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
