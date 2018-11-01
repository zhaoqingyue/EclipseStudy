/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: QuestionAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.adapter
 * @Description: 问答Adapter
 * @author: zhaoqy
 * @date: 2015-11-23 下午9:34:04
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.adapter;

import java.util.List;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.helper.BitmapManager;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.item.Post;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

public class QuestionAdapter extends BaseAdapter 
{
	private List<Post> 	   mData;
	private LayoutInflater mContainer;
	private BitmapManager  mBmpManager;
	private int 		   mResource;
	
	public QuestionAdapter(Context context, List<Post> data,int resource) 
	{
		mContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
		mResource = resource;
		mData = data;
		mBmpManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.oschina_dface_loading));
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
			mHolder.face = (ImageView)convertView.findViewById(R.id.question_listitem_userface);
			mHolder.title = (TextView)convertView.findViewById(R.id.question_listitem_title);
			mHolder.author = (TextView)convertView.findViewById(R.id.question_listitem_author);
			mHolder.count= (TextView)convertView.findViewById(R.id.question_listitem_count);
			mHolder.date= (TextView)convertView.findViewById(R.id.question_listitem_date);
			convertView.setTag(mHolder);
		}
		else 
		{
			mHolder = (ViewHolder)convertView.getTag();
		}	
      
		Post post = mData.get(position);
		String faceURL = post.getFace();
		if(faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL))
		{
			mHolder.face.setImageResource(R.drawable.oschina_dface);
		}
		else
		{
			mBmpManager.loadBitmap(faceURL, mHolder.face);
		}
		
		mHolder.face.setOnClickListener(faceClickListener);
		mHolder.face.setTag(post);
		mHolder.title.setText(post.getTitle());
		mHolder.title.setTag(post);
		mHolder.author.setText(post.getAuthor());
		mHolder.date.setText(StringUtils.friendly_time(post.getPubDate()));
		mHolder.count.setText(post.getAnswerCount()+"回|"+post.getViewCount()+"阅");
		return convertView;
	}
	
	private View.OnClickListener faceClickListener = new View.OnClickListener()
	{
		public void onClick(View v) 
		{
			Post post = (Post)v.getTag();
			UIHelper.showUserCenter(v.getContext(), post.getAuthorId(), post.getAuthor());
		}
	};
	
	static class ViewHolder
	{			
		public ImageView face;
        public TextView  title;  
	    public TextView  author;
	    public TextView  date;  
	    public TextView  count;
	}  
}
