/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: FlowFirewallAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.adapter
 * @Description: 联网防火墙adapter
 * @author: zhaoqy
 * @date: 2015-12-14 下午2:43:26
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.adapter;

import java.util.List;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.net.TrafficStats;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.util.TrafficDataUtil;

public class FlowFirewallAdapter extends BaseAdapter 
{
	private LayoutInflater    mInflater;
	private List<ResolveInfo> mListInfo;
	private Context           mContext;
	
	public FlowFirewallAdapter(Context context, List<ResolveInfo> listInfo)
	{
		mInflater = LayoutInflater.from(context);
		mListInfo = listInfo;
		mContext = context;
	}
	@Override
	public int getCount() 
	{
		return mListInfo.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return mListInfo.get(position);
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
		if(null == convertView)
		{
			convertView = mInflater.inflate(R.layout.item_safety_flow_firewall, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.flow_firewall_app_icon);
			holder.appName = (TextView) convertView.findViewById(R.id.flow_firewall_app_name);
			holder.allData = (TextView) convertView.findViewById(R.id.flow_firewall_alldata);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		final ResolveInfo info = mListInfo.get(position);
		holder.icon.setImageDrawable(info.loadIcon(mContext.getPackageManager()));
		holder.appName.setText(info.loadLabel(mContext.getPackageManager()));
		final long totalTx = TrafficStats.getUidTxBytes(info.activityInfo.applicationInfo.uid);
		final long totalRx = TrafficStats.getUidRxBytes(info.activityInfo.applicationInfo.uid);
		long all = totalTx+totalRx;
		holder.allData.setText(TrafficDataUtil.getTrafficData(all));
		return convertView;
	}	
	
    class ViewHolder
    {
    	ImageView icon;	
    	TextView  appName;
    	TextView  allData;
    } 
}
