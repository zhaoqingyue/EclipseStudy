/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: DianpingLoginActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.dianping.activity
 * @Description: 大众点评登录
 * @author: zhaoqy
 * @date: 2015-12-16 下午5:19:57
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.dianping.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zhaoqy.app.demo.R;

public class DianpingLoginActivity extends Activity 
{
	private ImageView mBack;
	private EditText  mPhone;
	private Button    mLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dianping_login);
		
		initView();
		setListener();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.login_back);
		mPhone = (EditText) findViewById(R.id.login_telephone_num);
		mLogin = (Button) findViewById(R.id.login);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
		mPhone.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				if(mPhone.getText().toString().length()>=5)
				{
					mLogin.setEnabled(true);
				}
				else
				{
					mLogin.setEnabled(false);
				}				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) 
			{
			}
			
			@Override
			public void afterTextChanged(Editable s) 
			{
			}
		});
	}
}
