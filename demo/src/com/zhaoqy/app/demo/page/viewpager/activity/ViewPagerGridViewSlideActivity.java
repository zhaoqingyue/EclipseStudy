package com.zhaoqy.app.demo.page.viewpager.activity;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;

@SuppressWarnings("deprecation")
public class ViewPagerGridViewSlideActivity extends ActivityGroup implements OnClickListener, OnPageChangeListener
{
	private String              mName[] = { "我 爱 学 习", "其 他 相 关"};
	private ImageView           mBack;
	private TextView            mTitle;
	private LinearLayout        mLinearLayout;
	private ArrayList<TextView> mTextViews;
	private ViewPager           mViewPager;
	private ArrayList<View>     mPageViews;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_viewpager_gridview);
		
		initView();
		setListener();
		initData();
		initTitle();
		setSelector(0);
		initViewPager();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mLinearLayout = (LinearLayout) findViewById(R.id.id_page_slide_viewpager_gridview_main);
		mViewPager = (ViewPager) findViewById(R.id.id_page_slide_viewpager_gridview_pager);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.page_slide_item1));
	}
	
	private void initViewPager() 
	{
		mPageViews = new ArrayList<View>();
		View view01 = getLocalActivityManager().startActivity("activity01", new Intent(this, ViewPagerGridViewSlideStudyActivity.class)).getDecorView();
		View view02 = getLocalActivityManager().startActivity("activity02", new Intent(this, ViewPagerGridViewSlideOtherActivity.class)).getDecorView();
		mPageViews.add(view01);
		mPageViews.add(view02);
		mViewPager.setAdapter(new myPagerView());
		mViewPager.clearAnimation();
	}
	
	private void initTitle() 
	{
		mTextViews = new ArrayList<TextView>();
		int width = getWindowManager().getDefaultDisplay().getWidth() / 2;
		int height = 100;
		for (int i=0; i<mName.length; i++) 
		{
			TextView textView = new TextView(this);
			textView.setText(mName[i]);
			textView.setTextSize(20);
			textView.setTextColor(000000);
			textView.setWidth(width);
			textView.setHeight(height);
			textView.setGravity(Gravity.CENTER);
			textView.setId(i);
			textView.setOnClickListener(this);
			mTextViews.add(textView);
			//分割线
			View view = new View(this);
			LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.width = 1;
			layoutParams.height = height;
			layoutParams.gravity = Gravity.CENTER;
			view.setLayoutParams(layoutParams);
			view.setBackgroundColor(000000);
			mLinearLayout.addView(textView);
			if (i != mName.length - 1) 
			{
				mLinearLayout.addView(view);
			}
		}
	}

	public void setSelector(int id) 
	{
		for (int i=0; i<mName.length; i++) 
		{
			if (id == i) 
			{
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.page_slide_viewpager_gridview_bg_normal);
				mTextViews.get(id).setBackgroundDrawable(new BitmapDrawable(bitmap));
				mTextViews.get(id).setTextColor(Color.BLACK);
				mViewPager.setCurrentItem(i);
			}
			else 
			{
				mTextViews.get(i).setBackgroundDrawable(new BitmapDrawable());
				mTextViews.get(i).setTextColor(getResources().getColor(R.color.black));
			}
		}
	}

	class myPagerView extends PagerAdapter 
	{
		// 显示数目
		@Override
		public int getCount() 
		{
			return mPageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) 
		{
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) 
		{
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) 
		{
			((ViewPager) arg0).removeView(mPageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) 
		{
			((ViewPager) arg0).addView(mPageViews.get(arg1));
			return mPageViews.get(arg1);
		}

	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		default:
			setSelector(v.getId());
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) 
	{
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) 
	{
	}

	@Override
	public void onPageSelected(int arg0) 
	{
		setSelector(arg0);
	}
}
