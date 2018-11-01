/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyClearAutostartAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.adapter
 * @Description: 应用自动启动
 * @author: zhaoqy
 * @date: 2015-12-10 下午9:16:21
 * @version: V1.0
 */


package com.zhaoqy.app.demo.page.safety.adapter;

import java.util.List;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.item.Autostart;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SafetyClearAutostartAdapter extends BaseAdapter 
{
	private List<Autostart> mAppInfoList;	
	private LayoutInflater  mInfater;
	
	public SafetyClearAutostartAdapter(Context context, List<Autostart> apps)
	{
		mInfater = LayoutInflater.from(context);
		mAppInfoList = apps ;
	}
	
	@Override
	public int getCount() 
	{
		return mAppInfoList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return mAppInfoList.get(position);
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
		if (convertView == null ) 
		{
			convertView = mInfater.inflate(R.layout.item_safety_clear_autostart, null);
			holder = new ViewHolder();
			holder.appIcon = (ImageView) convertView.findViewById(R.id.clear_autostart_icon);
			holder.tvAppLabel = (TextView) convertView.findViewById(R.id.clear_autostart_app_name);
			holder.tvAutostart = (TextView) convertView.findViewById(R.id.clear_autostart_autostart);
			holder.rl_cb = (RelativeLayout) convertView.findViewById(R.id.clear_autostart_rl_cb);
			holder.cb = (CheckBox) convertView.findViewById(R.id.clear_autostart_cb);
			convertView.setTag(holder);
		} 
		else
		{
			holder = (ViewHolder) convertView.getTag() ;
		}
		final Autostart appInfo = (Autostart) getItem(position);
		holder.appIcon.setImageDrawable(appInfo.getApp_Icon());
		holder.tvAppLabel.setText(appInfo.getApp_name());
		final CheckBox checkBox = holder.getCb();
		holder.rl_cb.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				if(checkBox.isChecked())
				{
					checkBox.setChecked(false);
					appInfo.setChecked(false);				
				}
				else
				{
					checkBox.setChecked(true);
					appInfo.setChecked(true);
				}
			}
		});
		return convertView;
	}

	class ViewHolder 
	{
		ImageView appIcon;
		TextView  tvAppLabel;
        TextView  tvAutostart;
        CheckBox  cb;
        RelativeLayout rl_cb;
        
        public CheckBox getCb() 
        {
			return cb;
		}
        
		public void setCb(CheckBox cb) 
		{
			this.cb = cb;
		}
	}
}
