/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MainMessageActivity.java
 * @Prject: DemoKaixin
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 消息
 * @author: zhaoqy
 * @date: 2015-11-11 下午9:09:21
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;

public class MainMessageActivity extends MenuKaixinBaseActivity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private ListView  mDisplay;
	private String[]  mTitles = { "短消息", "系统消息", "评论", "留言", "圈子", "聊天" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_desktop_message);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mDisplay = (ListView) findViewById(R.id.message_display);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("消息中心");
		mDisplay.setAdapter(new MessageAdapter());
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
		case R.id.id_title_right_img:
		{
			break;
		}
		default:
			break;
		}
	}

	private class MessageAdapter extends BaseAdapter 
	{

		public int getCount() 
		{
			return mTitles.length;
		}

		public Object getItem(int position) 
		{
			return mTitles[position];
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_desktop_message, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.message_item_title);
				holder.messageCount = (TextView) convertView.findViewById(R.id.message_item_messagecount);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(mTitles[position]);
			holder.messageCount.setText("0条新");
			return convertView;
		}

		class ViewHolder 
		{
			TextView title;
			TextView messageCount;
		}
	}
}
