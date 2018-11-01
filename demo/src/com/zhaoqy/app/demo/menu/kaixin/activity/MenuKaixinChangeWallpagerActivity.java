/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinChangeWallpagerActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 修改壁纸
 * @author: zhaoqy
 * @date: 2015-11-9 下午5:40:19
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import com.zhaoqy.app.demo.R;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class MenuKaixinChangeWallpagerActivity extends MenuKaixinBaseActivity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mTitle;
	private GridView  mDisplay;
	private WallpagerAdapter mAdapter;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_changer_wallpager);
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mDisplay = (GridView) findViewById(R.id.changewallpager_display);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("更换背景");
		mAdapter = new WallpagerAdapter();
		mDisplay.setAdapter(mAdapter);
	}
	
	@Override
	public void onClick(View v) 
	{
		//关闭当前界面,并返回更新信息
		setResult(RESULT_OK);
		finish();
	}

	private class WallpagerAdapter extends BaseAdapter 
	{
		public int getCount() 
		{
			return mApplication.mWallpagersName.length;
		}

		public Object getItem(int position) 
		{
			return mApplication.mWallpagersName[position];
		}

		public long getItemId(int position) 
		{
			return position;
		}

		@SuppressWarnings("deprecation")
		public View getView(final int position, View convertView, ViewGroup parent) 
		{
			ImageButton wallpager = null;
			if (convertView == null) 
			{
				wallpager = new ImageButton(MenuKaixinChangeWallpagerActivity.this);
				//设置显示图片大小为屏幕的宽度的1/3
				LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.width = (mScreenWidth - 30) / 3;
				params.height = (mScreenWidth - 30) / 3;
				wallpager.setLayoutParams(params);
			} 
			else 
			{
				wallpager = (ImageButton) convertView;
			}
			//查看当前图片是否选择,如果显示则显示选中状态
			if (mApplication.mWallpagerPosition == position) 
			{
				wallpager.setImageResource(R.drawable.menu_kaixin_change_bg);
			} 
			else 
			{
				wallpager.setImageBitmap(null);
			}
			//获取壁纸
			BitmapDrawable drawable = new BitmapDrawable(mApplication.getWallpager(position));
			//添加壁纸
			wallpager.setBackgroundDrawable(drawable);
			wallpager.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					//设置当前项为选中项并更新界面
					mApplication.mWallpagerPosition = position;
					notifyDataSetChanged();
				}
			});
			return wallpager;
		}
	}

	public void onBackPressed() 
	{
		//关闭当前界面,并返回更新信息
		setResult(RESULT_OK);
		finish();
	}
}
