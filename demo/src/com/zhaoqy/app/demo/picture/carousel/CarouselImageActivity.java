package com.zhaoqy.app.demo.picture.carousel;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class CarouselImageActivity extends Activity implements OnClickListener
{
	private ImageView    mBack;
	private TextView     mTitle;
	private ViewPager    mViewpager;
	private ImageAdapter mAdapter;
	private LinearLayout mViewGroup;
	private ImageView    mDots[];
	private int          mAutoChangeTime = 5000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_carousel);
		
		initView();
		setListener();
		initData();
		initViewPager();
	}
	
	private void initView()
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mViewpager = (ViewPager) findViewById(R.id.id_viewpager);
		mViewGroup = (LinearLayout) findViewById(R.id.id_viewpager_viewgroup);
	}
	
	private void setListener()
	{
		mBack.setOnClickListener(this);
	}
	
	private void initData()
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.picture_item0);
	}
	
	private void initViewPager() 
	{
		mAdapter = new ImageAdapter(this);
		mAdapter.change(getList());
		mViewpager.setAdapter(mAdapter);
		mViewpager.setOnPageChangeListener(myOnPageChangeListener);
		initDot();
		mHandler.postDelayed(runnable, mAutoChangeTime);
	}
	
	Runnable runnable = new Runnable() 
	{  
        @Override  
        public void run() 
        {
        	int next = mViewpager.getCurrentItem() + 1;
        	if(next >= mAdapter.getCount()) 
        	{
        		next = 0;
        	}
        	mHandler.sendEmptyMessage(next);
        }  
    };

	private List<Integer> getList() 
	{
		List<Integer> list = new ArrayList<Integer>();
		list.add(R.drawable.image_viewpager1);
		list.add(R.drawable.image_viewpager2);
		list.add(R.drawable.image_viewpager3);
		list.add(R.drawable.image_viewpager4);
		return list;
	}

	private void initDot() 
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(40, 40);
		layoutParams.setMargins(4, 3, 4, 3);
		mDots = new ImageView[mAdapter.getCount()];
		for (int i=0; i<mAdapter.getCount(); i++) 
		{
			ImageView dot = new ImageView(this);
			dot.setLayoutParams(layoutParams);
			mDots[i] = dot;
			mDots[i].setTag(i);
			mDots[i].setOnClickListener(onClick);

			if (i == 0) 
			{
				mDots[i].setBackgroundResource(R.drawable.image_viewpager_dot_f);
			} 
			else 
			{
				mDots[i].setBackgroundResource(R.drawable.image_viewpager_dot_n);
			}
			mViewGroup.addView(mDots[i]);
		}
	}

	OnPageChangeListener myOnPageChangeListener = new OnPageChangeListener() 
	{
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
			setCurDot(arg0);
			mHandler.removeCallbacks(runnable);
			mHandler.postDelayed(runnable, mAutoChangeTime);
		}
	};
	
	OnClickListener onClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			int position = (Integer) v.getTag();
			setCurView(position);
		}
	};

	private void setCurView(int position) 
	{
		if (position < 0 || position > mAdapter.getCount()) 
		{
			return;
		}
		mViewpager.setCurrentItem(position);
	}

	private void setCurDot(int position) 
	{
		for (int i=0; i<mDots.length; i++) 
		{
			if (position == i) 
			{
				mDots[i].setBackgroundResource(R.drawable.image_viewpager_dot_f);
			}
			else 
			{
				mDots[i].setBackgroundResource(R.drawable.image_viewpager_dot_n);
			}
		}
	}
	
    @SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() 
    { 
        @Override  
        public void handleMessage(Message msg) 
        {
        	super.handleMessage(msg);
        	setCurView(msg.what);  
        }  
    };

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
}
