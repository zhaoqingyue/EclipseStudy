package com.zhaoqy.app.demo.notebook.billnotes.adapter;

import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter 
{
	private List<Fragment> mList;

	public ViewPagerAdapter(FragmentManager fm) 
	{
		super(fm);
	}

	public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) 
	{
		super(fm);
		mList = list;
	}

	@Override
	public Fragment getItem(int position) 
	{
		Fragment fragment = mList.get(position);
		return fragment;
	}

	@Override
	public int getCount() 
	{
		if (mList != null)
		{
			return mList.size();
		}
		return 0;
	}
}
