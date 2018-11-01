/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SearchAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.adapter
 * @Description: 搜索Adapter
 * @author: zhaoqy
 * @date: 2015-11-25 上午11:34:53
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.item.SearchList.Result;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

public class SearchAdapter extends BaseAdapter 
{
	private List<Result>   listItems;
	private LayoutInflater listContainer;
	private int 		   itemViewResource;
	
	public SearchAdapter(Context context, List<Result> data, int resource) 
	{		
		listContainer = LayoutInflater.from(context);	
		itemViewResource = resource;
		listItems = data;
	}
	
	public int getCount() 
	{
		return listItems.size();
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
			convertView = listContainer.inflate(this.itemViewResource, null);
			mHolder = new ViewHolder();
			mHolder.title = (TextView)convertView.findViewById(R.id.search_listitem_title);
			mHolder.author = (TextView)convertView.findViewById(R.id.search_listitem_author);
			mHolder.date = (TextView)convertView.findViewById(R.id.search_listitem_date);
			mHolder.layout = (LinearLayout)convertView.findViewById(R.id.search_listitem_ll);
			convertView.setTag(mHolder);
		}
		else 
		{
			mHolder = (ViewHolder)convertView.getTag();
		}	
		
		//设置文字和图片
		Result res = listItems.get(position);
		mHolder.title.setText(res.getTitle());
		mHolder.title.setTag(res);
		if(StringUtils.isEmpty(res.getAuthor())) 
		{
			mHolder.layout.setVisibility(LinearLayout.GONE);
		}
		else
		{
			mHolder.layout.setVisibility(LinearLayout.VERTICAL);
			mHolder.author.setText(res.getAuthor());
			mHolder.date.setText(StringUtils.friendly_time(res.getPubDate()));
		}
		return convertView;
	}
	
	static class ViewHolder
	{		
        public TextView title;  
	    public TextView author;
	    public TextView date;  
	    public LinearLayout layout;
	}  
}
