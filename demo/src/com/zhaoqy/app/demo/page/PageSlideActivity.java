package com.zhaoqy.app.demo.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.dianping.activity.DianpingWelcomeActivity;
import com.zhaoqy.app.demo.page.mi.LaucherMiActivity;
import com.zhaoqy.app.demo.page.mushroom.LauncherMushroomActivity;
import com.zhaoqy.app.demo.page.safety.activity.SafetyMainActivity;
import com.zhaoqy.app.demo.page.sinaweibo.activity.SinaWeiboGuideActivity;
import com.zhaoqy.app.demo.page.viewpager.activity.ViewPagerGridViewSlideActivity;
import com.zhaoqy.app.demo.page.viewpager.activity.ViewPagerScrollListActivity;
import com.zhaoqy.app.demo.page.viewpager.activity.ViewPagerSlide2Activity;
import com.zhaoqy.app.demo.page.viewpager.activity.ViewPagerSlideActivity;
import com.zhaoqy.app.demo.page.vmall.activity.VmallWelcomeActivity;
import com.zhaoqy.app.demo.page.way.activity.LauncherWayActivity;

public class PageSlideActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mItem0;
	private TextView  mItem1;
	private TextView  mItem2;
	private TextView  mItem3;
	private TextView  mItem4;
	private TextView  mItem5;
	private TextView  mItem6;
	private TextView  mItem7;
	private TextView  mItem8;
	private TextView  mItem9;
	private TextView  mItem10;
	private TextView  mItem11;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mItem0 = (TextView) findViewById(R.id.id_page_slide_item0);
		mItem1 = (TextView) findViewById(R.id.id_page_slide_item1);
		mItem2 = (TextView) findViewById(R.id.id_page_slide_item2);
		mItem3 = (TextView) findViewById(R.id.id_page_slide_item3);
		mItem4 = (TextView) findViewById(R.id.id_page_slide_item4);
		mItem5 = (TextView) findViewById(R.id.id_page_slide_item5);
		mItem6 = (TextView) findViewById(R.id.id_page_slide_item6);
		mItem7 = (TextView) findViewById(R.id.id_page_slide_item7);
		mItem8 = (TextView) findViewById(R.id.id_page_slide_item8);
		mItem9 = (TextView) findViewById(R.id.id_page_slide_item9);
		mItem10 = (TextView) findViewById(R.id.id_page_slide_item10);
		mItem11 = (TextView) findViewById(R.id.id_page_slide_item11);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mItem0.setOnClickListener(this);
		mItem1.setOnClickListener(this);
		mItem2.setOnClickListener(this);
		mItem3.setOnClickListener(this);
		mItem4.setOnClickListener(this);
		mItem5.setOnClickListener(this);
		mItem6.setOnClickListener(this);
		mItem7.setOnClickListener(this);
		mItem8.setOnClickListener(this);
		mItem9.setOnClickListener(this);
		mItem10.setOnClickListener(this);
		mItem11.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.main_item4);
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
		case R.id.id_page_slide_item0:
		{
			Intent intent = new Intent(mContext, ViewPagerSlideActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_page_slide_item1:
		{
			Intent intent = new Intent(mContext, ViewPagerGridViewSlideActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_page_slide_item2:
		{
			Intent intent = new Intent(mContext, ViewPagerScrollListActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_page_slide_item3:
		{
			Intent intent = new Intent(mContext, LaucherMiActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_page_slide_item4:
		{
			Intent intent = new Intent(mContext, SafetyMainActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_page_slide_item5:
		{
			Intent intent = new Intent(mContext, LauncherMushroomActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_page_slide_item6:
		{
			Intent intent = new Intent(mContext, LauncherWayActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_page_slide_item7:
		{
			Intent intent = new Intent(mContext, SinaWeiboGuideActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_page_slide_item8:
		{
			Intent intent = new Intent(mContext, DianpingWelcomeActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_page_slide_item9:
		{
			Intent intent = new Intent(mContext, VmallWelcomeActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_page_slide_item10:
		{
			Intent intent = new Intent(mContext, ViewPagerSlide2Activity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_page_slide_item11:
		{
			break;
		}
		default:
			break;
		}
	}
}
