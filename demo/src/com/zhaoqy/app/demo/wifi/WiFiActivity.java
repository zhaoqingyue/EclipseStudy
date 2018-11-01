package com.zhaoqy.app.demo.wifi;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.wifi.wifihot.activity.WiFiHotActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class WiFiActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mWiFiHot;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mWiFiHot = (TextView) findViewById(R.id.id_wifi_hot);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mWiFiHot.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.wifi);
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
		case R.id.id_wifi_hot:
		{
			Intent intent = new Intent(mContext, WiFiHotActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
