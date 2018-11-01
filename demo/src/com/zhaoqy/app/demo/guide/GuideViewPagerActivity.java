package com.zhaoqy.app.demo.guide;

import java.util.ArrayList;
import java.util.List;
import com.zhaoqy.app.demo.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class GuideViewPagerActivity extends Activity implements OnClickListener, OnPageChangeListener
{
	private ImageView       mBack;
	private TextView        mTitle;
	private ViewPager       mViewPager;
	private List<ImageView> mImageList;
	private TextView        mDescription;
	private LinearLayout    mPoints;
	private String[] imageDescriptions;
	private int previousSelectPosition = 0;
	private boolean isLoop = true;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_viewpager);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mViewPager = (ViewPager) findViewById(R.id.id_guide_viewpager);
		mDescription = (TextView) findViewById(R.id.id_guide_viewpager_description);
		mPoints = (LinearLayout) findViewById(R.id.id_guide_viewpager_points);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		prepareData();
		
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.guide_item2));
		
		ViewPagerAdapter adapter = new ViewPagerAdapter();
    	mViewPager.setAdapter(adapter);
    	mViewPager.setOnPageChangeListener(this);
    	
    	mDescription.setText(imageDescriptions[previousSelectPosition]);
    	mPoints.getChildAt(previousSelectPosition).setEnabled(true);
    
    	int n = Integer.MAX_VALUE / 2 % mImageList.size();
    	int itemPosition = Integer.MAX_VALUE / 2 - n;
    	mViewPager.setCurrentItem(itemPosition);
		
		//自动切换页面功能
		new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				while (isLoop) 
				{
					SystemClock.sleep(2000);
					handler.sendEmptyMessage(0);
				}
			}
		}).start();
	}
	
	@SuppressWarnings("deprecation")
	private void prepareData() 
	{
		mImageList = new ArrayList<ImageView>();
    	int[] imageResIDs = getImageResIDs();
    	imageDescriptions = getImageDescription();

    	ImageView iv;
    	View view;
    	for (int i = 0; i < imageResIDs.length; i++) 
    	{
			iv = new ImageView(this);
			iv.setBackgroundResource(imageResIDs[i]);
			mImageList.add(iv);
			//添加点view对象
			view = new View(this);
			view.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_viewpager_point_bg));
			LayoutParams lp = new LayoutParams(5, 5);
			lp.leftMargin = 10;
			view.setLayoutParams(lp);
			view.setEnabled(false);
			mPoints.addView(view);
		}
    }
    
    private int[] getImageResIDs() 
    {
    	return new int[] {
    			R.drawable.welcome_02,
    			R.drawable.welcome_03,
    			R.drawable.welcome_04,
    			R.drawable.welcome_05,
    			R.drawable.welcome_06
    	};
    }
    
    private String[] getImageDescription() 
    {
    	return new String[] {
    			"第一个引导页面",
    			"第二个引导页面",
    			"第三个引导页面",
    			"第四个引导页面",
    			"第五个引导页面"
    	};
    }
    
    class ViewPagerAdapter extends PagerAdapter 
    {
		@Override
		public int getCount() 
		{
			return Integer.MAX_VALUE;
		}

		/**
		 * 判断出去的view是否等于进来的view 如果为true直接复用
		 */
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) 
		{
			return arg0 == arg1;
		}

		/**
		 * 销毁预加载以外的view对象, 会把需要销毁的对象的索引位置传进来就是position
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) 
		{
			container.removeView(mImageList.get(position % mImageList.size()));
		}

		/**
		 * 创建一个view
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) 
		{
			container.addView(mImageList.get(position % mImageList.size()));
			return mImageList.get(position % mImageList.size());
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
		//改变图片的描述信息
		mDescription.setText(imageDescriptions[position % mImageList.size()]);
		//切换选中的点
		mPoints.getChildAt(previousSelectPosition).setEnabled(false);	    //把前一个点置为normal状态
		mPoints.getChildAt(position % mImageList.size()).setEnabled(true); //把当前选中的position对应的点置为enabled状态
		previousSelectPosition = position  % mImageList.size();
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
}
