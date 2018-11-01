package com.zhaoqy.app.demo.picture.reversal;

import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReversalImageActivity extends Activity implements OnClickListener
{
	private ImageView      mBack;
	private TextView       mTitle;
	private RelativeLayout mLayout01;
	private RelativeLayout mLayout02;
	private ViewGroup      mContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_reversal);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mContainer = (ViewGroup) findViewById(R.id.id_coin_flip_container);
		mContainer.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
		mLayout01 = (RelativeLayout) findViewById(R.id.id_coin_flip_layout01);
		mLayout02 = (RelativeLayout) findViewById(R.id.id_coin_flip_layout02);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mLayout01.setOnClickListener(this);
		mLayout02.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.picture_item4);
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
		case R.id.id_coin_flip_layout01:
		{
			applyFlip(0,0,90);
			break;
		}
		case R.id.id_coin_flip_layout02:
		{
			applyFlip(-1, 180, 90);
			break;
		}
		default:
			break;
		}
	}
	
	 private void applyFlip(int position, float start, float end) 
	 {
	     final float centerX = mContainer.getWidth() / 2.0f;
	     final float centerY = mContainer.getHeight() / 2.0f;

	     final ReversalAnimation rotation = new ReversalAnimation(start, end, centerX, centerY, 310.0f, true);
	     rotation.setDuration(500);
	     rotation.setFillAfter(true);
	     rotation.setInterpolator(new AccelerateInterpolator());
	     rotation.setAnimationListener(new DisplayNextView(position));
	     mContainer.startAnimation(rotation);
	 }
	 
	 private final class DisplayNextView implements Animation.AnimationListener 
	 {
	     private final int mPosition;

	     private DisplayNextView(int position) 
	     {
	         mPosition = position;
	     }

	     public void onAnimationStart(Animation animation) 
	     {
	     }

	     public void onAnimationEnd(Animation animation) 
	     {
	         mContainer.post(new SwapViews(mPosition));
	     }

	     public void onAnimationRepeat(Animation animation) 
	     {
	     }
	 }
	 
	 private final class SwapViews implements Runnable 
	 {
		private final int mPosition;

		public SwapViews(int position) 
		{
			mPosition = position;
		}

		public void run() 
		{
			final float centerX = mContainer.getWidth() / 2.0f;
			final float centerY = mContainer.getHeight() / 2.0f;
			ReversalAnimation rotation;

			if (mPosition > -1) 
			{
				mLayout01.setVisibility(View.GONE);
				mLayout02.setVisibility(View.VISIBLE);
				rotation = new ReversalAnimation(90, 180, centerX, centerY, 310.0f,false);
			} 
			else 
			{
				mLayout02.setVisibility(View.GONE);
				mLayout01.setVisibility(View.VISIBLE);
				rotation = new ReversalAnimation(90, 0, centerX, centerY, 310.0f, false);
			}

			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());
			mContainer.startAnimation(rotation);
		}
	}
}
