/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyMainActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo
 * @Description: 360安全卫士主页
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:24:25
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.adapter.SafetyAdapter;
import com.zhaoqy.app.demo.page.safety.adapter.SafetyPagerAdapter;
import com.zhaoqy.app.demo.page.safety.util.Common;
import com.zhaoqy.app.demo.page.safety.view.DragGrid;

public class SafetyMainActivity extends Activity implements OnItemClickListener, OnClickListener, OnPageChangeListener
{
	private SafetyPagerAdapter mPagerAdapter;
	private SafetyAdapter mGridAdapdter1;
	private SafetyAdapter mGridAdapdter2;
	private List<View>    mViewList;
	private SlidingMenu   mSlidingMenu;
	private Context   mContext;
	private ViewPager mViewPager;
	private View      mView1;
	private View      mView2;
	private View      mShadow;
	private DragGrid  mDragGrid1;
	private DragGrid  mDragGrid2;
	private ImageView mOption;
	private ImageView mPageNum;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_main);
		mContext = this;
		
		initView();
		initData();
		initSlidingMenu();
		setListener();
	}

	private void initView() 
	{
		mViewPager = (ViewPager) findViewById(R.id.act_main_viewpager);
		mOption = (ImageView) findViewById(R.id.act_main_option);
		mPageNum = (ImageView) findViewById(R.id.act_main_pagenum);
		mShadow = (View) findViewById(R.id.act_main_shadow);
		mView1 = LayoutInflater.from(mContext).inflate(R.layout.view_safety_main, null);
		mView2 = LayoutInflater.from(mContext).inflate(R.layout.view_safety_main, null);
		mDragGrid1 = (DragGrid) mView1.findViewById(R.id.id_safety_main_gridview);
		mDragGrid2 = (DragGrid) mView2.findViewById(R.id.id_safety_main_gridview);
	}
	
	private void setListener() 
	{
		mViewPager.setOnPageChangeListener(this);
		mOption.setOnClickListener(this);
		mPageNum.setOnClickListener(this);
		mDragGrid1.setOnItemClickListener(this);
		mDragGrid2.setOnItemClickListener(this);
	}
	
	private void initData() 
	{
		mViewList = new ArrayList<View>();
		mGridAdapdter1 = new SafetyAdapter(mContext, Common.getDiskList1());
		mGridAdapdter2 = new SafetyAdapter(mContext, Common.getDiskList2());
		mDragGrid1.setAdapter(mGridAdapdter1);
		mDragGrid2.setAdapter(mGridAdapdter2);
		mViewList.add(mView1);
		mViewList.add(mView2);
		mPagerAdapter = new SafetyPagerAdapter(mViewList);
		mViewPager.setAdapter(mPagerAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		TextView name = (TextView) arg1.findViewById(R.id.main_gridview_item_name);
		String text = name.getText().toString();
		
		if (text.equals("手机清理")) 
		{
			Intent intent = new Intent(mContext, SafetyClearActivity.class);
			startActivity(intent);
		} 
		else if (text.equals("骚扰拦截")) 
		{
			Intent intent = new Intent(mContext, SafetyCrankActivity.class);
			startActivity(intent);
		}
		else if (text.equals("隐私空间")) 
		{
			Intent intent = new Intent(mContext, SafetyPersonalActivity.class);
			startActivity(intent);
		} 
		else if (text.equals("节电管理")) 
		{
			Intent intent = new Intent(mContext, SafetyBatteryActivity.class);
			startActivity(intent);
		} 
		else if (text.equals("流量监控")) 
		{
			Intent intent = new Intent(mContext, SafetyFlowActivity.class);
			startActivity(intent);
		} 
		else if (text.equals("安全二维码")) 
		{
			Intent intent = new Intent(mContext, SafetyQrCodeActivity.class);
			startActivity(intent);
		} 
		else if (text.equals("恶意广告拦截")) 
		{
			Intent intent = new Intent(mContext, SafetyFindActivity.class);
			startActivity(intent);
		}
	}

	private void initSlidingMenu() 
	{
		mSlidingMenu = new SlidingMenu(this);
		mSlidingMenu.setMode(SlidingMenu.RIGHT);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		mSlidingMenu.setBehindWidth(120);
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		mSlidingMenu.setMenu(R.layout.view_safety_main_slidemenu);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.act_main_option:
		{
			mSlidingMenu.showMenu();
			mShadow.setVisibility(View.VISIBLE);
			mOption.setBackgroundDrawable(getResources().getDrawable(R.drawable.safety_main_menu_negative_normal));
			mSlidingMenu.setOnCloseListener(new OnCloseListener() 
			{
				@Override
				public void onClose() 
				{
					mShadow.setVisibility(View.GONE);
					mOption.setBackgroundDrawable(getResources().getDrawable(R.drawable.safety_main_menu_normal));
				}
			});
			break;
		}
		case R.id.act_main_pagenum:
		{
			if (mViewPager.getCurrentItem() == 0) 
			{
				mViewPager.setCurrentItem(1);
			} 
			else 
			{
				mViewPager.setCurrentItem(0);
			}
			break;
		}
		default:
			break;
		}
	}

	private void setpage() 
	{
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.safety_page_in);
		animation.setAnimationListener(new AnimationListener() 
		{
			@Override
			public void onAnimationStart(Animation animation) 
			{
			}

			@Override
			public void onAnimationRepeat(Animation animation) 
			{
			}

			@Override
			public void onAnimationEnd(Animation animation) 
			{
				if (mViewPager.getCurrentItem() == 0) 
				{
					mPageNum.setBackgroundResource(R.drawable.safety_main_page_one);
				} 
				else 
				{
					mPageNum.setBackgroundResource(R.drawable.safety_main_page_two);
				}
				Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.safety_page_to);
				mPageNum.startAnimation(animation2);
			}
		});
		mPageNum.startAnimation(animation);
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
		setpage();
	}
}
