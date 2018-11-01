package com.zhaoqy.app.demo.weixin.adapter;

import com.zhaoqy.app.demo.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FriendCircleAdpter extends BaseAdapter
{
	private Context mContext;
	
	public FriendCircleAdpter(Context context) 
	{
		mContext = context;
	}
	
	@Override
	public int getCount() 
	{
		return 10;
	}

	@Override
	public Object getItem(int position) 
	{
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null) 
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.view_friend_circle_item, parent, false);
		}
		return convertView;
	}
}
