/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: CloudMainActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.cloud.activity
 * @Description: 360Cloud主页面
 * @author: zhaoqy
 * @date: 2015-12-9 上午11:31:18
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.cloud.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.cloud.adapter.ContentPagerAdapter;
import com.zhaoqy.app.demo.menu.cloud.fragment.MenuFragment;
import com.zhaoqy.app.demo.menu.headline.slidemenu.SlidingMenu;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class CloudMainActivity extends FragmentActivity 
{
	private ViewPager vp_content;
	private TextView tv_find;
	private TextView tv_my_file;
	private SlidingMenu slidingMenu;
	private ImageButton ibtn_trigger;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_360cloud_main);
		findView();
		init();
	}

	private void findView() 
	{
		vp_content = (ViewPager) findViewById(R.id.vp_content);
		tv_find = (TextView) findViewById(R.id.tv_find);
		tv_my_file = (TextView) findViewById(R.id.tv_my_file);
		ibtn_trigger = (ImageButton) findViewById(R.id.ibtn_right_menu);
	}

	private void init() 
	{
		vp_content.setAdapter(new ContentPagerAdapter(getSupportFragmentManager()));
		vp_content.setOnPageChangeListener(new OnPageChangeListener() 
		{
			@Override
			public void onPageSelected(int position) 
			{
				setCurrentPage(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) 
			{
			}

			@Override
			public void onPageScrollStateChanged(int state) 
			{
			}
		});

		ibtn_trigger.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				slidingMenu.toggle();
			}
		});

		slidingMenu = new SlidingMenu(this);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE); 
		slidingMenu.setShadowDrawable(R.drawable.cloud_shadow_right); 
		slidingMenu.setShadowWidth(30); 
		slidingMenu.setBehindOffset(80); 
		slidingMenu.setMode(SlidingMenu.RIGHT); 
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setMenu(R.layout.fragment_360cloud_slidemenu); 
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.menu_frame, new MenuFragment()).commit();
	}

	@Override
	public void onBackPressed() 
	{
		if (slidingMenu != null && slidingMenu.isMenuShowing()) 
		{
			slidingMenu.showContent();
		} 
		else 
		{
			super.onBackPressed();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_MENU) 
		{
			slidingMenu.toggle();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setCurrentPage(int current) 
	{
		if (current == 0) 
		{
			tv_find.setBackgroundResource(R.drawable.cloud_title_menu_current);
			tv_find.setTextColor(getResources().getColor(R.color.blue));
			tv_my_file.setBackgroundResource(R.drawable.cloud_title_menu_bg);
			tv_my_file.setTextColor(getResources().getColor(R.color.grey));
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		} 
		else 
		{
			tv_my_file.setBackgroundResource(R.drawable.cloud_title_menu_current);
			tv_my_file.setTextColor(getResources().getColor(R.color.blue));
			tv_find.setBackgroundResource(R.drawable.cloud_title_menu_bg);
			tv_find.setTextColor(getResources().getColor(R.color.grey));
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}
	}
}
