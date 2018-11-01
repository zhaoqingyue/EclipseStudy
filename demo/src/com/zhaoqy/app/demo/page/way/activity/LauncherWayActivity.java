package com.zhaoqy.app.demo.page.way.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.way.fragment.ActivityFragment;
import com.zhaoqy.app.demo.page.way.fragment.ChoiceFragment;
import com.zhaoqy.app.demo.page.way.fragment.LoginFragment;
import com.zhaoqy.app.demo.page.way.fragment.SpecialFragment;

public class LauncherWayActivity extends FragmentActivity implements OnClickListener, OnCheckedChangeListener
{
	private Context             mContext;
	private ImageView           mBack;
	private TextView            mTitle;
	private TextView            mName;
	private Animation           mAnimation;
	private ChoiceFragment      mChoiceFragment;
	private SpecialFragment     mSpecialFragment;
	private ActivityFragment    mActivityFragment;
	private LoginFragment       mLoginFragment;
	private ArrayList<Fragment> mFragments;
	private RadioGroup          mGroup;
	private RadioButton         mAdd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_way);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mName = (TextView) findViewById(R.id.id_page_slide_way_item_name);
		mGroup = (RadioGroup) findViewById(R.id.id_page_slide_way_tabbar);
		mAdd = (RadioButton) findViewById(R.id.id_page_slide_way_add);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mAdd.setOnClickListener(this);
		mGroup.setOnCheckedChangeListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.page_slide_item6));
		
		mChoiceFragment = new ChoiceFragment();
		mSpecialFragment = new SpecialFragment();
		mActivityFragment = new ActivityFragment();
		mLoginFragment= new LoginFragment();
		
		mFragments = new ArrayList<Fragment>();
		mFragments.add(mChoiceFragment);
		mFragments.add(mSpecialFragment);
		mFragments.add(mActivityFragment);
		mFragments.add(mLoginFragment);
		
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.id_page_slide_way_framelayout, mFragments.get(0));
		transaction.commit();
		mName.setText("精选");
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
		case R.id.id_page_slide_way_add:
		{
			mAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.page_slide_way_add);
			mAdd.startAnimation(mAnimation);
			startActivity(new Intent(mContext, WayAddActivity.class));
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) 
	{
		int childCount = group.getChildCount();
		int checkedIndex = 0;
		RadioButton btnButton = null;
		for (int i=0; i<childCount; i++) 
		{
			btnButton = (RadioButton) group.getChildAt(i);
			if (btnButton.isChecked()) 
			{
				checkedIndex = i;
				break;
			}
		}

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		Fragment fragment = null;
		switch (checkedIndex) 
		{
		case 0:
		{
			fragment = mFragments.get(0);
			transaction.replace(R.id.id_page_slide_way_framelayout, fragment);
			transaction.commit();
			mName.setText("精选");
			break;
		}
		case 1:
		{
			fragment = mFragments.get(1);
			transaction.replace(R.id.id_page_slide_way_framelayout, fragment);
			transaction.commit();
			mName.setText("专题");
			break;
		}
		case 2:
			break;
		case 3:
		{
			fragment = mFragments.get(2);
			transaction.replace(R.id.id_page_slide_way_framelayout, fragment);
			transaction.commit();
			mName.setText("活动");
			break;
		}
		case 4:
		{
			fragment = mFragments.get(3);
			transaction.replace(R.id.id_page_slide_way_framelayout, fragment);
			transaction.commit();
			mName.setText("登录");
			break;
		}
		default:
			break;
		}
	}
}
