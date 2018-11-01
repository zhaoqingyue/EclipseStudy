package com.zhaoqy.app.demo.menu;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.cloud.activity.CloudSplashActivity;
import com.zhaoqy.app.demo.menu.headline.activity.HeadLineActivity;
import com.zhaoqy.app.demo.menu.kaixin.activity.MenuKaixinGuideActivity;
import com.zhaoqy.app.demo.menu.qq.MenuQQActivity;
import com.zhaoqy.app.demo.menu.translate.MenuTranslateActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mItem0;
	private TextView  mItem1;
	private TextView  mItem2;
	private TextView  mItem3;
	private TextView  mItem4;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mItem0 = (TextView) findViewById(R.id.id_menu_item0);
		mItem1 = (TextView) findViewById(R.id.id_menu_item1);
		mItem2 = (TextView) findViewById(R.id.id_menu_item2);
		mItem3 = (TextView) findViewById(R.id.id_menu_item3);
		mItem4 = (TextView) findViewById(R.id.id_menu_item4);
		
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mItem0.setOnClickListener(this);
		mItem1.setOnClickListener(this);
		mItem2.setOnClickListener(this);
		mItem3.setOnClickListener(this);
		mItem4.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.main_item5);
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
		case R.id.id_menu_item0:
		{
			Intent intent = new Intent(mContext, MenuQQActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item1:
		{
			Intent intent = new Intent(mContext, HeadLineActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item2:
		{
			Intent intent = new Intent(mContext, MenuKaixinGuideActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item3:
		{
			Intent intent = new Intent(mContext, CloudSplashActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item4:
		{
			Intent intent = new Intent(mContext, MenuTranslateActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
