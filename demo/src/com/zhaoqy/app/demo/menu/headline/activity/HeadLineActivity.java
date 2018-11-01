package com.zhaoqy.app.demo.menu.headline.activity;

import java.util.ArrayList;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.headline.adapt.NewsFragmentPagerAdapter;
import com.zhaoqy.app.demo.menu.headline.item.NewsClassify;
import com.zhaoqy.app.demo.menu.headline.slidemenu.SlidingMenu;
import com.zhaoqy.app.demo.menu.headline.util.Constants;
import com.zhaoqy.app.demo.menu.headline.view.ColumnHScrollView;
import com.zhaoqy.app.demo.menu.headline.view.DrawerView;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HeadLineActivity extends FragmentActivity implements OnClickListener
{
	private ArrayList<NewsClassify> mNewsClassify = new ArrayList<NewsClassify>();
	private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	private ColumnHScrollView   mHScrollView;
	private LinearLayout        mRadioGroup;
	private LinearLayout        mMoreColumns;
	private RelativeLayout      mColumn;
	private ViewPager           mViewPager;
	private ImageView           mMoreColumnsIcon;
	private ImageView           mShadeLeft;
	private ImageView           mShadeRight;
	private SlidingMenu         mSlidingMenu;
	private ProgressBar         mProgress;
	private ImageView           mRefresh;
	private ImageView           mHead;
	private ImageView           mMore;
	private int                 mSelectIndex = 0;
	private int                 mScreenWidth = 0;
	private int                 mItemWidth = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_headline);
		
		mScreenWidth = getWindowsWidth(this);
		mItemWidth = mScreenWidth / 7;
		initView();
		setListener();
		setChangelView();
		initSlidingMenu();
	}
	
	private void initView() 
	{
		mHScrollView =  (ColumnHScrollView)findViewById(R.id.id_menu_headline_hscrollview);
		mRadioGroup = (LinearLayout) findViewById(R.id.id_menu_headline_radiogroup);
		mMoreColumns = (LinearLayout) findViewById(R.id.id_menu_headline_more_columns);
		mColumn = (RelativeLayout) findViewById(R.id.id_menu_headline_column);
		mMoreColumnsIcon = (ImageView) findViewById(R.id.id_menu_headline_more_columns_icon);
		mViewPager = (ViewPager) findViewById(R.id.id_menu_headline_viewpager);
		mShadeLeft = (ImageView) findViewById(R.id.id_menu_headline_shade_left);
		mShadeRight = (ImageView) findViewById(R.id.id_menu_headline_shade_right);
		mHead = (ImageView) findViewById(R.id.id_menu_headline_head_top_head);
		mMore = (ImageView) findViewById(R.id.id_menu_headline_head_top_more);
		mRefresh = (ImageView) findViewById(R.id.id_menu_headline_head_refresh);
		mProgress = (ProgressBar) findViewById(R.id.id_menu_headline_head_progress);
	}
	
	private void setListener()
	{
		mMoreColumnsIcon.setOnClickListener(this);
		mHead.setOnClickListener(this);
		mMore.setOnClickListener(this);
		mRefresh.setOnClickListener(this);
	}
	
	private void setChangelView() 
	{
		initColumnData();
		initTabColumn();
		initFragment();
	}
	
	private void initColumnData() 
	{
		mNewsClassify = Constants.getData();
		mProgress.setMax(100);
	}
	
	private void initTabColumn() 
	{
		mRadioGroup.removeAllViews();
		mHScrollView.setParam(this, mScreenWidth, mRadioGroup, mShadeLeft, mShadeRight, mMoreColumns, mColumn);
		for(int i=0; i<mNewsClassify.size(); i++)
		{
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this, R.style.slidemenu_top_category_scroll_view);
			columnTextView.setBackgroundResource(R.drawable.menu_headline_radio_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(i);
			columnTextView.setText(mNewsClassify.get(i).getTitle());
			columnTextView.setTextColor(getResources().getColorStateList(R.color.menu_headline_slidemenu_top_category_scroll));
			if(mSelectIndex == i)
			{
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
			          for(int i=0; i<mRadioGroup.getChildCount(); i++)
			          {
				          View localView = mRadioGroup.getChildAt(i);
				          if (localView != v)
				          {
				        	  localView.setSelected(false);
				          }
				          else
				          {
				        	  localView.setSelected(true);
				        	  mViewPager.setCurrentItem(i);
				          }
			          }
			          Toast.makeText(getApplicationContext(), mNewsClassify.get(v.getId()).getTitle(), Toast.LENGTH_SHORT).show();
				}
			});
			mRadioGroup.addView(columnTextView, i ,params);
		}
	}
	
	private void selectTab(int tab_postion) 
	{
		mSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup.getChildCount(); i++) 
		{
			View checkView = mRadioGroup.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			mHScrollView.smoothScrollTo(i2, 0);
		}
		
		for (int j = 0; j <  mRadioGroup.getChildCount(); j++) 
		{
			View checkView = mRadioGroup.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) 
			{
				ischeck = true;
			} 
			else 
			{
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}
	
	private void initFragment() 
	{
		int count =  mNewsClassify.size();
		for(int i = 0; i< count;i++)
		{
			Bundle data = new Bundle();
    		data.putString("text", mNewsClassify.get(i).getTitle());
			NewsFragment newfragment = new NewsFragment();
			newfragment.setArguments(data);
			mFragments.add(newfragment);
		}
		NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);
	}
	
	public OnPageChangeListener pageListener= new OnPageChangeListener()
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
		public void onPageSelected(int position) 
		{
			mViewPager.setCurrentItem(position);
			selectTab(position);
		}
	};

	protected void initSlidingMenu() 
	{
		mSlidingMenu = new DrawerView(this).initSlidingMenu();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if(mSlidingMenu.isMenuShowing() || mSlidingMenu.isSecondaryMenuShowing())
			{
				mSlidingMenu.showContent();
			}
			else 
			{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public final static int getWindowsWidth(Activity activity) 
	{
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_menu_headline_more_columns_icon:
		{
			break;
		}
		case R.id.id_menu_headline_head_top_head:
		{
			if(mSlidingMenu.isMenuShowing())
			{
				mSlidingMenu.showContent();
			}
			else
			{
				mSlidingMenu.showMenu();
			}
			break;
		}
		case R.id.id_menu_headline_head_top_more:
		{
			if(mSlidingMenu.isSecondaryMenuShowing())
			{
				mSlidingMenu.showContent();
			}
			else
			{
				mSlidingMenu.showSecondaryMenu();
			}
			break;
		}
		case R.id.id_menu_headline_head_refresh:
		{
			break;
		}
		default:
			break;
		}
	}
}
