/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinChatLogsActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 聊天记录
 * @author: zhaoqy
 * @date: 2015-11-10 下午2:44:21
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.ChatResult;
import com.zhaoqy.app.demo.menu.kaixin.util.PhotoUtil;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;

public class MenuKaixinChatLogsActivity extends MenuKaixinBaseActivity 
{
	private ImageView  mBack;
	private TextView   mTitle;
	private ListView mDisplay;
	private String   mName;   //当前聊天记录的所属用户姓名
	private int      mAvatar; //当前聊天记录的所属用户头像

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_chat_logs);
		findViewById();
		setListener();
		init();
	}

	private void findViewById() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mDisplay = (ListView) findViewById(R.id.chatlogs_display);
		
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("聊天记录");
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
	}

	private void init() 
	{
		//接收当前聊天记录所属用户的姓名和头像
		mName = getIntent().getStringExtra("name");
		mAvatar = getIntent().getIntExtra("avatar", -1);
		//添加适配器
		mDisplay.setAdapter(new ChatLogsAdapter());
	}

	public class ChatLogsAdapter extends BaseAdapter 
	{
		public int getCount() 
		{
			return mApplication.mChatResults.size();
		}

		public Object getItem(int position) 
		{
			return mApplication.mChatResults.get(position);
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
				convertView = LayoutInflater.from(MenuKaixinChatLogsActivity.this).inflate(R.layout.item_menu_kaixin_chat, null);
				holder = new ViewHolder();
				holder.in = (RelativeLayout) convertView.findViewById(R.id.chat_item_in_layout);
				holder.out = (RelativeLayout) convertView.findViewById(R.id.chat_item_out_layout);
				holder.inAvatar = (ImageView) convertView.findViewById(R.id.chat_item_in_avatar);
				holder.outAvatar = (ImageView) convertView.findViewById(R.id.chat_item_out_avatar);
				holder.inName = (TextView) convertView.findViewById(R.id.chat_item_in_name);
				holder.outName = (TextView) convertView.findViewById(R.id.chat_item_out_name);
				holder.inTime = (TextView) convertView.findViewById(R.id.chat_item_in_time);
				holder.outTime = (TextView) convertView.findViewById(R.id.chat_item_out_time);
				holder.inContent = (TextView) convertView.findViewById(R.id.chat_item_in_content);
				holder.outContent = (TextView) convertView.findViewById(R.id.chat_item_out_content);
				convertView.setTag(holder);
			}
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			ChatResult result = mApplication.mChatResults.get(position);
			// 根据消息的类型,显示不同的界面效果,1为用户自己发出的消息,2为用户收到的消息
			switch (result.getType()) 
			{
			case 1:
				holder.in.setVisibility(View.GONE);
				holder.out.setVisibility(View.VISIBLE);
				holder.outAvatar.setImageBitmap(PhotoUtil.toRoundCorner(BitmapFactory.decodeResource(getResources(), R.drawable.menu_kaixin_head_default), 15));
				holder.outName.setText("我");
				holder.outTime.setText(result.getTime());
				holder.outContent.setText(new TextUtil(mApplication).replace(result.getContent()));
				break;
			case 2:
				holder.out.setVisibility(View.GONE);
				holder.in.setVisibility(View.VISIBLE);
				holder.inAvatar.setImageBitmap(mApplication.getAvatar(mAvatar));
				holder.inName.setText(mName);
				holder.inTime.setText(result.getTime());
				holder.inContent.setText(new TextUtil(mApplication).replace(result.getContent()));
				break;
			}
			return convertView;
		}

		class ViewHolder 
		{
			RelativeLayout in;
			RelativeLayout out;
			ImageView inAvatar;
			ImageView outAvatar;
			TextView  inName;
			TextView  outName;
			TextView  inTime;
			TextView  outTime;
			TextView  inContent;
			TextView  outContent;
		}
	}
}
