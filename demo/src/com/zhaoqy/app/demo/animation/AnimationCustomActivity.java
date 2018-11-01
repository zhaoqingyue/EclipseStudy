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

public class AnimationCustomActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mItem0;
	private TextView  mItem1;
	private TextView  mItem2;
	private TextView  mItem3;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation_custom);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mItem0 = (TextView) findViewById(R.id.id_animation_custom_item0);
		mItem1 = (TextView) findViewById(R.id.id_animation_custom_item1);
		mItem2 = (TextView) findViewById(R.id.id_animation_custom_item2);
		mItem3 = (TextView) findViewById(R.id.id_animation_custom_item3);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mItem0.setOnClickListener(this);
		mItem1.setOnClickListener(this);
		mItem2.setOnClickListener(this);
		mItem3.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.animation_custom);
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
		case R.id.id_animation_custom_item0:
		{
			Intent intent = new Intent(mContext, AnimationFrameLayoutActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_custom_item1:
		{
			Intent intent = new Intent(mContext, AnimationCircleMenuActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_custom_item2:
		{
			Intent intent = new Intent(mContext, AnimationWaterWaveActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_custom_item3:
		{
			Intent intent = new Intent(mContext, AnimationSoundWaveActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
