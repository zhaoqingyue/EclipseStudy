package com.zhaoqy.app.demo.page.dianping.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class DianpingPagerAdapter extends PagerAdapter 
{
	public List<View> mViews;
	
	public DianpingPagerAdapter(List<View> views)
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
		container.removeView(mViews.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) 
	{
		container.addView(mViews.get(position));
		return mViews.get(position);
	}
}
