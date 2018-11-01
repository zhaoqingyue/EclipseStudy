/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: TweetAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.adapter
 * @Description: 动弹Adapter
 * @author: zhaoqy
 * @date: 2015-11-25 下午4:00:30
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
import com.zhaoqy.app.demo.oschina.item.Tweet;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

public class TweetAdapter extends BaseAdapter 
{
	private Context 	   mContext;
	private List<Tweet>    mData;
	private LayoutInflater mContainer;
	private BitmapManager  mBmpManager;
	private int 		   mResource;
	
	public TweetAdapter(Context context, List<Tweet> data, int resource) 
	{
		mContext = context;			
		mContainer = LayoutInflater.from(context);	
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
			mHolder.userface = (ImageView)convertView.findViewById(R.id.tweet_listitem_userface);
			mHolder.username = (TextView)convertView.findViewById(R.id.tweet_listitem_username);
			mHolder.content = (TextView)convertView.findViewById(R.id.tweet_listitem_content);
			mHolder.image= (ImageView)convertView.findViewById(R.id.tweet_listitem_image);
			mHolder.date= (TextView)convertView.findViewById(R.id.tweet_listitem_date);
			mHolder.commentCount= (TextView)convertView.findViewById(R.id.tweet_listitem_commentCount);
			mHolder.client= (TextView)convertView.findViewById(R.id.tweet_listitem_client);
			convertView.setTag(mHolder);
		}
		else 
		{
			mHolder = (ViewHolder)convertView.getTag();
		}
				
		//设置文字和图片
		Tweet tweet = mData.get(position);
		mHolder.username.setText(tweet.getAuthor());
		mHolder.username.setTag(tweet);//设置隐藏参数(实体类)
		mHolder.content.setText(tweet.getBody());
		mHolder.date.setText(StringUtils.friendly_time(tweet.getPubDate()));
		mHolder.commentCount.setText(tweet.getCommentCount()+"");

		switch(tweet.getAppClient())
		{	
			case Tweet.CLIENT_MOBILE:
			{
				mHolder.client.setText("来自:手机");
				break;
			}
			case Tweet.CLIENT_ANDROID:
			{
				mHolder.client.setText("来自:Android");
				break;
			}
			case Tweet.CLIENT_IPHONE:
			{
				mHolder.client.setText("来自:iPhone");
				break;
			}
			case Tweet.CLIENT_WINDOWS_PHONE:
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
		
		String faceURL = tweet.getFace();
		if(faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL))
		{
			mHolder.userface.setImageResource(R.drawable.oschina_dface);
		}
		else
		{
			mBmpManager.loadBitmap(faceURL, mHolder.userface);
		}
		mHolder.userface.setOnClickListener(faceClickListener);
		mHolder.userface.setTag(tweet);
		
		String imgSmall = tweet.getImgSmall();
		if(!StringUtils.isEmpty(imgSmall)) 
		{
			mBmpManager.loadBitmap(imgSmall, mHolder.image, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.oschina_image_loading));
			mHolder.image.setOnClickListener(imageClickListener);
			mHolder.image.setTag(tweet.getImgBig());
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
	    public TextView  commentCount;
	    public TextView  client;
	    public ImageView image;
	}  
	
	private View.OnClickListener faceClickListener = new View.OnClickListener()
	{
		public void onClick(View v) 
		{
			Tweet tweet = (Tweet)v.getTag();
			UIHelper.showUserCenter(v.getContext(), tweet.getAuthorId(), tweet.getAuthor());
		}
	};
	
	private View.OnClickListener imageClickListener = new View.OnClickListener()
	{
		public void onClick(View v) 
		{
			UIHelper.showImageDialog(v.getContext(), (String)v.getTag());
		}
	};
}
