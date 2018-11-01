/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MessageAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.adapter
 * @Description: 用户留言详情Adapter
 * @author: zhaoqy
 * @date: 2015-11-24 下午4:24:47
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
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.helper.BitmapManager;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.Comment;
import com.zhaoqy.app.demo.oschina.util.StringUtils;
import com.zhaoqy.app.demo.oschina.view.LinkView;

public class MessageDetailAdapter extends BaseAdapter 
{
	private List<Comment>  mData;
	private LayoutInflater mContainer;
	private BitmapManager  mBmpManager;
	private int 		   mResource;

	public MessageDetailAdapter(Context context, List<Comment> data,int resource) 
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
			mHolder.userface1 = (ImageView)convertView.findViewById(R.id.messagedetail_listitem_userface1);
			mHolder.userface2 = (ImageView)convertView.findViewById(R.id.messagedetail_listitem_userface2);
			mHolder.username = (LinkView)convertView.findViewById(R.id.messagedetail_listitem_username);
			mHolder.date = (TextView)convertView.findViewById(R.id.messagedetail_listitem_date);
			mHolder.contentll = (LinearLayout)convertView.findViewById(R.id.messagedetail_listitem_contentll);
			mHolder.client= (TextView)convertView.findViewById(R.id.messagedetail_listitem_client);
			convertView.setTag(mHolder);
		}
		else 
		{
			mHolder = (ViewHolder)convertView.getTag();
		}
		
		Comment msg = mData.get(position);
		mHolder.username.setLinkText("<font color='#0e5986'><b>" + msg.getAuthor() + "</b></font>：" + msg.getContent());
		mHolder.username.setTag(msg);
		mHolder.date.setText(StringUtils.friendly_time(msg.getPubDate()));
		
		switch(msg.getAppClient())
		{	
			case Comment.CLIENT_MOBILE:
			{
				mHolder.client.setText("来自:手机");
				break;
			}
			case Comment.CLIENT_ANDROID:
			{
				mHolder.client.setText("来自:Android");
				break;
			}
			case Comment.CLIENT_IPHONE:
			{
				mHolder.client.setText("来自:iPhone");
				break;
			}	
			case Comment.CLIENT_WINDOWS_PHONE:
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
		//发留言者是我
		if(msg.getAuthorId() == UserHelper.getLoginUid())
		{
			if(faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL))
			{
				mHolder.userface2.setImageResource(R.drawable.oschina_dface);
			}
			else
			{
				mBmpManager.loadBitmap(faceURL, mHolder.userface2);
			}
			mHolder.userface2.setOnClickListener(faceClickListener);
			mHolder.userface2.setTag(msg);
			mHolder.userface2.setVisibility(ImageView.VISIBLE);
			mHolder.userface1.setVisibility(ImageView.GONE);
			mHolder.contentll.setBackgroundResource(R.drawable.oschina_review_bg_right);
		}
		else
		{
			if(faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL))
			{
				mHolder.userface1.setImageResource(R.drawable.oschina_dface);
			}
			else
			{
				mBmpManager.loadBitmap(faceURL, mHolder.userface1);
			}
			mHolder.userface1.setOnClickListener(faceClickListener);
			mHolder.userface1.setTag(msg);
			mHolder.userface1.setVisibility(ImageView.VISIBLE);
			mHolder.userface2.setVisibility(ImageView.GONE);
			mHolder.contentll.setBackgroundResource(R.drawable.oschina_review_bg_left);
		}
		return convertView;
	}
	
	static class ViewHolder
	{	
		public LinearLayout contentll;
		public ImageView userface1;
		public ImageView userface2;
		public LinkView  username;  
	    public TextView  date;  
	    public TextView  client;
	}  
	
	private View.OnClickListener faceClickListener = new View.OnClickListener()
	{
		public void onClick(View v) 
		{
			Comment msg = (Comment)v.getTag();
			UIHelper.showUserCenter(v.getContext(), msg.getAuthorId(), msg.getAuthor());
		}
	};
}
