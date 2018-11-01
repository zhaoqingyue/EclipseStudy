/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: InterceptReportAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.adapter
 * @Description: 举报adapter
 * @author: zhaoqy
 * @date: 2015-12-15 上午9:36:30
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
import com.zhaoqy.app.demo.page.safety.item.ReportInfo;

public class InterceptReportAdapter extends BaseAdapter 
{
	private ArrayList<ReportInfo> mInfoList;
	private Context               mContext;
	private ReportInfo            mInfo;

	public InterceptReportAdapter(Context context, ArrayList<ReportInfo> infoList) 
	{
		mContext = context;
		mInfoList = infoList;
	}

	@Override
	public int getCount() 
	{
		return mInfoList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return mInfoList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		convertView = LayoutInflater.from(mContext).inflate(R.layout.item_safety_intercept_report, null);
		TextView tv1 = (TextView) convertView.findViewById(R.id.ad_jubao_list_item_tv1);
		ImageView icon = (ImageView) convertView.findViewById(R.id.ad_jubao_list_item_icon);
		ImageView checkicon = (ImageView) convertView.findViewById(R.id.ad_jubao_list_item_checked);
		mInfo = mInfoList.get(position);
		tv1.setText(mInfo.getAppName());
		icon.setImageDrawable(mInfo.getAppIcon());
		checkicon.setImageDrawable(mInfo.getIcon());
		return convertView;
	}

	class ViewHolder 
	{
		TextView  tv1;
		ImageView icon;
	}
}
