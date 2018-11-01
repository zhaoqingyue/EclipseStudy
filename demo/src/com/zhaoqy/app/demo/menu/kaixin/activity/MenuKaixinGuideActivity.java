package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.zhaoqy.app.demo.R;

public class MenuKaixinGuideActivity extends Activity implements OnClickListener
{
	private List<View> mViews = new ArrayList<View>();
	private Context    mContext;
	private ViewPager  mPager;
	private Button     mButton;
	private View       mPage0;
	private View       mPage1;
	private View       mPage2;
	private View       mPage3;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_guide);
		mContext = this;
		
		checkFirst();
	}
	
	private void checkFirst() 
	{
		if(getFirst())
		{
			saveFirst();
			initView();
			setListener();
			initData();
		}
		else
		{
			startKaixin();
		}
	}

	private void initView() 
	{
		mPage0 = LayoutInflater.from(mContext).inflate(R.layout.view_menu_kaixin_guide_page0, null);
		mPage1 = LayoutInflater.from(mContext).inflate(R.layout.view_menu_kaixin_guide_page1, null);
		mPage2 = LayoutInflater.from(mContext).inflate(R.layout.view_menu_kaixin_guide_page2, null);
		mPage3 = LayoutInflater.from(mContext).inflate(R.layout.view_menu_kaixin_guide_page3, null);
		mPager = (ViewPager) findViewById(R.id.id_menu_kaixin_guide_viewpager);
		mButton = (Button) mPage3.findViewById(R.id.id_menu_kaixin_guide_open);
	}

	private void setListener() 
	{
		mButton.setOnClickListener(this);
	}
	
	private void initData() 
	{
		mViews.add(mPage0);
		mViews.add(mPage1);
		mViews.add(mPage2);
		mViews.add(mPage3);
		mPager.setAdapter(new ViewPagerAdapter());
	}
	
	private void startKaixin()
	{
		Intent intent = new Intent(mContext, MenuKaixinMainActivity.class);
		startActivity(intent);
		finish();
	}

	private class ViewPagerAdapter extends PagerAdapter 
	{
		public void destroyItem(View arg0, int arg1, Object arg2) 
		{
			((ViewPager) arg0).removeView(mViews.get(arg1));
		}

		public void finishUpdate(View arg0) 
		{
		}

		public int getCount() 
		{
			return mViews.size();
		}

		public Object instantiateItem(View arg0, int arg1) 
		{
			((ViewPager) arg0).addView(mViews.get(arg1));
			return mViews.get(arg1);
		}

		public boolean isViewFromObject(View arg0, Object arg1) 
		{
			return arg0 == arg1;
		}

		public void restoreState(Parcelable arg0, ClassLoader arg1) 
		{
		}

		public Parcelable saveState() 
		{
			return null;
		}

		public void startUpdate(View arg0) 
		{
		}
	}

	@Override
	public void onClick(View v) 
	{
		startKaixin();
	}
	
	private Boolean getFirst()
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = getSharedPreferences("sp", mContext.MODE_PRIVATE);
		return sp.getBoolean("KaixinFirst", true);
	}
	
	private void saveFirst()
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = getSharedPreferences("sp", mContext.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("KaixinFirst", false);
		editor.commit();
	}
}
