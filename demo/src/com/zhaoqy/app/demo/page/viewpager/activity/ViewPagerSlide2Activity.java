package com.zhaoqy.app.demo.page.viewpager.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.viewpager.adapter.ViewPagerAdapter;

public class ViewPagerSlide2Activity extends Activity implements OnClickListener, OnPageChangeListener
{
	private ImageView    mBack;
	private TextView     mTitle;
	private ViewPager    mViewPager;   
	private LinearLayout mPoints;
	private View         mView1;       
	private View         mView2;       
	private View         mView3;        
	private List<View>   mViews;   
	private int          mCurPosition = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_viewpager2);
		
		initView();
		setListener();
		initData();
		initViewPager();
		initPoints();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mViewPager = (ViewPager) findViewById(R.id.id_page_slide_viewpager);
		mPoints = (LinearLayout) findViewById(R.id.id_page_slide_viewpager_points);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}
	
	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.page_slide_item8));
	}
	
	private void initViewPager() 
	{
		mViews = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		mView1 = inflater.inflate(R.layout.view_page_slide_viewpaer0, null);
		mView2 = inflater.inflate(R.layout.view_page_slide_viewpaer1, null);
		mView3 = inflater.inflate(R.layout.view_page_slide_viewpaer2, null);
		mViews.add(mView1);
		mViews.add(mView2);
		mViews.add(mView3);
		mViewPager.setAdapter(new ViewPagerAdapter(mViews));
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setCurrentItem(0);
	}
	
	@SuppressWarnings("deprecation")
	private void initPoints() 
	{
    	View view;
    	for (int i=0; i<mViews.size(); i++) 
    	{
			//添加点view对象
			view = new View(this);
			view.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_viewpager_point_bg));
			LayoutParams lp = new LayoutParams(15, 15);
			lp.leftMargin = 20;
			view.setLayoutParams(lp);
			view.setEnabled(false);
			view.setVisibility(View.VISIBLE);
			mPoints.addView(view);
		}
    	mPoints.getChildAt(mCurPosition).setEnabled(true);
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
	public void onPageSelected(int position) 
	{
		mPoints.getChildAt(mCurPosition).setEnabled(false);	    
		mPoints.getChildAt(position % mViews.size()).setEnabled(true); 
		mViewPager.setCurrentItem(position % mViews.size());
		mCurPosition = position  % mViews.size();
	}
}
