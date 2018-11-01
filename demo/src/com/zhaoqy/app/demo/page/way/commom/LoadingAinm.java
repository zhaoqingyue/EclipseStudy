package com.zhaoqy.app.demo.page.way.commom;

import com.zhaoqy.app.demo.R;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

public class LoadingAinm 
{
	public static void ininLodingView(View view) 
	{
		ImageView loadingImageView = (ImageView) view.findViewById(R.id.id_view_way_load_image);
		loadingImageView.setBackgroundResource(R.anim.page_slide_way_lodding);
		final AnimationDrawable animationDrawable = (AnimationDrawable) loadingImageView.getBackground();
		loadingImageView.post(new Runnable() 
		{
			@Override
			public void run() 
			{
				animationDrawable.start();
			}
		});
	}

	public static void ininLoding(Activity activity) 
	{
		ImageView loadingImageView = (ImageView) activity.findViewById(R.id.id_view_way_load_image);
		loadingImageView.setBackgroundResource(R.anim.page_slide_way_lodding);
		final AnimationDrawable animationDrawable = (AnimationDrawable) loadingImageView.getBackground();
		loadingImageView.post(new Runnable() 
		{
			@Override
			public void run() 
			{
				animationDrawable.start();
			}
		});
	}
}
