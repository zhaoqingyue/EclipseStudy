/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: DianpingMainActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.dianping.activity
 * @Description: 大众点评主页
 * @author: zhaoqy
 * @date: 2015-12-16 下午3:06:43
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.dianping.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.dianping.fragment.FindFragment;
import com.zhaoqy.app.demo.page.dianping.fragment.HomeFragment;
import com.zhaoqy.app.demo.page.dianping.fragment.MyFragment;
import com.zhaoqy.app.demo.page.dianping.fragment.TuanFragment;

public class DianpingMainActivity extends FragmentActivity implements OnCheckedChangeListener
{
	private FragmentManager mManager;
	private RadioGroup   mRadioGroup;
	private RadioButton  mRadioHome;
	private HomeFragment mHomeFragment;
	private TuanFragment mTuanFragment;
	private FindFragment mFindFragment;
	private MyFragment   mMyFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dianping_main);
		
		initView();
		initData();
		setListener();
	}

	private void initView() 
	{
		mRadioGroup = (RadioGroup) findViewById(R.id.main_bottom_tabs);
		mRadioHome = (RadioButton) findViewById(R.id.main_home);
	}
	
	private void initData() 
	{
		mManager = getSupportFragmentManager();
		mRadioHome.setChecked(true);
		changeFragment(0);
	}
	
	private void setListener() 
	{
		mRadioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) 
	{
		switch (checkedId) 
		{
		case R.id.main_home:
		{
			changeFragment(0);			
			break;
		}
		case R.id.main_tuan:
		{
			changeFragment(1);
			break;
		}
		case R.id.main_find:
		{
			changeFragment(2);
			break;
		}
		case R.id.main_my:
		{
			changeFragment(3);
			break;
		}
		default:
			break;
		}		
	}
	
	public void changeFragment(int index)
	{
		FragmentTransaction beginTransaction = mManager.beginTransaction();
		hideFragments(beginTransaction);
		switch (index) 
		{
		case 0:
		{
			if(mHomeFragment == null)
			{
				mHomeFragment = new HomeFragment();
				beginTransaction.add(R.id.main_content,	mHomeFragment);
			}
			else
			{
				beginTransaction.show(mHomeFragment);
			}
			break;
		}
		case 1:
		{
			if(mTuanFragment == null)
			{
				mTuanFragment = new TuanFragment();
				beginTransaction.add(R.id.main_content,	mTuanFragment);
			}
			else
			{
				beginTransaction.show(mTuanFragment);
			}
			break;
		}
		case 2:
		{
			if(mFindFragment == null)
			{
				mFindFragment = new FindFragment();
				beginTransaction.add(R.id.main_content,	mFindFragment);
			}
			else
			{
				beginTransaction.show(mFindFragment);
			}
			break;
		}
		case 3:
		{
			if(mMyFragment == null)
			{
				mMyFragment = new MyFragment();
				beginTransaction.add(R.id.main_content,	mMyFragment);
			}
			else
			{
				beginTransaction.show(mMyFragment);
			}
			break;
		}
		default:
			break;
		}
		beginTransaction.commit();
	}
	
	private void hideFragments(FragmentTransaction transaction) 
	{  
		if (mHomeFragment != null)   
		{
			transaction.hide(mHomeFragment);  	     
		}      
		if (mTuanFragment != null) 
		{
			transaction.hide(mTuanFragment);  	    
		}
		if (mFindFragment != null)   
		{
			transaction.hide(mFindFragment);  	   
		}
		if (mMyFragment != null)   
		{
			transaction.hide(mMyFragment);  	  
		}
	}  
}
