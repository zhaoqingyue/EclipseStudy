/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyClearMemoryAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.adapter
 * @Description: 清除内存adapter
 * @author: zhaoqy
 * @date: 2015-12-10 下午7:50:38
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.item.ProcessInfo;
import com.zhaoqy.app.demo.page.safety.util.TrafficDataUtil;

public class SafetyClearMemoryAdapter extends BaseAdapter 
{
	public List<ProcessInfo> mlistAppInfo;	
	public LayoutInflater    mInfater;
	
	public SafetyClearMemoryAdapter(Context context,  List<ProcessInfo> apps)
	{
		mInfater = LayoutInflater.from(context);
		mlistAppInfo = apps ;
	}
	
	@Override
	public int getCount() 
	{
		return mlistAppInfo.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return mlistAppInfo.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder = null;
		if (convertView == null ) 
		{
			convertView = mInfater.inflate(R.layout.item_safety_clear_memory, null);
			holder = new ViewHolder();
			holder.mAppIcon = (ImageView) convertView.findViewById(R.id.clear_memory_icon);
			holder.mAppLabel = (TextView) convertView.findViewById(R.id.clear_memory_app_name);
			holder.mMemSize = (TextView) convertView.findViewById(R.id.clear_memoryitem_memory);
			holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.clear_memory_cb);
			convertView.setTag(holder);
		} 
		else
		{
			holder = (ViewHolder) convertView.getTag() ;
		}
		final ProcessInfo appInfo = (ProcessInfo) getItem(position);
		holder.mAppIcon.setImageDrawable(appInfo.getAppIcon());
		holder.mAppLabel.setText(appInfo.getAppLabel());
		holder.mMemSize.setText(TrafficDataUtil.getMemoryData(appInfo.getMemSize()));
		return convertView;
	}

	class ViewHolder 
	{
		ImageView mAppIcon;
		TextView  mAppLabel;
        TextView  mMemSize;
        CheckBox  mCheckBox;
	}
}
