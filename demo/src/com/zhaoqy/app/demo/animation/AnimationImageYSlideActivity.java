package com.zhaoqy.app.demo.animation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import com.zhaoqy.app.demo.R;

public class AnimationImageYSlideActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		ImageView img1 = new ImageView(this);
		img1.setImageResource(R.drawable.welcome_05);
		ImageView img2 = new ImageView(this);
		img2.setImageResource(R.drawable.welcome_06);
		TwoSidedView tsv = new TwoSidedView(this, img1, img2, 2500);
		setContentView(tsv);
	}
}
