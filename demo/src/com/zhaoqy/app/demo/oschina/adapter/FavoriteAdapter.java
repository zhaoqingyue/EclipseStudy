/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: FavoriteAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.adapter
 * @Description: 用户收藏Adapter
 * @author: zhaoqy
 * @date: 2015-11-19 下午9:08:23
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.item.FavoriteList.Favorite;

public class FavoriteAdapter extends BaseAdapter 
{
	private List<Favorite> mData;
	private LayoutInflater mContainer;
	private int 		   mResource;
	
	public FavoriteAdapter(Context context, List<Favorite> data, int resource) 
	{	
		mContainer = LayoutInflater.from(context);	
		mResource = resource;
		mData = data;
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
		ViewHolder  mHolder = null;
		if (convertView == null) 
		{
			convertView = mContainer.inflate(mResource, null);
			mHolder = new ViewHolder();
			mHolder.title = (TextView)convertView.findViewById(R.id.favorite_listitem_title);
			convertView.setTag(mHolder);
		}
		else 
		{
			mHolder = (ViewHolder)convertView.getTag();
		}	
		
		Favorite favorite = mData.get(position);
		mHolder.title.setText(favorite.title);
		mHolder.title.setTag(favorite);
		return convertView;
	}
	
	static class ViewHolder
	{
        public TextView title;  
	}  
}
