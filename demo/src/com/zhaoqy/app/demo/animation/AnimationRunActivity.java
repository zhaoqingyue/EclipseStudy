package com.zhaoqy.app.demo.animation;

import java.util.ArrayList;
import java.util.List;
import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AnimationRunActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private ImageView mImage;
	private ListView  mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation_run);
		mContext = this;
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mImage = (ImageView) findViewById(R.id.id_animation_run_image);
		mListView = (ListView) findViewById(R.id.id_animation_run_listView);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		Intent intent = getIntent();
		int index = intent.getIntExtra("index", 1);
		setTitleText(index);
		startAnimation(index);
	}

	private void startAnimation(int index) 
	{
		Animation animation;
		switch (index) 
		{
		case 1:
		{
			animation = AnimationUtils.loadAnimation(mContext, R.anim.animation_scale);
			mImage.startAnimation(animation);
			break;
		}
		case 2:
		{
			animation = AnimationUtils.loadAnimation(mContext, R.anim.animation_alpha);
			mImage.startAnimation(animation);
			break;
		}
		case 3:
		{
			animation = AnimationUtils.loadAnimation(mContext, R.anim.animation_rotate);
			mImage.startAnimation(animation);
			break;
		}
		case 4:
		{
			animation = AnimationUtils.loadAnimation(mContext, R.anim.animation_translate);
			mImage.startAnimation(animation);
			break;
		}
		case 5:
		{
			animation = AnimationUtils.loadAnimation(mContext, R.anim.animation_translate);
			mImage.startAnimation(animation);
			final Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.animation_rotate);
			animation.setAnimationListener(new AnimationListener() 
			{
				@Override
				public void onAnimationStart(Animation arg0) {
				}
				@Override
				public void onAnimationRepeat(Animation arg0) {
				}
				@Override
				public void onAnimationEnd(Animation arg0) 
				{
					mImage.startAnimation(animation2);
				}
			});
			break;
		}
		case 6:
		{
			animation = AnimationUtils.loadAnimation(mContext, R.anim.animation_alpha_alpha);
			mImage.startAnimation(animation);
			break;
		}
		case 7:
		{
			AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
			alphaAnimation.setDuration(100);
			alphaAnimation.setRepeatCount(10);
			//倒序重复REVERSE  正序重复RESTART
			alphaAnimation.setRepeatMode(Animation.REVERSE);
			mImage.startAnimation(alphaAnimation);
			break;
		}
		case 8:
		{
			TranslateAnimation translate = new TranslateAnimation(-50, 50, 0, 0);
			translate.setDuration(1000);
			translate.setRepeatCount(Animation.INFINITE);
			translate.setRepeatMode(Animation.REVERSE);
			mImage.startAnimation(translate);
			break;
		}
		case 9:
		{
			break;
		}
		case 10:
		{
			mImage.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			
			List<String> list = new ArrayList<String>();
			for(int i=0; i<12; i++)
			{
				list.add("demo" + i);
			}
			AnimAdapter adapter = new AnimAdapter(mContext, list);
			mListView.setAdapter(adapter);
		    LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(mContext, R.anim.animation_zoom_in));
		    lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
		    mListView.setLayoutAnimation(lac);
		    mListView.startLayoutAnimation();
			break;
		}
		case 11:
		{
			mImage.setImageBitmap(null);
			mImage.setBackgroundResource(R.drawable.animation_list);
			AnimationDrawable drawable = (AnimationDrawable) mImage.getBackground();
			drawable.start();
			break;
		}
		default:
			break;
		}
	}

	private void setTitleText(int index) 
	{
		switch (index) 
		{
		case 1:
		{
			mTitle.setText(R.string.animation_basic_scale);
			break;
		}
		case 2:
		{
			mTitle.setText(R.string.animation_basic_alpha);
			break;
		}
		case 3:
		{
			mTitle.setText(R.string.animation_basic_rotate);
			break;
		}
		case 4:
		{
			mTitle.setText(R.string.animation_basic_translate);
			break;
		}
		case 5:
		{
			mTitle.setText(R.string.animation_basic_translate_rotate);
			break;
		}
		case 6:
		{
			mTitle.setText(R.string.animation_basic_alpha_alpha);
			break;
		}
		case 7:
		{
			mTitle.setText(R.string.animation_basic_flash);
			break;
		}
		case 8:
		{
			mTitle.setText(R.string.animation_basic_shake);
			break;
		}
		case 9:
		{
			mTitle.setText(R.string.animation_basic_switch);
			break;
		}
		case 10:
		{
			mTitle.setText(R.string.animation_basic_layout);
			break;
		}
		case 11:
		{
			mTitle.setText(R.string.animation_basic_frame);
			break;
		}
		default:
			break;
		}
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
