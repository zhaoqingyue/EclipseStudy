/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: DianpingCategoryActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.dianping.activity
 * @Description: 大众点评分类页
 * @author: zhaoqy
 * @date: 2015-12-17 上午10:26:48
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.dianping.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.zhaoqy.app.demo.R;

public class DianpingCategoryActivity extends Activity implements OnClickListener
{
	private ImageView    mBack;
	private LinearLayout mItem0;
	private LinearLayout mItem1;
	private LinearLayout mItem2;
	private LinearLayout mItem3;
	private LinearLayout mFilter;
	private ImageView    mArrow0;
	private ImageView    mArrow1;
	private ImageView    mArrow2;
	private ImageView    mArrow3;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dianping_category);
		
		initView();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.detail_activity_home_list1_back);
		mArrow0  = (ImageView) findViewById(R.id.detail_activity_home_list1_0_arrow_fujin);
		mArrow1  = (ImageView) findViewById(R.id.detail_activity_home_list1_0_arrow_meishi);
		mArrow2  = (ImageView) findViewById(R.id.detail_activity_home_list1_0_arrow_zhineng);
		mArrow3  = (ImageView) findViewById(R.id.detail_activity_home_list1_0_arrow_shaixuan);
		mItem0 = (LinearLayout) findViewById(R.id.detail_activity_home_list1_fujin);
		mItem1 = (LinearLayout) findViewById(R.id.detail_activity_home_list1_meishi);
		mItem2 = (LinearLayout) findViewById(R.id.detail_activity_home_list1_zhineng);
		mItem3 = (LinearLayout) findViewById(R.id.detail_activity_home_list1_shaixuan);
		mFilter = (LinearLayout) findViewById(R.id.detail_activity_home_list1_filter1);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mItem0.setOnClickListener(this);
		mItem1.setOnClickListener(this);
		mItem2.setOnClickListener(this);
		mItem3.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.detail_activity_home_list1_back:
		{
			finish();
			break;
		}
		case R.id.detail_activity_home_list1_fujin:
		{
			if(mFilter.getVisibility()==View.GONE)
			{
				mFilter.setVisibility(View.VISIBLE);
				mArrow0.setVisibility(View.VISIBLE);
			}
			else
			{            
				mFilter.setVisibility(View.GONE);
				setAllImageArrowGone();
			}
			break;
		}
		case R.id.detail_activity_home_list1_meishi:
		{
			if(mFilter.getVisibility()==View.GONE)
			{
				mFilter.setVisibility(View.VISIBLE);
				mArrow1.setVisibility(View.VISIBLE);
			}
			else
			{             
				mFilter.setVisibility(View.GONE);
				setAllImageArrowGone();
			}
			break;
		}
		case R.id.detail_activity_home_list1_zhineng:
		{
			if(mFilter.getVisibility()==View.GONE)
			{
				mFilter.setVisibility(View.VISIBLE);
				mArrow2.setVisibility(View.VISIBLE);
			}
			else
			{               
				mFilter.setVisibility(View.GONE);
				setAllImageArrowGone();
			}
			break;
		}
		case R.id.detail_activity_home_list1_shaixuan:
		{
			if(mFilter.getVisibility()==View.GONE)
			{
				mFilter.setVisibility(View.VISIBLE);
				mArrow3.setVisibility(View.VISIBLE);
			}
			else
			{              
				mFilter.setVisibility(View.GONE);
				setAllImageArrowGone();
			}
			break;
		}
		default:
			break;
		}
	}
	
	private void setAllImageArrowGone()
	{
		mArrow0.setVisibility(View.GONE);
		mArrow1.setVisibility(View.GONE);
		mArrow2.setVisibility(View.GONE);
		mArrow3.setVisibility(View.GONE);
	}
}
