/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: NewsAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.adapter
 * @Description: 新闻资讯Adapter
 * @author: zhaoqy
 * @date: 2015-11-25 下午5:21:23
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.item.News;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

public class NewsAdapter extends BaseAdapter 
{
	private List<News> 	   mData;
	private LayoutInflater mContainer;
	private int 		   mResource;

	public NewsAdapter(Context context, List<News> data,int resource) 
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
			mHolder.title = (TextView)convertView.findViewById(R.id.news_listitem_title);
			mHolder.author = (TextView)convertView.findViewById(R.id.news_listitem_author);
			mHolder.count= (TextView)convertView.findViewById(R.id.news_listitem_commentCount);
			mHolder.date= (TextView)convertView.findViewById(R.id.news_listitem_date);
			mHolder.flag= (ImageView)convertView.findViewById(R.id.news_listitem_flag);
			convertView.setTag(mHolder);
		}
		else 
		{
			mHolder = (ViewHolder)convertView.getTag();
		}	
		
		//设置文字和图片
		News news = mData.get(position);
		mHolder.title.setText(news.getTitle());
		mHolder.title.setTag(news);
		mHolder.author.setText(news.getAuthor());
		mHolder.date.setText(StringUtils.friendly_time(news.getPubDate()));
		mHolder.count.setText(news.getCommentCount()+"");
		if(StringUtils.isToday(news.getPubDate()))
		{
			mHolder.flag.setVisibility(View.VISIBLE);
		}
		else
		{
			mHolder.flag.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	static class ViewHolder
	{				
        public TextView  title;  
	    public TextView  author;
	    public TextView  date;  
	    public TextView  count;
	    public ImageView flag;
	}  
}
