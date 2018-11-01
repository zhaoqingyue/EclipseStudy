package com.zhaoqy.app.demo.guide;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class GuideSceneActivity extends Activity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mTitle;
	private ImageView mPicture;
	private TextView  mDesc ;
	private Animation mFadeIn;
	private Animation mFadeInScale;
	private Animation mFadeOut;
	private Drawable  mPicture1;
	private Drawable  mPicture2;
	private Drawable  mPicture3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_scene);
		
		initView();
		initData();
		setListener();
	}
	
	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.guide_item1));
		
		initAnim();
		initPicture();
		/**
		 * 界面刚开始显示的内容
		 */
		mPicture.setImageDrawable(mPicture1);
		mDesc.setText("儿时的我们...");
		mPicture.startAnimation(mFadeIn);
	}
	
	/**
	 * 初始化动画
	 */
	private void initAnim() 
	{
		mFadeIn = AnimationUtils.loadAnimation(this,R.anim.welcome_fade_in);
		mFadeIn.setDuration(1000);
		mFadeInScale = AnimationUtils.loadAnimation(this,R.anim.welcome_fade_in_scale);
		mFadeInScale.setDuration(6000);
		mFadeOut = AnimationUtils.loadAnimation(this,R.anim.welcome_fade_out);
		mFadeOut.setDuration(1000);
	}

	/**
	 * 初始化图片
	 */
	
	private void initPicture() 
	{
		mPicture1 = getResources().getDrawable(R.drawable.welcome_02);
		mPicture2 = getResources().getDrawable(R.drawable.welcome_03);
		mPicture3 = getResources().getDrawable(R.drawable.welcome_04);
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mPicture = (ImageView) findViewById(R.id.id_guide_scene_pic);
		mDesc = (TextView) findViewById(R.id.id_guide_scene_desc);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		
		/**
		 * 动画切换原理:开始时是用第一个渐现动画,当第一个动画结束时开始第二个放大动画,当第二个动画结束时调用第三个渐隐动画,
		 * 第三个动画结束时修改显示的内容并且重新调用第一个动画,从而达到循环效果
		 */
		mFadeIn.setAnimationListener(new AnimationListener() 
		{
			public void onAnimationStart(Animation animation) 
			{
			}

			public void onAnimationRepeat(Animation animation) 
			{
			}

			public void onAnimationEnd(Animation animation) 
			{
				mPicture.startAnimation(mFadeInScale);
			}
		});
		mFadeInScale.setAnimationListener(new AnimationListener() 
		{
			public void onAnimationStart(Animation animation) 
			{
			}

			public void onAnimationRepeat(Animation animation) 
			{
			}

			public void onAnimationEnd(Animation animation) 
			{
				mPicture.startAnimation(mFadeOut);
			}
		});
		mFadeOut.setAnimationListener(new AnimationListener() 
		{
			public void onAnimationStart(Animation animation) 
			{
			}

			public void onAnimationRepeat(Animation animation) 
			{
			}

			public void onAnimationEnd(Animation animation) 
			{
				/**
				 * 这里其实有些写的不好,还可以采用更多的方式来判断当前显示的是第几个,从而修改数据,
				 * 我这里只是简单的采用获取当前显示的图片来进行判断。
				 */
				if (mPicture.getDrawable().equals(mPicture1)) 
				{
					mDesc.setText("懵懂的我们...");
					mPicture.setImageDrawable(mPicture2);
				} 
				else if (mPicture.getDrawable().equals(mPicture2)) 
				{
					mDesc.setText("少年的我们...");
					mPicture.setImageDrawable(mPicture3);
				} 
				else if (mPicture.getDrawable().equals(mPicture3)) 
				{
					mDesc.setText("儿时的我们...");
					mPicture.setImageDrawable(mPicture1);
				}
				mPicture.startAnimation(mFadeIn);
			}
		});
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
		default:
			break;
		}
	}
}
