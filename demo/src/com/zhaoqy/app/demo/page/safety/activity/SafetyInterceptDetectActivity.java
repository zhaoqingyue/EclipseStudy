/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyInterceptSettingActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 通知栏广告检测
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.adapter.InterceptDetectAdapter;

public class SafetyInterceptDetectActivity extends Activity implements OnClickListener
{
	private InterceptDetectAdapter mAdapter1;
	private InterceptDetectAdapter mAdapter2;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private Button    mDetect1;
	private Button    mDetect2;
	private ImageView mImageView1;
	private ImageView mImageView2;
	private ListView  mListView1;
	private ListView  mListView2;
	private View      mView1;
	private View      mView2;
	private TextView  mTextView1;
	private TextView  mTextView2;
	private boolean   mIsClick1 = true;
	private boolean   mIsClick2 = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_intercept_detect);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mDetect1 = (Button) findViewById(R.id.ad_jiance_bt1);
		mDetect2 = (Button) findViewById(R.id.ad_jiance_bt2);
		mImageView1 = (ImageView) findViewById(R.id.ad_jiance_iv1);
		mImageView2 = (ImageView) findViewById(R.id.ad_jiance_iv2);
		mListView1 = (ListView) findViewById(R.id.ad_jiance_lv1);
		mListView2 = (ListView) findViewById(R.id.ad_jiance_lv2);
		mView1 = findViewById(R.id.ad_jiance_rl2);
		mView2 = findViewById(R.id.ad_jiance_rl3);
		mTextView1 = (TextView) findViewById(R.id.ad_jiance_message1);
		mTextView2 = (TextView) findViewById(R.id.ad_jiance_message2);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mView1.setOnClickListener(this);
		mView2.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("通知栏广告检测");
		mBack.setVisibility(View.VISIBLE);
		
		mAdapter1 = new InterceptDetectAdapter(mContext, SafetyInterceptActivity.mAppList);
		mAdapter2 = new InterceptDetectAdapter(mContext, SafetyInterceptActivity.mAppList);
		mListView1.setAdapter(mAdapter1);
		mListView2.setAdapter(mAdapter2);
		mDetect1.setText("0");
		mDetect2.setText(SafetyInterceptActivity.mAppList.size() + "");
	}
	
	@SuppressWarnings("deprecation")
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
		case R.id.ad_jiance_rl2:
		{
			if (mIsClick1) 
			{
				mImageView1.setBackgroundDrawable(getResources().getDrawable(R.drawable.safety_intercept_arrow_down));
				mTextView1.setVisibility(View.VISIBLE);
				mIsClick1 = false;
			}
			else 
			{
				mImageView1.setBackgroundDrawable(getResources().getDrawable(R.drawable.safety_intercept_arrow_up));
				mTextView1.setVisibility(View.GONE);
				mIsClick1 = true;
			}
			break;
		}
		case R.id.ad_jiance_rl3:
		{
			if (mIsClick2) 
			{
				mImageView2.setBackgroundDrawable(getResources().getDrawable(R.drawable.safety_intercept_arrow_down));
				mListView2.setVisibility(View.VISIBLE);
				mTextView2.setVisibility(View.GONE);
				mIsClick2 = false;
			}
			else 
			{
				mImageView2.setBackgroundDrawable(getResources().getDrawable(R.drawable.safety_intercept_arrow_up));
				mListView2.setVisibility(View.GONE);
				mTextView2.setVisibility(View.VISIBLE);
				mIsClick2 = true;
			}
			break;
		}
		default:
			break;
		}
	}
}
