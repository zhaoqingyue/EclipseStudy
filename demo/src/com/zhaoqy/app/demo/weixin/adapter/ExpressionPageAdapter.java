/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: ExpressionAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.weixin.adapter
 * @Description: 表情PagerAdapter
 * @author: zhaoqy
 * @date: 2015-12-8 下午2:49:18
 * @version: V1.0
 */

package com.zhaoqy.app.demo.weixin.adapter;

import java.util.List;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ExpressionPageAdapter extends PagerAdapter 
{
	private List<View> mViews;

	public ExpressionPageAdapter(List<View> views) 
	{
		mViews = views;
	}

	@Override
	public int getCount() 
	{
		return mViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) 
	{
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) 
	{
		((ViewPager) arg0).addView(mViews.get(arg1));
		return mViews.get(arg1);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) 
	{
		((ViewPager) arg0).removeView(mViews.get(arg1));
	}
}
