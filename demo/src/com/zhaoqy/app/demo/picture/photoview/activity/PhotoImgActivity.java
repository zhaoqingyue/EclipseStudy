package com.zhaoqy.app.demo.picture.photoview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.picture.photoview.view.PhotoView;

public class PhotoImgActivity extends Activity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mTitle;
	private PhotoView mPhotoView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_photo_img);

		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mPhotoView = (PhotoView) findViewById(R.id.img1);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("单个ImageView");
		mPhotoView.enable();
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
