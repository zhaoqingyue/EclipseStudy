package com.zhaoqy.app.demo.page.viewpager.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class ViewPagerSlideActivity extends Activity implements OnClickListener
{
	private ImageView  mBack;
	private TextView   mTitle;
	private ViewPager  mViewPager;    
	private ImageView  mImageView;   
	private TextView   mTextView1;
	private TextView   mTextView2;
	private TextView   mTextView3;
	private View       mView1;       
	private View       mView2;       
	private View       mView3;        
	private List<View> mViews;       
	private int        mOffset = 0;   
	private int        mCurIndex = 0; 
	private int        mBmpW;         
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_viewpager);
		
		initView();
		setListener();
		initData();
		InitImageView();
		InitViewPager();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mTextView1 = (TextView) findViewById(R.id.id_page_slide_viewpager_me);
		mTextView2 = (TextView) findViewById(R.id.id_page_slide_viewpager_review);
		mTextView3 = (TextView) findViewById(R.id.id_page_slide_viewpager_letter);
		mViewPager = (ViewPager) findViewById(R.id.id_page_slide_viewpager_pager);
		mImageView = (ImageView) findViewById(R.id.id_page_slide_viewpager_cursor);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mTextView1.setOnClickListener(this);
		mTextView2.setOnClickListener(this);
		mTextView3.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.page_slide_item0));
	}

	private void InitViewPager() 
	{
		mViews = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		mView1 = inflater.inflate(R.layout.view_page_slide_viewpager_layout1, null);
		mView2 = inflater.inflate(R.layout.view_page_slide_viewpager_layout2, null);
		mView3 = inflater.inflate(R.layout.view_page_slide_viewpager_layout3, null);
		mViews.add(mView1);
		mViews.add(mView2);
		mViews.add(mView3);
		mViewPager.setAdapter(new MyViewPagerAdapter(mViews));
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	//初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据 
	private void InitImageView() 
	{
		mBmpW = BitmapFactory.decodeResource(getResources(), R.drawable.page_slide_viewpager_icon).getWidth(); //获取图片宽度  
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;  //获取分辨率宽度  
		mOffset = (screenW / 3 - mBmpW) / 2;  //计算偏移量  
		Matrix matrix = new Matrix();
		matrix.postTranslate(mOffset, 0);
		mImageView.setImageMatrix(matrix);  //设置动画初始位置  
	}
	
	public class MyViewPagerAdapter extends PagerAdapter
	{
		private List<View> mListViews;
		
		public MyViewPagerAdapter(List<View> listViews) 
		{
			mListViews = listViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) 	
		{	
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) 
		{			
			 container.addView(mListViews.get(position), 0);
			 return mListViews.get(position);
		}

		@Override
		public int getCount() 
		{			
			return  mListViews.size();
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) 
		{			
			return arg0==arg1;
		}
	}

    public class MyOnPageChangeListener implements OnPageChangeListener
    {
    	int one = mOffset * 2 + mBmpW; //页卡1 -> 页卡2 偏移量  
		
		public void onPageScrollStateChanged(int arg0) 
		{
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) 
		{
		}

		public void onPageSelected(int arg0) 
		{
			Animation animation = new TranslateAnimation(one*mCurIndex, one*arg0, 0, 0);
			mCurIndex = arg0;
			//True: 图片停在动画结束位置 
			animation.setFillAfter(true);  
			animation.setDuration(300);
			mImageView.startAnimation(animation);
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
		case R.id.id_page_slide_viewpager_me:
		{
			mViewPager.setCurrentItem(0);		
			break;
		}
		case R.id.id_page_slide_viewpager_review:
		{
			mViewPager.setCurrentItem(1);		
			break;
		}
		case R.id.id_page_slide_viewpager_letter:
		{
			mViewPager.setCurrentItem(2);		
			break;
		}
		default:
			break;
		}
	}
}
