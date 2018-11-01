/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: CommentAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.adapter
 * @Description: 用户评论Adapter
 * @author: zhaoqy
 * @date: 2015-11-20 下午2:09:53
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
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.helper.BitmapManager;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.item.Comment;
import com.zhaoqy.app.demo.oschina.item.Comment.Refer;
import com.zhaoqy.app.demo.oschina.item.Comment.Reply;
import com.zhaoqy.app.demo.oschina.util.StringUtils;
import com.zhaoqy.app.demo.oschina.view.LinkView;

public class CommentAdapter extends BaseAdapter 
{
	private Context 	   mContext;
	private List<Comment>  mData;
	private LayoutInflater mContainer;
	private BitmapManager  mBmpManager;
	private int 		   mResource;
	
	public CommentAdapter(Context context, List<Comment> data, int resource) 
	{
		mContext = context;
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
		ViewHolder mHolder = null;
		if (convertView == null) 
		{
			convertView = mContainer.inflate(mResource, null);
			mHolder = new ViewHolder();
			mHolder.face = (ImageView)convertView.findViewById(R.id.comment_listitem_userface);
			mHolder.name = (TextView)convertView.findViewById(R.id.comment_listitem_username);
			mHolder.date = (TextView)convertView.findViewById(R.id.comment_listitem_date);
			mHolder.content = (LinkView)convertView.findViewById(R.id.comment_listitem_content);
			mHolder.client= (TextView)convertView.findViewById(R.id.comment_listitem_client);
			mHolder.relies = (LinearLayout)convertView.findViewById(R.id.comment_listitem_relies);
			mHolder.refers = (LinearLayout)convertView.findViewById(R.id.comment_listitem_refers);
			convertView.setTag(mHolder);
		}
		else 
		{
			mHolder = (ViewHolder)convertView.getTag();
		}	
		
		Comment comment = mData.get(position);
		String faceURL = comment.getFace();
		if(faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL))
		{
			mHolder.face.setImageResource(R.drawable.oschina_dface);
		}
		else
		{
			mBmpManager.loadBitmap(faceURL, mHolder.face);
		}
		mHolder.face.setTag(comment);//设置隐藏参数(实体类)
		mHolder.face.setOnClickListener(faceClickListener);
		mHolder.name.setText(comment.getAuthor());
		mHolder.date.setText(StringUtils.friendly_time(comment.getPubDate()));
		mHolder.content.setText(comment.getContent());
		mHolder.content.parseLinkText();
		mHolder.content.setTag(comment);//设置隐藏参数(实体类)
		
		switch(comment.getAppClient())
		{	
			case Comment.CLIENT_MOBILE:
			{
				mHolder.client.setText("来自:手机");
				break;
			}
			case Comment.CLIENT_ANDROID:
			{
				mHolder.client.setText("来自:Android");
				break;
			}
			case Comment.CLIENT_IPHONE:
			{
				mHolder.client.setText("来自:iPhone");
				break;
			}
			case Comment.CLIENT_WINDOWS_PHONE:
			{
				mHolder.client.setText("来自:Windows Phone");
				break;
			}
			default:
			{
				mHolder.client.setText("");
				break;
			}
		}
		if(StringUtils.isEmpty(mHolder.client.getText().toString()))
		{
			mHolder.client.setVisibility(View.GONE);
		}
		else
		{
			mHolder.client.setVisibility(View.VISIBLE);
		}
		
		mHolder.relies.setVisibility(View.GONE);
		//先清空
		mHolder.relies.removeAllViews();
		if(comment.getReplies().size() > 0)
		{
			//评论数目
			View view = mContainer.inflate(R.layout.view_oschina_comment_reply, null);
			TextView tv = (TextView)view.findViewById(R.id.comment_reply_content);
			tv.setText(mContext.getString(R.string.comment_reply_title, comment.getReplies().size()));
			mHolder.relies.addView(view);
			//评论内容
			for(Reply reply : comment.getReplies())
			{
				View view2 = mContainer.inflate(R.layout.view_oschina_comment_reply, null);
				TextView tv2 = (TextView)view2.findViewById(R.id.comment_reply_content);
				tv2.setText(reply.rauthor + "(" + StringUtils.friendly_time(reply.rpubDate) + ")：" + reply.rcontent);
				mHolder.relies.addView(view2);
			}
			mHolder.relies.setVisibility(View.VISIBLE);
		}
		
		mHolder.refers.setVisibility(View.GONE);
		mHolder.refers.removeAllViews();
		//先清空
		if(comment.getRefers().size() > 0)
		{
			//引用内容
			for(Refer refer : comment.getRefers())
			{
				View view = mContainer.inflate(R.layout.view_oschina_comment_refer, null);
				TextView title = (TextView)view.findViewById(R.id.comment_refer_title);
				TextView body = (TextView)view.findViewById(R.id.comment_refer_body);
				title.setText(refer.refertitle);
				body.setText(refer.referbody);
				mHolder.refers.addView(view);
			}
			mHolder.refers.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	
	static class ViewHolder
	{				
		public ImageView    face;
        public TextView     name;  
	    public TextView     date;  
	    public LinkView     content;
	    public TextView     client;
	    public LinearLayout relies;
	    public LinearLayout refers;
 }  
	
	private View.OnClickListener faceClickListener = new View.OnClickListener()
	{
		public void onClick(View v) 
		{
			Comment comment = (Comment)v.getTag();
			UIHelper.showUserCenter(v.getContext(), comment.getAuthorId(), comment.getAuthor());
		}
	};

}
