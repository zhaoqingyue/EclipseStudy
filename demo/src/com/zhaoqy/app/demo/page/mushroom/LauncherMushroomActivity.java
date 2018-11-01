package com.zhaoqy.app.demo.page.mushroom;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.mushroom.TabWidget.OnTabSelectedListener;

public class LauncherMushroomActivity extends FragmentActivity implements OnClickListener, OnTabSelectedListener
{
	public static final int HOME_FRAGMENT_INDEX = 0;
	public static final int CATEGORY_FRAGMENT_INDEX = 1;
	public static final int COLLECT_FRAGMENT_INDEX = 2;
	public static final int SETTING_FRAGMENT_INDEX = 3;
	private ImageView        mBack;
	private TextView         mTitle;
	private TabWidget        mTabWidget;
	private HomeFragment     mHomeFragment;
	private CategoryFragment mCategoryFragment;
	private CollectFragment  mCollectFragment;
	private SettingFragment  mSettingFragment;
	private FragmentManager  mFragmentManager;
	private int mIndex = HOME_FRAGMENT_INDEX;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_mushroom);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mTabWidget = (TabWidget) findViewById(R.id.id_page_slide_mushroom_tab_widget);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mTabWidget.setOnTabSelectedListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.page_slide_item5));
		mFragmentManager = getSupportFragmentManager();
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		onTabSelected(mIndex);
		mTabWidget.setTabsDisplay(this, mIndex);
		mTabWidget.setIndicateDisplay(this, 3, true);
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
	public void onTabSelected(int index) 
	{
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		hideFragments(transaction);
		switch (index) 
		{
		case HOME_FRAGMENT_INDEX:
		{
			if (null == mHomeFragment) 
			{
				mHomeFragment = new HomeFragment();
				transaction.add(R.id.id_page_slide_mushroom_center_layout, mHomeFragment);
			} 
			else 
			{
				transaction.show(mHomeFragment);
			}
			break;
		}
		case CATEGORY_FRAGMENT_INDEX:
		{
			if (null == mCategoryFragment) 
			{
				mCategoryFragment = new CategoryFragment();
				transaction.add(R.id.id_page_slide_mushroom_center_layout, mCategoryFragment);
			} 
			else 
			{
				transaction.show(mCategoryFragment);
			}
			break;
		}
		case COLLECT_FRAGMENT_INDEX:
		{
			if (null == mCollectFragment) 
			{
				mCollectFragment = new CollectFragment();
				transaction.add(R.id.id_page_slide_mushroom_center_layout, mCollectFragment);
			} 
			else 
			{
				transaction.show(mCollectFragment);
			}
			break;
		}
		case SETTING_FRAGMENT_INDEX:
		{
			if (null == mSettingFragment) 
			{
				mSettingFragment = new SettingFragment();
				transaction.add(R.id.id_page_slide_mushroom_center_layout, mSettingFragment);
			} 
			else 
			{
				transaction.show(mSettingFragment);
			}
			break;
		}
		default:
			break;
		}
		mIndex = index;
		transaction.commitAllowingStateLoss();
	}
	
	private void hideFragments(FragmentTransaction transaction) 
	{
		if (null != mHomeFragment) 
		{
			transaction.hide(mHomeFragment);
		}
		if (null != mCategoryFragment) 
		{
			transaction.hide(mCategoryFragment);
		}
		if (null != mCollectFragment) 
		{
			transaction.hide(mCollectFragment);
		}
		if (null != mSettingFragment) 
		{
			transaction.hide(mSettingFragment);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		outState.putInt("index", mIndex);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		mIndex = savedInstanceState.getInt("index");
	}
}
