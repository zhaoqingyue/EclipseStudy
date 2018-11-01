package com.zhaoqy.app.demo.oschina.widget;

import java.lang.ref.WeakReference;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

public class QuickAction 
{
	public Drawable mDrawable;
    public CharSequence mTitle;
    WeakReference<View> mView;

    public QuickAction(Drawable d, CharSequence title) 
    {
        mDrawable = d;
        mTitle = title;
    }

    public QuickAction(Context ctx, int drawableId, CharSequence title) 
    {
        mDrawable = ctx.getResources().getDrawable(drawableId);
        mTitle = title;
    }

    public QuickAction(Context ctx, Drawable d, int titleId) 
    {
        mDrawable = d;
        mTitle = ctx.getResources().getString(titleId);
    }

    public QuickAction(Context ctx, int drawableId, int titleId) 
    {
        mDrawable = ctx.getResources().getDrawable(drawableId);
        mTitle = ctx.getResources().getString(titleId);
    }

	public Drawable getIcon() 
	{
		return mDrawable;
	}

	public void setIcon(Drawable mDrawable) 
	{
		this.mDrawable = mDrawable;
	}

	public CharSequence getTitle() 
	{
		return mTitle;
	}

	public void setTitle(CharSequence mTitle) 
	{
		this.mTitle = mTitle;
	}    
}
