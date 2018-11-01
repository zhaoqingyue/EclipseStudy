package com.zhaoqy.app.demo.weixin.item;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ActionItem 
{
	public Drawable     mDrawable;  //定义图片对象
	public CharSequence mTitle; //定义文本对象

	public ActionItem(Drawable drawable, CharSequence title) 
	{
		mDrawable = drawable;
		mTitle = title;
	}

	public ActionItem(Context context, int titleId, int drawableId) 
	{
		mTitle = context.getResources().getText(titleId);
		mDrawable = context.getResources().getDrawable(drawableId);
	}

	public ActionItem(Context context, CharSequence title, int drawableId) 
	{
		mTitle = title;
		mDrawable = context.getResources().getDrawable(drawableId);
	}

	public ActionItem(Context context, CharSequence title) 
	{
		mTitle = title;
		mDrawable = null;
	}
}
