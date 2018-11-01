package com.zhaoqy.app.demo.camera.gallery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class GalleryActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mSystem;
	private TextView  mCustom;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mSystem = (TextView) findViewById(R.id.id_gallery_system);
		mCustom = (TextView) findViewById(R.id.id_gallery_custom);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mSystem.setOnClickListener(this);
		mCustom.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.camera_gallery);
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
		case R.id.id_gallery_system:
		{
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivity(intent);
			break;
		}
		case R.id.id_gallery_custom:
		{
			Intent intent = new Intent(mContext, CustomGalleryActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
