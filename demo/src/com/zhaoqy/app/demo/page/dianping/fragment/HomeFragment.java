/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: HomeFragment.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.dianping.fragment
 * @Description: 大众点评--首页
 * @author: zhaoqy
 * @date: 2015-12-16 下午8:19:14
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.dianping.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.dianping.activity.DianpingCategoryActivity;
import com.zhaoqy.app.demo.page.dianping.activity.DianpingCityListActivity;
import com.zhaoqy.app.demo.page.dianping.activity.DianpingMoreActivity;
import com.zhaoqy.app.demo.page.dianping.adapter.DianpingPagerAdapter;
import com.zhaoqy.app.demo.page.dianping.util.HomeConstant;
import com.zhaoqy.app.demo.page.dianping.util.SharedUtil;


public class HomeFragment extends Fragment implements OnClickListener, OnPageChangeListener
{
	private TextView     mTopCity;
	private ViewPager    mViewPager;   
	private LinearLayout mPoints;
	private View         mView1;       
	private View         mView2;       
	private View         mView3;        
	private List<View>   mViews;  
	private LinearLayout mTipsContent;
	private ImageView    mTips;
	private ImageView    mTipsArrow;
	private TextView     mNavItem0;
	private TextView     mNavItem1;
	private TextView     mNavItem2;
	private TextView     mNavItem3;
	private TextView     mNavItem4;
	private TextView     mNavItem5;
	private TextView     mNavItem6;
	private TextView     mNavItem7;
	private TextView     mNavItem8;
	private TextView     mNavItem9;
	private TextView     mNavItem10;
	private TextView     mNavItem11;
	private TextView     mNavItem12;
	private TextView     mNavItem13;
	private TextView     mNavItem14;
	private TextView     mNavItem15;
	private TextView     mNavItem16;
	private TextView     mNavItem17;
	private TextView     mNavItem18;
	private TextView     mNavItem19;
	private TextView     mNavItem20;
	private TextView     mNavItem21;
	private TextView     mNavItem22;
	private TextView     mNavItem23;
	private String       mCityName;
	private boolean      mDisplayTips = false;
	private int          mCurPosition = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_dianping_home, null);
		initView(view);
		initData();
		initViewPager();
		initPoints();
		setListener();
		return view;
	}

	private void initView(View view) 
	{
		mTopCity = (TextView) view.findViewById(R.id.home_top_city);
		mTips = (ImageView) view.findViewById(R.id.index_home_tip);
		mTipsArrow = (ImageView) view.findViewById(R.id.index_home_tips_arrow);
		mTipsContent = (LinearLayout) view.findViewById(R.id.index_home_tips_content);
		mViewPager = (ViewPager) view.findViewById(R.id.id_dianping_home_viewpager);
		mPoints = (LinearLayout) view.findViewById(R.id.id_dianping_home_points);
	}
	
	private void initData() 
	{
		mTopCity.setText(SharedUtil.getCityName(getActivity()));
	}
	
	private void setListener() 
	{
		mTopCity.setOnClickListener(this);
		mTips.setOnClickListener(this);
		mNavItem0.setOnClickListener(this);
		mNavItem1.setOnClickListener(this);
		mNavItem2.setOnClickListener(this);
		mNavItem3.setOnClickListener(this);
		mNavItem4.setOnClickListener(this);
		mNavItem5.setOnClickListener(this);
		mNavItem6.setOnClickListener(this);
		mNavItem7.setOnClickListener(this);
		mNavItem8.setOnClickListener(this);
		mNavItem9.setOnClickListener(this);
		mNavItem10.setOnClickListener(this);
		mNavItem11.setOnClickListener(this);
		mNavItem12.setOnClickListener(this);
		mNavItem13.setOnClickListener(this);
		mNavItem14.setOnClickListener(this);
		mNavItem15.setOnClickListener(this);
		mNavItem16.setOnClickListener(this);
		mNavItem17.setOnClickListener(this);
		mNavItem18.setOnClickListener(this);
		mNavItem19.setOnClickListener(this);
		mNavItem20.setOnClickListener(this);
		mNavItem21.setOnClickListener(this);
		mNavItem22.setOnClickListener(this);
		mNavItem23.setOnClickListener(this);
	}
	
	private void initViewPager()
	{  
		mViews = new ArrayList<View>();
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		mView1 = inflater.inflate(R.layout.view_dianping_home_category1, null);
		mView2 = inflater.inflate(R.layout.view_dianping_home_category2, null);
		mView3 = inflater.inflate(R.layout.view_dianping_home_category3, null);
		mNavItem0 = (TextView) mView1.findViewById(R.id.id_home_nav_food);
		mNavItem1 = (TextView) mView1.findViewById(R.id.id_home_nav_movie);
		mNavItem2 = (TextView) mView1.findViewById(R.id.id_home_nav_relax);
		mNavItem3 = (TextView) mView1.findViewById(R.id.id_home_nav_beauty);
		mNavItem4 = (TextView) mView1.findViewById(R.id.id_home_nav_group);
		mNavItem5 = (TextView) mView1.findViewById(R.id.id_home_nav_takeaway);
		mNavItem6 = (TextView) mView1.findViewById(R.id.id_home_nav_hotel);
		mNavItem7 = (TextView) mView1.findViewById(R.id.id_home_nav_tour);
		mNavItem8 = (TextView) mView2.findViewById(R.id.id_home_nav_footer);
		mNavItem9 = (TextView) mView2.findViewById(R.id.id_home_nav_ktv);
		mNavItem10 = (TextView) mView2.findViewById(R.id.id_home_nav_sport);
		mNavItem11 = (TextView) mView2.findViewById(R.id.id_home_nav_shop);
		mNavItem12 = (TextView) mView2.findViewById(R.id.id_home_nav_house);
		mNavItem13 = (TextView) mView2.findViewById(R.id.id_home_nav_marry);
		mNavItem14 = (TextView) mView2.findViewById(R.id.id_home_nav_baby);
		mNavItem15 = (TextView) mView2.findViewById(R.id.id_home_nav_fitting);
		mNavItem16 = (TextView) mView3.findViewById(R.id.id_home_nav_car);
		mNavItem17 = (TextView) mView3.findViewById(R.id.id_home_nav_discount);
		mNavItem18 = (TextView) mView3.findViewById(R.id.id_home_nav_show);
		mNavItem19 = (TextView) mView3.findViewById(R.id.id_home_nav_coffee);
		mNavItem20 = (TextView) mView3.findViewById(R.id.id_home_nav_pet);
		mNavItem21 = (TextView) mView3.findViewById(R.id.id_home_nav_viewpoint);
		mNavItem22 = (TextView) mView3.findViewById(R.id.id_home_nav_hospital);
		mNavItem23 = (TextView) mView3.findViewById(R.id.id_home_nav_more);
		mViews.add(mView1);
		mViews.add(mView2);
		mViews.add(mView3);
		mViewPager.setAdapter(new DianpingPagerAdapter(mViews));
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setCurrentItem(0);
	}

	@SuppressWarnings("deprecation")
	private void initPoints() 
	{
    	View view;
    	for (int i=0; i<mViews.size(); i++) 
    	{
			view = new View(getActivity());
			view.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_viewpager_point_bg));
			LayoutParams lp = new LayoutParams(15, 15);
			lp.leftMargin = 20;
			view.setLayoutParams(lp);
			view.setEnabled(false);
			view.setVisibility(View.VISIBLE);
			mPoints.addView(view);
		}
    	mPoints.getChildAt(mCurPosition).setEnabled(true);
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
	public void onPageSelected(int position) 
	{
		mPoints.getChildAt(mCurPosition).setEnabled(false);	    
		mPoints.getChildAt(position % mViews.size()).setEnabled(true); 
		mViewPager.setCurrentItem(position % mViews.size());
		mCurPosition = position  % mViews.size();
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.home_top_city:
		{
			Intent intent = new Intent(getActivity(), DianpingCityListActivity.class);  
			startActivityForResult(intent, HomeConstant.RequestCityCode);  	
			break;
		}
		case R.id.index_home_tip:
		{
			if(mDisplayTips)
			{
				mTipsArrow.setVisibility(View.GONE);
				mTipsContent.setVisibility(View.GONE);
			}
			else
			{
				mTipsArrow.setVisibility(View.VISIBLE);
				mTipsContent.setVisibility(View.VISIBLE);
			}
			mDisplayTips =! mDisplayTips;	
			break;
		}
		case R.id.id_home_nav_food:
		case R.id.id_home_nav_movie:
		case R.id.id_home_nav_relax:
		case R.id.id_home_nav_beauty:
		case R.id.id_home_nav_group:
		case R.id.id_home_nav_takeaway:
		case R.id.id_home_nav_hotel:
		case R.id.id_home_nav_tour:
		case R.id.id_home_nav_footer:
		case R.id.id_home_nav_ktv:
		case R.id.id_home_nav_sport:
		case R.id.id_home_nav_shop:
		case R.id.id_home_nav_house:
		case R.id.id_home_nav_marry:
		case R.id.id_home_nav_baby:
		case R.id.id_home_nav_fitting:
		case R.id.id_home_nav_car:
		case R.id.id_home_nav_discount:
		case R.id.id_home_nav_show:
		case R.id.id_home_nav_coffee:
		case R.id.id_home_nav_pet:
		case R.id.id_home_nav_viewpoint:
		case R.id.id_home_nav_hospital:
		{
			Intent intent = new Intent(getActivity(), DianpingCategoryActivity.class);  
			startActivity(intent);
			break;
		}
		case R.id.id_home_nav_more:
		{
			Intent intent = new Intent(getActivity(), DianpingMoreActivity.class);  
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == HomeConstant.RequestCityCode && resultCode == Activity.RESULT_OK)
		{
			mCityName = data.getStringExtra("cityName");
			mTopCity.setText(mCityName);
		}
	}
}
