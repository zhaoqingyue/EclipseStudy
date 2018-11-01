/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: GuideJDActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.guide
 * @Description: 仿京东启动页面
 * @author: zhaoqy
 * @date: 2015-11-16 下午2:13:30
 * @version: V1.0
 */

package com.zhaoqy.app.demo.guide;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.zhaoqy.app.demo.R;

public class GuideJDActivity extends Activity 
{
	private ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_jingdong);
		
		initView();
		initData();
	}
	
	private void initView() 
	{
		mImageView = (ImageView) findViewById(R.id.splash_loading_item);
	}
	
	private void initData() 
	{
		Animation translate = AnimationUtils.loadAnimation(this, R.anim.animation_guide_jd);
		translate.setAnimationListener(new AnimationListener() 
		{
			@Override
			public void onAnimationStart(Animation animation) {}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {}
		});
		mImageView.setAnimation(translate);
	}
}
