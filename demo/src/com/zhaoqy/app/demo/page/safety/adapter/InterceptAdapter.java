/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: InterceptAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.adapter
 * @Description: 广告拦截adapter
 * @author: zhaoqy
 * @date: 2015-12-14 下午8:24:31
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.item.AppInfo;

public class InterceptAdapter extends BaseAdapter 
{
	private ArrayList<AppInfo> mAppList;
	private Context            mContext;
	private AppInfo            mInfo;

	public InterceptAdapter(Context context, ArrayList<AppInfo> appList) 
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
		return position;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_safety_intercept_ad, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.ad_main_listview_item_tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.ad_main_listview_item_tv2);
			holder.icon = (ImageView) convertView.findViewById(R.id.ad_main_listview_item_icon);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		mInfo = mAppList.get(position);
		holder.tv1.setText(mInfo.getAppName());
		System.out.println(mInfo.getAppName());
		holder.tv2.setText("安全软件");
		holder.icon.setImageDrawable(mInfo.getAppIcon());
		return convertView;
	}

	class ViewHolder 
	{
		TextView  tv1;
		TextView  tv2;
		ImageView icon;
	}
}
