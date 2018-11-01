/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.adapter
 * @Description: 安全卫士首页adapter
 * @author: zhaoqy
 * @date: 2015-12-10 下午4:09:34
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.item.SafetyItem;

public class SafetyAdapter extends BaseAdapter 
{
	private LayoutInflater   mInflater;
	private List<SafetyItem> mList;
	private boolean isChanged = false; //是否变化
	private boolean ShowItem = false;  //显示item
	private int holdPosition;
	
	public SafetyAdapter(Context context, List<SafetyItem> list)  
	{  
		mInflater = LayoutInflater.from(context);
		mList = list;
	}  

	@Override
	public int getCount() 
	{
		return mList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		convertView = mInflater.inflate(R.layout.item_safety_main, null);
		TextView tv = (TextView) convertView.findViewById(R.id.main_gridview_item_name);
		ImageView iv = (ImageView) convertView.findViewById(R.id.main_gridview_src);
		tv.setText(mList.get(position).getName());
		iv.setBackgroundResource(mList.get(position).getId());
		
		if(isChanged)
		{
			if(position == holdPosition)
			{
				if(!ShowItem)
				{
					convertView.setVisibility(View.VISIBLE);
				}
			}
		}
		return convertView;
	}

	public void exchange(int startPosition, int endPosition)
	{
		Object start_obj = getItem(startPosition);
		holdPosition = endPosition;
		if(startPosition < endPosition)
		{
			mList.add(endPosition+1,(SafetyItem)start_obj);
			mList.remove(startPosition);
		}
		else
		{
			mList.add(endPosition, (SafetyItem)start_obj);
			mList.remove(startPosition+1);
		}
		isChanged = true;
		notifyDataSetChanged();
	}

	public void showDropItem(boolean b) 
	{
		this.ShowItem = b;
	}
}
