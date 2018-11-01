package com.zhaoqy.app.demo.menu.headline.adapt;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

public class NewsFragmentPagerAdapter extends FragmentPagerAdapter 
{
	private ArrayList<Fragment> mFragments;
	private FragmentManager     mFragmentManager;

	public NewsFragmentPagerAdapter(FragmentManager fm) 
	{
		super(fm);
		mFragmentManager = fm;
	}

	public NewsFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) 
	{
		super(fm);
		mFragmentManager = fm;
		mFragments = fragments;
	}

	@Override
	public int getCount() 
	{
		return mFragments.size();
	}

	@Override
	public Fragment getItem(int position) 
	{
		return mFragments.get(position);
	}

	@Override
	public int getItemPosition(Object object) 
	{
		return POSITION_NONE;
	}

	public void setFragments(ArrayList<Fragment> fragments) 
	{
		if (mFragments != null) 
		{
			FragmentTransaction ft = mFragmentManager.beginTransaction();
			for (Fragment f : mFragments) 
			{
				ft.remove(f);
			}
			ft.commit();
			ft = null;
			mFragmentManager.executePendingTransactions();
		}
		mFragments = fragments;
		notifyDataSetChanged();
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) 
	{
		Object obj = super.instantiateItem(container, position);
		return obj;
	}
}
