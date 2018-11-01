package com.zhaoqy.app.demo.picture.kenburns.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.picture.kenburns.view.KenBurnsView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleImageActivity extends Activity implements OnClickListener
{
	private ImageView    mBack;
	private TextView     mTitle;
	private KenBurnsView mImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_kenburns_single);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView()
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mImg = (KenBurnsView) findViewById(R.id.id_picture_kenburns_single);
	}
	
	private void setListener()
	{
		mBack.setOnClickListener(this);
	}
	
	private void initData()
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.picture_kenburns_item0);
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		mImg.resume();
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
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		mImg.pause();
	}
}
