/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: InterceptDetectAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.adapter
 * @Description: 通知栏广告检测adapter
 * @author: zhaoqy
 * @date: 2015-12-15 上午10:49:10
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

public class InterceptDetectAdapter extends BaseAdapter 
{
	private ArrayList<AppInfo> mAppList;
	private LayoutInflater     mInflater;
	private AppInfo            mInfo;

	public InterceptDetectAdapter(Context context, ArrayList<AppInfo> appList) 
	{
		super();
		mAppList = appList;
		mInflater = LayoutInflater.from(context);
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
			convertView = mInflater.inflate(R.layout.item_safety_intercept_detect, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.ad_jiance_list_item1_tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.ad_jiance_list_item1_tv2);
			holder.icon = (ImageView) convertView.findViewById(R.id.ad_jiance_list_item1_icon);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		mInfo = mAppList.get(position);
		holder.tv1.setText(mInfo.getAppName());
		holder.tv2.setText("暂未发现广告插件");
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
