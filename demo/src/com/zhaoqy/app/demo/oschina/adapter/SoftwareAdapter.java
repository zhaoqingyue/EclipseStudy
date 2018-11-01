/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SoftwareAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.adapter
 * @Description: 软件Adapter
 * @author: zhaoqy
 * @date: 2015-11-25 下午2:21:03
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.item.SoftwareList.Software;

public class SoftwareAdapter extends BaseAdapter 
{
	private List<Software> mData;
	private LayoutInflater mContainer;
	private int 		   mResource;
	
	public SoftwareAdapter(Context context, List<Software> data,int resource) 
	{	
		mContainer = LayoutInflater.from(context);	
		mResource = resource;
		mData = data;
	}
	
	public int getCount() 
	{
		return mData.size();
	}

	public Object getItem(int arg0) 
	{
		return null;
	}

	public long getItemId(int arg0) 
	{
		return 0;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder mHolder = null;
		if (convertView == null) 
		{
			convertView = mContainer.inflate(mResource, null);
			mHolder = new ViewHolder();
			mHolder.name = (TextView)convertView.findViewById(R.id.software_listitem_name);
			mHolder.description = (TextView)convertView.findViewById(R.id.software_listitem_description);
			convertView.setTag(mHolder);
		}
		else 
		{
			mHolder = (ViewHolder)convertView.getTag();
		}	
		
		//设置文字和图片
		Software software = mData.get(position);
		mHolder.name.setText(software.name);
		mHolder.name.setTag(software);//设置隐藏参数(实体类)
		mHolder.description.setText(software.description);
		return convertView;
	}

	static class ViewHolder
	{
        public TextView name;  
	    public TextView description;
	}  
}
