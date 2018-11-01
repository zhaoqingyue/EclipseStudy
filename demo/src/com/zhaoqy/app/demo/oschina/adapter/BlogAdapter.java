/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: BlogAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.adapter
 * @Description: 用户博客Adapter
 * @author: zhaoqy
 * @date: 2015-11-24 下午5:42:41
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
import com.zhaoqy.app.demo.oschina.item.Blog;
import com.zhaoqy.app.demo.oschina.item.BlogList;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

public class BlogAdapter extends BaseAdapter 
{
	private List<Blog> 	   mData;
	private LayoutInflater mContainer;
	private int 		   mResource;
	private int			   mBlogType;
	

	public BlogAdapter(Context context, int blogtype, List<Blog> data, int resource) 
	{	
		mContainer = LayoutInflater.from(context);	
		mResource = resource;
		mData = data;
		mBlogType = blogtype;
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
		ViewHolder  mHolder = null;
		if (convertView == null) 
		{
			convertView = mContainer.inflate(mResource, null);
			mHolder = new ViewHolder();
			mHolder.title = (TextView)convertView.findViewById(R.id.blog_listitem_title);
			mHolder.author = (TextView)convertView.findViewById(R.id.blog_listitem_author);
			mHolder.count = (TextView)convertView.findViewById(R.id.blog_listitem_commentCount);
			mHolder.date = (TextView)convertView.findViewById(R.id.blog_listitem_date);
			mHolder.type = (ImageView)convertView.findViewById(R.id.blog_listitem_documentType);
			convertView.setTag(mHolder);
		}
		else 
		{
			mHolder = (ViewHolder)convertView.getTag();
		}	
		
		//设置文字和图片
		Blog blog = mData.get(position);
		mHolder.title.setText(blog.getTitle());
		mHolder.title.setTag(blog);//设置隐藏参数(实体类)
		mHolder.date.setText(StringUtils.friendly_time(blog.getPubDate()));
		mHolder.count.setText(blog.getCommentCount() + "");
		if(blog.getDocumentType() == Blog.DOC_TYPE_ORIGINAL)
		{
			mHolder.type.setImageResource(R.drawable.oschina_original_icon);
		}
		else
		{
			mHolder.type.setImageResource(R.drawable.oschina_repaste_icon);
		}
		
		if(mBlogType == BlogList.CATALOG_USER)
		{
			mHolder.author.setVisibility(View.GONE);
		}
		else
		{
			mHolder.author.setText(blog.getAuthor() + "   发表于");
		}
		return convertView;
	}
	
	static class ViewHolder
	{
        public TextView  title;
        public TextView  author;
	    public TextView  date;  
	    public TextView  count;
	    public ImageView type;
	}  
}
