/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyPagerAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.adapter
 * @Description: 安全卫士PagerAdapter
 * @author: zhaoqy
 * @date: 2015-12-10 下午4:35:38
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class SafetyPagerAdapter extends PagerAdapter 
{
	public List<View> mViews;
	
	public SafetyPagerAdapter(List<View> views)
	{
		mViews = views;
	}
	
	@Override
	public int getCount() 
	{
		return mViews.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) 
	{
		return view == obj;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) 
	{
		super.destroyItem(container, position, object);
		container.removeView(mViews.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) 
	{
		container.addView(mViews.get(position));
		return mViews.get(position);
	}
}
