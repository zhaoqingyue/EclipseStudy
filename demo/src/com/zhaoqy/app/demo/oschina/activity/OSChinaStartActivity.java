/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaStartActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 启动页面
 * @author: zhaoqy
 * @date: 2015-11-17 下午1:58:28
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import com.zhaoqy.app.demo.R;

public class OSChinaStartActivity extends Activity 
{
	private Context mContext;
	private View    mView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oschina_start);
		mContext = this;
		
		initView();
		initData();
	}

	private void initView() 
	{
		mView = findViewById(R.id.id_oschina_start);
	}

	private void initData() 
	{
		//渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(1500);
		mView.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener()
		{
			@Override
			public void onAnimationEnd(Animation arg0) 
			{
				Intent intent = new Intent(mContext, OSChinaMainActivity.class);
		        startActivity(intent);
		        finish();
			}
			
			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}
			
			@Override
			public void onAnimationStart(Animation animation) 
			{
			}
		});		
	}
}
