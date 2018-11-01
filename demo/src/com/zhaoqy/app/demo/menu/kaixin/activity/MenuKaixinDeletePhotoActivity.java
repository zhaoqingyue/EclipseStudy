/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinDeletePhotoActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 移出图片类
 * @author: zhaoqy
 * @date: 2015-11-6 下午2:57:57
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.util.PhotoAnimations;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuKaixinDeletePhotoActivity extends MenuKaixinBaseActivity 
{
	private RelativeLayout mTitle;
	private ImageView      mBack;
	private TextView       mDelete;
	private ImageView      mDisplay;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_delete_photo);
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mTitle = (RelativeLayout) findViewById(R.id.id_menu_kaixin_delete_photo_title);
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mDelete = (TextView) findViewById(R.id.id_title_right_text);
		mDisplay = (ImageView) findViewById(R.id.id_menu_kaixin_delete_photo_display);
		
		mBack.setVisibility(View.VISIBLE);
		mDelete.setVisibility(View.VISIBLE);
		mDelete.setText("移出照片");
	}

	private void setListener() 
	{
		mBack.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//关闭当前界面,并返回数据
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		mDelete.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//关闭当前界面,并返回数据
				setResult(RESULT_OK);
				finish();
			}
		});
		mDisplay.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//判断标题栏是否显示,显示则隐藏,隐藏则显示
				if (mTitle.isShown()) 
				{
					PhotoAnimations.startTopOutAnimation(mTitle);
				} 
				else 
				{
					PhotoAnimations.startTopInAnimation(mTitle);
				}
			}
		});
	}

	private void initData() 
	{
		//获取图片的路径
		String path = getIntent().getStringExtra("path");
		//获取图片
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		//显示图片
		mDisplay.setImageBitmap(bitmap);
	}

	public void onBackPressed() 
	{
		//关闭当前界面,并返回数据
		setResult(RESULT_CANCELED);
		finish();
	}
}
