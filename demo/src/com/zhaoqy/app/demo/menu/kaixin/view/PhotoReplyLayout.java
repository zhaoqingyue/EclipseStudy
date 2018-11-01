/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: PhotoReplyLayout.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.view
 * @Description: 显示照片评论中的回复
 * @author: zhaoqy
 * @date: 2015-11-10 下午1:45:50
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhaoqy.app.demo.DemoApplication;
import com.zhaoqy.app.demo.R;

public class PhotoReplyLayout 
{
	private DemoApplication mApplication;
	private View      mLayout;
	private ImageView mAvatar;
	private TextView  mName;
	private TextView  mTime;
	private TextView  mContent;

	public PhotoReplyLayout(Context context, DemoApplication application) 
	{
		mApplication = application;
		mLayout = LayoutInflater.from(context).inflate(R.layout.item_menu_kaixin_photo_comment_detail_reply, null);
		@SuppressWarnings("deprecation")
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		mLayout.setLayoutParams(params);
		mAvatar = (ImageView) mLayout.findViewById(R.id.photocommentdetail_item_reply_item_avatar);
		mName = (TextView) mLayout.findViewById(R.id.photocommentdetail_item_reply_item_name);
		mTime = (TextView) mLayout.findViewById(R.id.photocommentdetail_item_reply_item_time);
		mContent = (TextView) mLayout.findViewById(R.id.photocommentdetail_item_reply_item_content);
	}

	public View getLayout() 
	{
		return mLayout;
	}

	public void setAvatar(int position) 
	{
		mAvatar.setImageBitmap(mApplication.getAvatar(position));
	}

	public void setName(CharSequence name) 
	{
		if (!TextUtils.isEmpty(name)) 
		{
			mName.setText(name);
		}
	}

	public void setTime(CharSequence time) 
	{
		if (!TextUtils.isEmpty(time)) 
		{
			mTime.setText(time);
		}
	}

	public void setContent(CharSequence content) 
	{
		if (!TextUtils.isEmpty(content)) 
		{
			mContent.setText(content);
		}
	}
}
