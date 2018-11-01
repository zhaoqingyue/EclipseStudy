/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: FriendAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.adapter
 * @Description: 用户粉丝、关注Adapter
 * @author: zhaoqy
 * @date: 2015-11-19 下午3:30:18
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
import com.zhaoqy.app.demo.oschina.item.FriendList.Friend;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

public class FriendAdapter extends BaseAdapter 
{
	private List<Friend>   mData;       
	private LayoutInflater mContainer;    
	private BitmapManager  mBmpManager;
	private int 		   mResource; 

	public FriendAdapter(Context context, List<Friend> data, int resource) 
	{	
		mContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
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
			mHolder.name = (TextView)convertView.findViewById(R.id.friend_listitem_name);
			mHolder.expertise = (TextView)convertView.findViewById(R.id.friend_listitem_expertise);
			mHolder.face = (ImageView)convertView.findViewById(R.id.friend_listitem_userface);
			mHolder.gender = (ImageView)convertView.findViewById(R.id.friend_listitem_gender);
			convertView.setTag(mHolder);
		}
		else 
		{
			mHolder = (ViewHolder)convertView.getTag();
		}	
		
		Friend friend = mData.get(position);
		mHolder.name.setText(friend.getName());
		mHolder.name.setTag(friend);
		mHolder.expertise.setText(friend.getExpertise());
		
		if(friend.getGender() == 1)
		{
			mHolder.gender.setImageResource(R.drawable.oschina_gender_man);
		}
			
		else
		{
			mHolder.gender.setImageResource(R.drawable.oschina_gender_woman);
		}
		
		String faceURL = friend.getFace();
		if(faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL))
		{
			mHolder.face.setImageResource(R.drawable.oschina_dface);
		}
		else
		{
			mBmpManager.loadBitmap(faceURL, mHolder.face);
		}
		mHolder.face.setTag(friend);
		return convertView;
	}

	static class ViewHolder
	{				
        public ImageView face;  
        public ImageView gender;
        public TextView  name;  
        public TextView  expertise;
	}  
}
