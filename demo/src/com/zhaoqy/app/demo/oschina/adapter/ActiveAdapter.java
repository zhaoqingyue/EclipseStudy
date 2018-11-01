/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: ActiveAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.adapter
 * @Description: 用户动态Adapter
 * @author: zhaoqy
 * @date: 2015-11-24 下午5:05:18
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
import com.zhaoqy.app.demo.oschina.item.Active;
import com.zhaoqy.app.demo.oschina.item.Active.ObjectReply;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

public class ActiveAdapter extends BaseAdapter 
{
	private Context 	   mContext;
	private List<Active>   mData;
	private LayoutInflater mContainer;
	private BitmapManager  mBmpManager;
	private int 		   mResource;
	

	public ActiveAdapter(Context context, List<Active> data,int resource) 
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
			mHolder.userface = (ImageView)convertView.findViewById(R.id.active_listitem_userface);
			mHolder.username = (TextView)convertView.findViewById(R.id.active_listitem_username);
			mHolder.content = (TextView)convertView.findViewById(R.id.active_listitem_content);
			mHolder.date = (TextView)convertView.findViewById(R.id.active_listitem_date);
			mHolder.commentCount = (TextView)convertView.findViewById(R.id.active_listitem_commentCount);
			mHolder.client= (TextView)convertView.findViewById(R.id.active_listitem_client);
			mHolder.reply = (TextView)convertView.findViewById(R.id.active_listitem_reply);
			mHolder.redirect = (ImageView)convertView.findViewById(R.id.active_listitem_redirect);
			mHolder.image= (ImageView)convertView.findViewById(R.id.active_listitem_image);
			convertView.setTag(mHolder);
		}
		else 
		{
			mHolder = (ViewHolder)convertView.getTag();
		}
		//设置文字和图片
		Active active = mData.get(position);
		mHolder.username.setText(UIHelper.parseActiveAction(active.getAuthor(),active.getObjectType(),active.getObjectCatalog(),active.getObjectTitle()));
		mHolder.username.setTag(active);
		mHolder.content.setText(active.getMessage());
		mHolder.date.setText(StringUtils.friendly_time(active.getPubDate()));
		mHolder.commentCount.setText(active.getCommentCount()+"");
		
		switch(active.getAppClient())
		{	
			case Active.CLIENT_MOBILE:
			{
				mHolder.client.setText("来自:手机");
				break;
			}
			case Active.CLIENT_ANDROID:
			{
				mHolder.client.setText("来自:Android");
				break;
			}
			case Active.CLIENT_IPHONE:
			{
				mHolder.client.setText("来自:iPhone");
				break;
			}
			case Active.CLIENT_WINDOWS_PHONE:
			{
				mHolder.client.setText("来自:Windows Phone");
				break;
			}
			default:
				mHolder.client.setText("");
				break;
		}
		if(StringUtils.isEmpty(mHolder.client.getText().toString()))
		{
			mHolder.client.setVisibility(View.GONE);
		}
		else
		{
			mHolder.client.setVisibility(View.VISIBLE);
		}
		
		ObjectReply reply= active.getObjectReply();
		if(reply != null)
		{
			mHolder.reply.setText(UIHelper.parseActiveReply(reply.objectName, reply.objectBody));
			mHolder.reply.setVisibility(TextView.VISIBLE);
		}
		else
		{
			mHolder.reply.setText("");
			mHolder.reply.setVisibility(TextView.GONE);
		}
		
		if(active.getActiveType() == Active.CATALOG_OTHER)
		{
			mHolder.redirect.setVisibility(ImageView.GONE);
		}
		else
		{
			mHolder.redirect.setVisibility(ImageView.VISIBLE);
		}
		
		String faceURL = active.getFace();
		if(faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL))
		{
			mHolder.userface.setImageResource(R.drawable.oschina_dface);
		}
		else
		{
			mBmpManager.loadBitmap(faceURL, mHolder.userface);
		}
		mHolder.userface.setOnClickListener(faceClickListener);
		mHolder.userface.setTag(active);
		
		String imgSmall = active.getTweetimage();
		if(!StringUtils.isEmpty(imgSmall)) 
		{
			mBmpManager.loadBitmap(imgSmall, mHolder.image, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.oschina_image_loading));
			mHolder.image.setVisibility(ImageView.VISIBLE);
		}
		else
		{
			mHolder.image.setVisibility(ImageView.GONE);
		}
		
		return convertView;
	}
	
	static class ViewHolder
	{		
		public ImageView userface;  
        public TextView  username;  
	    public TextView  date;  
	    public TextView  content;
	    public TextView  reply;
	    public TextView  commentCount;
	    public TextView  client;
	    public ImageView redirect;  
	    public ImageView image;
	}  
	
	private View.OnClickListener faceClickListener = new View.OnClickListener()
	{
		public void onClick(View v) 
		{
			Active active = (Active)v.getTag();
			UIHelper.showUserCenter(v.getContext(), active.getAuthorId(), active.getAuthor());
		}
	};
}
