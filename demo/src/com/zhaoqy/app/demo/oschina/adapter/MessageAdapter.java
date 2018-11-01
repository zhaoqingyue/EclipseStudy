/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MessageAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.adapter
 * @Description: 用户留言Adapter
 * @author: zhaoqy
 * @date: 2015-11-25 下午4:01:21
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.adapter;

import java.util.List;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.helper.BitmapManager;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.Messages;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

public class MessageAdapter extends BaseAdapter 
{
	private List<Messages> mData;
	private LayoutInflater mContainer;
	private BitmapManager  mBmpManager;
	private int 		   mResource;
	
	public MessageAdapter(Context context, List<Messages> data, int resource) 
	{	
		mContainer = LayoutInflater.from(context);
		mResource = resource;
		mData = data;
		mBmpManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.oschina_dface_loading));
	}
	
	public int getCount() 
	{
		return mData.size();
	}

	public Object getItem(int arg0) 
	{
		return null;
	}

	public long getItemId(int arg0) 
	{
		return 0;
	}
	   
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder mHolder = null;
		if (convertView == null) 
		{
			convertView = mContainer.inflate(mResource, null);
			mHolder = new ViewHolder();
			mHolder.userface = (ImageView)convertView.findViewById(R.id.message_listitem_userface);
			mHolder.username = (TextView)convertView.findViewById(R.id.message_listitem_username);
			mHolder.date = (TextView)convertView.findViewById(R.id.message_listitem_date);
			mHolder.messageCount = (TextView)convertView.findViewById(R.id.message_listitem_messageCount);
			mHolder.client= (TextView)convertView.findViewById(R.id.message_listitem_client);
			convertView.setTag(mHolder);
		}
		else 
		{
			mHolder = (ViewHolder)convertView.getTag();
		}
		
		//设置文字和图片
		Messages msg = mData.get(position);
		if(msg.getSenderId() == UserHelper.getLoginUid())
		{
			mHolder.username.setText(UIHelper.parseMessageSpan(msg.getFriendName(), msg.getContent(), "发给 "));
		}
		else
		{
			mHolder.username.setText(UIHelper.parseMessageSpan(msg.getSender(), msg.getContent(), ""));
		}
		mHolder.username.setTag(msg);
		mHolder.date.setText(StringUtils.friendly_time(msg.getPubDate()));
		mHolder.messageCount.setText("共有 "+msg.getMessageCount()+" 条留言");
		
		switch(msg.getAppClient())
		{	
			case Messages.CLIENT_MOBILE:
			{
				mHolder.client.setText("来自:手机");
				break;
			}
			case Messages.CLIENT_ANDROID:
			{
				mHolder.client.setText("来自:Android");
				break;
			}
			case Messages.CLIENT_IPHONE:
			{
				mHolder.client.setText("来自:iPhone");
				break;
			}
			case Messages.CLIENT_WINDOWS_PHONE:
			{
				mHolder.client.setText("来自:Windows Phone");
				break;
			}
			default:
			{
				mHolder.client.setText("");
				break;
			}
		}
		
		if(StringUtils.isEmpty(mHolder.client.getText().toString()))
		{
			mHolder.client.setVisibility(View.GONE);
		}
		else
		{
			mHolder.client.setVisibility(View.VISIBLE);
		}
		
		String faceURL = msg.getFace();
		if(faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL))
		{
			mHolder.userface.setImageResource(R.drawable.oschina_dface);
		}
		else
		{
			mBmpManager.loadBitmap(faceURL, mHolder.userface);
		}
		mHolder.userface.setOnClickListener(faceClickListener);
		mHolder.userface.setTag(msg);
		return convertView;
	}
	
	static class ViewHolder
	{				
		public ImageView userface;
		public TextView  username;
		public TextView  date;
		public TextView  messageCount;
		public TextView  client;
	 }  
	
	private View.OnClickListener faceClickListener = new View.OnClickListener()
	{
		public void onClick(View v) 
		{
			Messages msg = (Messages)v.getTag();
			UIHelper.showUserCenter(v.getContext(), msg.getFriendId(), msg.getFriendName());
		}
	};
}
