/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: CloudSplashActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.cloud.activity
 * @Description: 360Cloud启动页
 * @author: zhaoqy
 * @date: 2015-12-9 上午11:11:52
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.cloud.activity;

import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class CloudSplashActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		View view = getLayoutInflater().inflate(R.layout.activity_360cloud_splash, null);
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1000);
		animation.setAnimationListener(new AnimationListener() 
		{
			@Override
			public void onAnimationStart(Animation animation) 
			{
			}

			@Override
			public void onAnimationRepeat(Animation animation) 
			{
			}

			@Override
			public void onAnimationEnd(Animation animation) 
			{
				skip();
			}
		});
		view.setAnimation(animation);
		setContentView(view);
	}

	private void skip() 
	{
		Intent intent = new Intent(this, CloudLoginActivity.class);
		startActivity(intent);
		finish();
	}
}
