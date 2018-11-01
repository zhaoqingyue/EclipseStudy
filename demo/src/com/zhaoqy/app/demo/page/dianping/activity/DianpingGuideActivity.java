/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: DianpingGuideActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.dianping.activity
 * @Description: 大众点评引导页
 * @author: zhaoqy
 * @date: 2015-12-16 下午3:06:43
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.dianping.activity;

import java.util.ArrayList;
import java.util.List;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.dianping.adapter.DianpingPagerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class DianpingGuideActivity extends Activity implements OnClickListener, OnPageChangeListener
{
	private Context    mContext;
	private ViewPager  mPager;
	private ImageView  mSkip;
	private List<View> mList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dianping_guide);
		mContext = this;

		initView();
		initViewPager();
		setListener();
	}

	private void initView() 
	{
		mPager = (ViewPager) findViewById(R.id.dianping_guide_viewpager);
		mSkip = (ImageView) findViewById(R.id.dianping_guide_skip);
	}

	private void initViewPager() 
	{
		mList = new ArrayList<View>();
		ImageView iv1 = new ImageView(mContext);
		iv1.setImageResource(R.drawable.dianping_guide_01);
		mList.add(iv1);
		ImageView iv2 = new ImageView(mContext);
		iv2.setImageResource(R.drawable.dianping_guide_02);
		mList.add(iv2);
		ImageView iv3 = new ImageView(mContext);
		iv3.setImageResource(R.drawable.dianping_guide_03);
		mList.add(iv3);
		mPager.setAdapter(new DianpingPagerAdapter(mList));
	}
	
	private void setListener() 
	{
		mPager.setOnPageChangeListener(this);
		mSkip.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		Intent intent = new Intent(mContext, DianpingMainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) 
	{
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) 
	{
	}

	@Override
	public void onPageSelected(int arg0) 
	{
		if(arg0 == 2)
		{
			mSkip.setVisibility(View.VISIBLE);
		}
		else
		{
			mSkip.setVisibility(View.GONE);
		}
	}
}
