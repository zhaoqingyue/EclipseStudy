package com.zhaoqy.app.demo.animation;

import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimationBasicActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mScale;
	private TextView  mAlpha;
	private TextView  mRotate;
	private TextView  mTranslate;
	private TextView  mTranslateRotate;
	private TextView  mAlphaAlpha;
	private TextView  mFlash;
	private TextView  mShake;
	private TextView  mSwitch;
	private TextView  mLayout;
	private TextView  mFrame;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation_basic);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mScale = (TextView) findViewById(R.id.id_animation_basic_scale);
		mAlpha = (TextView) findViewById(R.id.id_animation_basic_alpha);
		mRotate = (TextView) findViewById(R.id.id_animation_basic_rotate);
		mTranslate = (TextView) findViewById(R.id.id_animation_basic_translate);
		mTranslateRotate = (TextView) findViewById(R.id.id_animation_basic_translate_rotate);
		mAlphaAlpha = (TextView) findViewById(R.id.id_animation_basic_alpha_alpha);
		mFlash = (TextView) findViewById(R.id.id_animation_basic_flash);
		mShake = (TextView) findViewById(R.id.id_animation_basic_shake);
		mSwitch = (TextView) findViewById(R.id.id_animation_basic_switch);
		mLayout = (TextView) findViewById(R.id.id_animation_basic_layout);
		mFrame = (TextView) findViewById(R.id.id_animation_basic_frame);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mScale.setOnClickListener(this);
		mAlpha.setOnClickListener(this);
		mRotate.setOnClickListener(this);
		mTranslate.setOnClickListener(this);
		mTranslateRotate.setOnClickListener(this);
		mAlphaAlpha.setOnClickListener(this);
		mFlash.setOnClickListener(this);
		mShake.setOnClickListener(this);
		mSwitch.setOnClickListener(this);
		mLayout.setOnClickListener(this);
		mFrame.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.animation_basic);
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
		case R.id.id_animation_basic_scale:
		{
			Intent intent = new Intent(mContext, AnimationRunActivity.class);
			intent.putExtra("index", 1);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_basic_alpha:
		{
			Intent intent = new Intent(mContext, AnimationRunActivity.class);
			intent.putExtra("index", 2);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_basic_rotate:
		{
			Intent intent = new Intent(mContext, AnimationRunActivity.class);
			intent.putExtra("index", 3);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_basic_translate:
		{
			Intent intent = new Intent(mContext, AnimationRunActivity.class);
			intent.putExtra("index", 4);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_basic_translate_rotate:
		{
			Intent intent = new Intent(mContext, AnimationRunActivity.class);
			intent.putExtra("index", 5);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_basic_alpha_alpha:
		{
			Intent intent = new Intent(mContext, AnimationRunActivity.class);
			intent.putExtra("index", 6);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_basic_flash:
		{
			Intent intent = new Intent(mContext, AnimationRunActivity.class);
			intent.putExtra("index", 7);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_basic_shake:
		{
			Intent intent = new Intent(mContext, AnimationRunActivity.class);
			intent.putExtra("index", 8);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_basic_switch:
		{
			Intent intent = new Intent(mContext, AnimationRunActivity.class);
			intent.putExtra("index", 9);
			startActivity(intent);
			overridePendingTransition(R.anim.animation_zoom_in, R.anim.animation_zoom_out);
			break;
		}
		case R.id.id_animation_basic_layout:
		{
			Intent intent = new Intent(mContext, AnimationRunActivity.class);
			intent.putExtra("index", 10);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_basic_frame:
		{
			Intent intent = new Intent(mContext, AnimationRunActivity.class);
			intent.putExtra("index", 11);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
