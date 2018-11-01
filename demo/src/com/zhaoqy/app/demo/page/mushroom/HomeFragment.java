package com.zhaoqy.app.demo.page.mushroom;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.mushroom.TopIndicator.OnTopIndicatorListener;

public class HomeFragment extends BaseFragment implements OnTopIndicatorListener
{
	private Activity        mActivity;
	private ViewPager       mViewPager;
	private TabPagerAdapter mPagerAdapter;
	private TopIndicator    mTopIndicator;

	public static HomeFragment newInstance() 
	{
		HomeFragment homeFragment = new HomeFragment();
		return homeFragment;
	}

	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_mushroom_home, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) 
	{
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		initDisplay();
	}

	private void initViews(View view) 
	{
		
		mViewPager = (ViewPager) view.findViewById(R.id.id_home_view_pager);
		mPagerAdapter = new TabPagerAdapter(getFragmentManager());
		mTopIndicator = (TopIndicator) view.findViewById(R.id.id_home_top_indicator);
		mTopIndicator.setOnTopIndicatorListener(this);
	}
	
	private void initDisplay() 
	{
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.invalidate();
		mPagerAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroy() 
	{
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
	}

	private class TabPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener 
	{
		public TabPagerAdapter(FragmentManager fm) 
		{
			super(fm);
			mViewPager.setOnPageChangeListener(this);
		}

		@Override
		public Fragment getItem(int position) 
		{
			HomeTabFragment fragment = (HomeTabFragment) Fragment.instantiate(mActivity, HomeTabFragment.class.getName());
			fragment.setMsgName("message name " + position);
			return fragment;
		}

		@Override
		public int getCount() 
		{
			return 4;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) 
		{
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) 
		{
		}

		@Override
		public void onPageSelected(int position) 
		{
			mTopIndicator.setTabsDisplay(mActivity, position);
		}
	}

	@Override
	public void onIndicatorSelected(int index) 
	{
		mViewPager.setCurrentItem(index);
	}
}
