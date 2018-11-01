/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SoftwareCatelogAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.adapter
 * @Description: 软件分类Adapter
 * @author: zhaoqy
 * @date: 2015-11-25 下午2:22:40
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
import com.zhaoqy.app.demo.oschina.item.SoftwareCatalogList.SoftwareType;

public class SoftwareCatelogAdapter extends BaseAdapter 
{
	private List<SoftwareType> mData;
	private LayoutInflater 	   mContainer;
	private int 			   mResource;
	
	public SoftwareCatelogAdapter(Context context, List<SoftwareType> data, int resource) 
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
			mHolder.name = (TextView)convertView.findViewById(R.id.softwarecatalog_listitem_name);
			convertView.setTag(mHolder);
		}
		else 
		{
			mHolder = (ViewHolder)convertView.getTag();
		}	
		
		//设置文字和图片
		SoftwareType softwareType = mData.get(position);
		mHolder.name.setText(softwareType.name);
		mHolder.name.setTag(softwareType);
		return convertView;
	}
	
	static class ViewHolder
	{
        public TextView name;  
	}  
}
