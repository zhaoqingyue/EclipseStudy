/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: InterceptNoteAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.adapter
 * @Description: 拦截日志adapter
 * @author: zhaoqy
 * @date: 2015-12-14 下午9:20:38
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.item.AppInfo;

@SuppressLint("SimpleDateFormat")
public class InterceptNoteAdapter extends BaseAdapter 
{
	private ArrayList<AppInfo> mAppList;
	private Context            mContext;
	private AppInfo            mAppInfo;

	public InterceptNoteAdapter(Context context, ArrayList<AppInfo> appList) 
	{
		mContext = context;
		mAppList = appList;
	}

	@Override
	public int getCount() 
	{
		return mAppList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder = null;
		if (convertView == null) 
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_safety_intercept_note, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.ad_note_listview_item_tv1);
			holder.icon = (ImageView) convertView.findViewById(R.id.ad_note_listview_item_icon);
			holder.time = (TextView) convertView.findViewById(R.id.ad_note_listview_item_time);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		mAppInfo = mAppList.get(position);
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm:ss");
		holder.tv1.setText(mAppInfo.getAppName());
		holder.icon.setImageDrawable(mAppInfo.getAppIcon());
		holder.time.setText(sdf.format(System.currentTimeMillis()));
		return convertView;
	}

	class ViewHolder 
	{
		TextView  tv1;
		TextView  time;
		ImageView icon;
	}
}
