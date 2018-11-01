package com.zhaoqy.app.demo.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.login.email.activity.LoginEmailActivity;
import com.zhaoqy.app.demo.login.kaixin.LoginKaixinActivity;
import com.zhaoqy.app.demo.login.qq.LoginQQActivity;
import com.zhaoqy.app.demo.login.weixin.LoginWeixinActivity;

public class LoginActivity extends Activity implements OnClickListener 
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mItem0;
	private TextView  mItem1;
	private TextView  mItem2;
	private TextView  mItem3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack =  (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mItem0 = (TextView) findViewById(R.id.id_login_item0);
		mItem1 = (TextView) findViewById(R.id.id_login_item1);
		mItem2 = (TextView) findViewById(R.id.id_login_item2);
		mItem3 = (TextView) findViewById(R.id.id_login_item3);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mItem0.setOnClickListener(this);
		mItem1.setOnClickListener(this);
		mItem2.setOnClickListener(this);
		mItem3.setOnClickListener(this);
	}
	
	private void initData()
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(mContext.getResources().getString(R.string.login));
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
		case R.id.id_login_item0:
		{
			Intent intent = new Intent(mContext, LoginWeixinActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_login_item1:
		{
			Intent intent = new Intent(mContext, LoginQQActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_login_item2:
		{
			Intent intent = new Intent(mContext, LoginKaixinActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_login_item3:
		{
			Intent intent = new Intent(mContext, LoginEmailActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
