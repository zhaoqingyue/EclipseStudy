package com.zhaoqy.app.demo.weixin.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class SettingActivity extends Activity implements OnClickListener 
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mMsgtip;
	private TextView  mUserSafe;
	private TextView  mYinSi;
	private TextView  mCommon;
	private TextView  mAbout;
	private Button    mExit;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weixin_setting);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mUserSafe = (TextView) findViewById(R.id.id_setting_usersafe);
		mMsgtip = (TextView) findViewById(R.id.id_setting_new_msg);
		mYinSi = (TextView) findViewById(R.id.id_setting_yinsi);
		mCommon = (TextView) findViewById(R.id.id_setting_tongyong);
		mAbout = (TextView) findViewById(R.id.id_setting_about);
		mExit = (Button) findViewById(R.id.id_setting_exit);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mExit.setOnClickListener(this);
		mMsgtip.setOnClickListener(this);
		mUserSafe.setOnClickListener(this);
		mYinSi.setOnClickListener(this);
		mCommon.setOnClickListener(this);
		mAbout.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.weixin_me_setting);
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
		case R.id.id_setting_usersafe:
		{
			break;
		}
		case R.id.id_setting_new_msg:
		{
			break;
		}
		case R.id.id_setting_yinsi:
		{
			break;
		}
		case R.id.id_setting_tongyong:
		{
			break;
		}
		case R.id.id_setting_about:
		{
			Intent intent = new Intent();
			intent.setClass(mContext, WebViewActivity.class);
			intent.putExtra("Title", "关于微信");
			intent.putExtra("URL", "https://github.com/motianhuo/wechat");
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.id_setting_exit:
		{
			finish();
			break;
		}
		default:
			break;
		}
	}
}
