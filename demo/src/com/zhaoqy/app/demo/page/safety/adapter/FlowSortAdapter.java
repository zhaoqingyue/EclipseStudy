/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: FlowSortAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.adapter
 * @Description: 流量排行榜
 * @author: zhaoqy
 * @date: 2015-12-14 下午3:10:20
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.adapter;

import java.util.List;
import android.content.Context;
import android.net.TrafficStats;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.item.TrafficInfo;
import com.zhaoqy.app.demo.page.safety.util.TrafficDataUtil;

public class FlowSortAdapter extends BaseAdapter 
{
	private LayoutInflater    mInflater;
	private List<TrafficInfo> mTrafficInfo;
	
	public FlowSortAdapter(Context context, List<TrafficInfo> trafficInfo_list)
	{
		mInflater = LayoutInflater.from(context);
		mTrafficInfo = trafficInfo_list;
	}
	@Override
	public int getCount() 
	{
		return mTrafficInfo.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return mTrafficInfo.get(position);
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
		TrafficInfo info = mTrafficInfo.get(position);
		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_safety_flow_sort, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.flow_sort_app_icon);
			holder.appName = (TextView) convertView.findViewById(R.id.flow_sort_app_name);
			holder.upData = (TextView) convertView.findViewById(R.id.flow_sort_updata);
			holder.downData = (TextView) convertView.findViewById(R.id.flow_sort_downdata);
			holder.allData = (TextView) convertView.findViewById(R.id.flow_sort_alldata);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.icon.setImageDrawable(info.getIcon());
		holder.appName.setText(info.getName());
		//上传的数据
		final long totalTx = TrafficStats.getUidTxBytes(info.getUid());
		//接收的数据
		final long totalRx = TrafficStats.getUidRxBytes(info.getUid());
		holder.upData.setText(TrafficDataUtil.getTrafficData(totalTx));
		holder.downData.setText(TrafficDataUtil.getTrafficData(totalRx));
		long all = totalTx+totalRx;
		holder.allData.setText(TrafficDataUtil.getTrafficData(all));
		return convertView;
	}
	
   class ViewHolder
   {
	   ImageView icon;
	   TextView  appName;
	   TextView  upData;
	   TextView  downData;
	   TextView  allData;
   } 
}
