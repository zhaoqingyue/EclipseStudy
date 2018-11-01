/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: VmallMainActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.vmall.activity
 * @Description: 华为商城首页
 * @author: zhaoqy
 * @date: 2015-12-17 下午9:57:45
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.vmall.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.vmall.fragment.CategoryFragment;
import com.zhaoqy.app.demo.page.vmall.fragment.HomeFragment;
import com.zhaoqy.app.demo.page.vmall.fragment.HonorFragment;
import com.zhaoqy.app.demo.page.vmall.fragment.MeFragment;
import com.zhaoqy.app.demo.page.vmall.util.DBManager;
import com.zhaoqy.app.demo.page.vmall.util.VmallStatic;

@SuppressLint("NewApi")
public class VmallMainActivity extends Activity implements OnClickListener
{
	private FragmentTransaction mTransaction;
	private FragmentManager  mFragmentManager;
	private HomeFragment     mFrag1;
	private HonorFragment    mFrag2;
	private CategoryFragment mFrag3;
	private MeFragment       mFrag4;
	private Context   mContext;
	private ImageView mTabHome;
	private ImageView mTabHonor;
	private ImageView mTabCategory;
	private ImageView mTabMe;
	private ImageView mSearch;
	private ImageView mShop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vmall_main);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}

	private void initView() 
	{
		mTabHome = (ImageView) findViewById(R.id.title_home_iv);
		mTabHonor = (ImageView) findViewById(R.id.title_honor_iv);
		mTabCategory = (ImageView) findViewById(R.id.title_category_iv);
		mTabMe = (ImageView) findViewById(R.id.title_me_iv);
		mSearch = (ImageView) findViewById(R.id.main_ivsearch);
		mShop = (ImageView) findViewById(R.id.main_ivshop);
	}

	private void initData() 
	{
		VmallStatic.db = DBManager.getHelper(mContext).getWritableDatabase();
		mFragmentManager = getFragmentManager();
		mTransaction = mFragmentManager.beginTransaction();
		mTransaction.add(R.id.layoutfragment, (mFrag1 = new HomeFragment()));
		mTransaction.commit();
	}

	private void setListener() 
	{
		mSearch.setOnClickListener(this);
		mShop.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.main_ivsearch:
		{
			Intent intent = new Intent(mContext, VmallSearchActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.main_ivshop:
		{
			Intent intent = new Intent(mContext, VmallCartActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
	
	public void changePage(View view) 
	{
		mTransaction = mFragmentManager.beginTransaction();
		hideAllFrag();
		switch (view.getId()) 
		{
		case R.id.title_home:
		{
			mTransaction.show(mFrag1);
			mTabHome.setVisibility(View.VISIBLE);
			mTabHonor.setVisibility(View.INVISIBLE);
			mTabCategory.setVisibility(View.INVISIBLE);
			mTabMe.setVisibility(View.INVISIBLE);
			break;
		}
			
		case R.id.title_honor:
		{
			if (mFrag2 == null) 
			{
				mTransaction.add(R.id.layoutfragment, (mFrag2 = new HonorFragment()));
			} 
			else 
			{
				mTransaction.show(mFrag2);
			}
			mTabHome.setVisibility(View.INVISIBLE);
			mTabHonor.setVisibility(View.VISIBLE);
			mTabCategory.setVisibility(View.INVISIBLE);
			mTabMe.setVisibility(View.INVISIBLE);
			break;
		}
		case R.id.title_category:
		{
			if (mFrag3 == null) 
			{
				mTransaction.add(R.id.layoutfragment, (mFrag3 = new CategoryFragment()));
			} 
			else 
			{
				mTransaction.show(mFrag3);
			}
			mTabHome.setVisibility(View.INVISIBLE);
			mTabHonor.setVisibility(View.INVISIBLE);
			mTabCategory.setVisibility(View.VISIBLE);
			mTabMe.setVisibility(View.INVISIBLE);
			break;
		}
		case R.id.title_me:
		{
			if (mFrag4 == null) 
			{
				mTransaction.add(R.id.layoutfragment, (mFrag4 = new MeFragment()));
			} 
			else 
			{
				mTransaction.show(mFrag4);
			}
			mTabHome.setVisibility(View.INVISIBLE);
			mTabHonor.setVisibility(View.INVISIBLE);
			mTabCategory.setVisibility(View.INVISIBLE);
			mTabMe.setVisibility(View.VISIBLE);
			break;
		}
		default:
			break;
		}
		mTransaction.commit();
	}
	
	private void hideAllFrag() 
	{
		if (mFrag1 != null) 
		{
			mTransaction.hide(mFrag1);
		}
		if (mFrag2 != null) 
		{
			mTransaction.hide(mFrag2);
		}
		if (mFrag3 != null) 
		{
			mTransaction.hide(mFrag3);
		}
		if (mFrag4 != null) 
		{
			mTransaction.hide(mFrag4);
		}
	}
	
	@Override
	public void finish() 
	{
		if(VmallStatic.db.isOpen())
		{
			VmallStatic.db.close();
		}
		super.finish();
	}
}
