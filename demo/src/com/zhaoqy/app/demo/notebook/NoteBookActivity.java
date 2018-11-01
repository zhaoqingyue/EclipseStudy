/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: NoteBookActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.notebook
 * @Description: 记事本
 * @author: zhaoqy
 * @date: 2015-12-21 下午5:39:47
 * @version: V1.0
 */

package com.zhaoqy.app.demo.notebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.billnotes.activity.BillLoginActivity;
import com.zhaoqy.app.demo.notebook.calendar.activity.CalendarActivity;
import com.zhaoqy.app.demo.notebook.milletnotes.activity.NotesMainActivity;
import com.zhaoqy.app.demo.notebook.pwdnotes.activity.NotesLoginActivity;
import com.zhaoqy.app.demo.notebook.windnotes.activity.WindWelcomeActivity;

public class NoteBookActivity extends Activity implements OnClickListener 
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
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notebook);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack =  (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mItem0 = (TextView) findViewById(R.id.id_notebook_item0);
		mItem1 = (TextView) findViewById(R.id.id_notebook_item1);
		mItem2 = (TextView) findViewById(R.id.id_notebook_item2);
		mItem3 = (TextView) findViewById(R.id.id_notebook_item3);
		mItem4 = (TextView) findViewById(R.id.id_notebook_item4);
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
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(mContext.getResources().getString(R.string.main_item13));
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
		case R.id.id_notebook_item0:
		{
			Intent intent = new Intent(mContext, NotesMainActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_notebook_item1:
		{
			Intent intent = new Intent(mContext, BillLoginActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_notebook_item2:
		{
			Intent intent = new Intent(mContext, WindWelcomeActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_notebook_item3:
		{
			Intent intent = new Intent(mContext, NotesLoginActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_notebook_item4:
		{
			Intent intent = new Intent(mContext, CalendarActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
