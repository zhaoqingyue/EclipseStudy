/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinVisitorsActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 最近来访
 * @author: zhaoqy
 * @date: 2015-11-9 下午7:58:49
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.util.ArrayList;
import java.util.List;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.VisitorsResult;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class MenuKaixinVisitorsActivity extends MenuKaixinBaseActivity 
{
	private List<VisitorsResult> mResults;
	private VisitorsAdapter      mAdapter;
	private GridView             mDisplay;
	private ImageView            mBack;
	private TextView             mTitle;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_visitor);
		
		findViewById();
		setListener();
		init();
	}

	private void findViewById() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mDisplay = (GridView) findViewById(R.id.visitors_display);
		
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("最近来访");
	}

	private void setListener() 
	{
		mBack.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//关闭当前界面
				finish();
			}
		});
		mDisplay.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				//获取点击的来访者数据,跳转到好友资料
				VisitorsResult result = mResults.get(position);
				Intent intent = new Intent();
				intent.setClass(MenuKaixinVisitorsActivity.this, MenuKaixinFriendInfoActivity.class);
				intent.putExtra("uid", result.getUid());
				intent.putExtra("name", result.getName());
				intent.putExtra("avatar", result.getAvatar());
				startActivity(intent);
			}
		});
	}

	private void init() 
	{
		//获取查看最近来访用户的ID
		String uid = getIntent().getStringExtra("uid");
		//ID为null时代表当前用户数据
		if (uid == null) 
		{
			mResults = mApplication.mMyVisitorsResults;

		} 
		else 
		{
			if (mApplication.mFriendVisitorsResults.containsKey(uid)) 
			{
				mResults = mApplication.mFriendVisitorsResults.get(uid);
			} 
			else 
			{
				mResults = new ArrayList<VisitorsResult>();
			}
		}
		//添加适配器
		mAdapter = new VisitorsAdapter();
		mDisplay.setAdapter(mAdapter);
	}

	private class VisitorsAdapter extends BaseAdapter 
	{
		public VisitorsAdapter() 
		{
		}

		public int getCount() 
		{
			return mResults.size();
		}

		public Object getItem(int position) 
		{
			return mResults.get(position);
		}

		public long getItemId(int position) 
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ViewHolder holder = null;
			if (convertView == null) 
			{
				convertView = LayoutInflater.from(MenuKaixinVisitorsActivity.this).inflate(R.layout.item_menu_kaixin_visitor, null);
				holder = new ViewHolder();
				holder.avatar = (ImageView) convertView.findViewById(R.id.visitors_item_avatar);
				int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
				LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.width = (mScreenWidth - padding) / 4;
				params.height = (mScreenWidth - padding) / 4;
				holder.avatar.setLayoutParams(params);
				holder.name = (TextView) convertView.findViewById(R.id.visitors_item_name);
				holder.time = (TextView) convertView.findViewById(R.id.visitors_item_time);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			VisitorsResult result = mResults.get(position);
			holder.avatar.setImageBitmap(mApplication.getAvatar(result.getAvatar()));
			holder.name.setText(result.getName());
			holder.time.setText(result.getTime());
			return convertView;
		}

		class ViewHolder 
		{
			ImageView avatar;
			TextView name;
			TextView time;
		}
	}
}
