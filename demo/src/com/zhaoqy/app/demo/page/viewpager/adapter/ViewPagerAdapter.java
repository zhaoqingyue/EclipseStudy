package com.zhaoqy.app.demo.page.viewpager.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter 
{
	private List<View> mViews;
	
	public ViewPagerAdapter(List<View> listViews) 
	{
		mViews = listViews;
	}
	
	@Override
	public int getCount() 
	{			
		return  mViews.size();
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) 
	{			
		return arg0==arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) 
	{			
		 container.addView(mViews.get(position), 0);
		 return mViews.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) 	
	{	
		container.removeView(mViews.get(position));
	}
}
