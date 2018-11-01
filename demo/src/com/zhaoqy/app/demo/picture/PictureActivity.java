package com.zhaoqy.app.demo.picture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.picture.carousel.CarouselImageActivity;
import com.zhaoqy.app.demo.picture.circle.CircleImageActivity;
import com.zhaoqy.app.demo.picture.custom.CustomImageActivity;
import com.zhaoqy.app.demo.picture.kenburns.activity.KenBurnsActivity;
import com.zhaoqy.app.demo.picture.oval.activity.RoundOvalActivity;
import com.zhaoqy.app.demo.picture.photoview.activity.PhotoMainActivity;
import com.zhaoqy.app.demo.picture.process.ImageProcessActivity;
import com.zhaoqy.app.demo.picture.qrcode.activity.QrCodeImageActivity;
import com.zhaoqy.app.demo.picture.reversal.ReversalImageActivity;
import com.zhaoqy.app.demo.picture.round.RoundImageActivity;
import com.zhaoqy.app.demo.picture.textdrawable.activity.TextDrawableActivity;

public class PictureActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mItem0;
	private TextView  mItem1;
	private TextView  mItem2;
	private TextView  mItem3;
	private TextView  mItem4;
	private TextView  mItem5;
	private TextView  mItem6;
	private TextView  mItem7;
	private TextView  mItem8;
	private TextView  mItem9;
	private TextView  mItem10;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mItem0 = (TextView) findViewById(R.id.id_picture_item0);
		mItem1 = (TextView) findViewById(R.id.id_picture_item1);
		mItem2 = (TextView) findViewById(R.id.id_picture_item2);
		mItem3 = (TextView) findViewById(R.id.id_picture_item3);
		mItem4 = (TextView) findViewById(R.id.id_picture_item4);
		mItem5 = (TextView) findViewById(R.id.id_picture_item5);
		mItem6 = (TextView) findViewById(R.id.id_picture_item6);
		mItem7 = (TextView) findViewById(R.id.id_picture_item7);
		mItem8 = (TextView) findViewById(R.id.id_picture_item8);
		mItem9 = (TextView) findViewById(R.id.id_picture_item9);
		mItem10 = (TextView) findViewById(R.id.id_picture_item10);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mItem0.setOnClickListener(this);
		mItem1.setOnClickListener(this);
		mItem2.setOnClickListener(this);
		mItem3.setOnClickListener(this);
		mItem4.setOnClickListener(this);
		mItem5.setOnClickListener(this);
		mItem6.setOnClickListener(this);
		mItem7.setOnClickListener(this);
		mItem8.setOnClickListener(this);
		mItem9.setOnClickListener(this);
		mItem10.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.main_item12);
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
		case R.id.id_picture_item0:
		{
			Intent intent = new Intent(mContext, CarouselImageActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_picture_item1:
		{
			Intent intent = new Intent(mContext, CircleImageActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_picture_item2:
		{
			Intent intent = new Intent(mContext, RoundImageActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_picture_item3:
		{
			Intent intent = new Intent(mContext, RoundOvalActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_picture_item4:
		{
			Intent intent = new Intent(mContext, ReversalImageActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_picture_item5:
		{
			Intent intent = new Intent(mContext, ImageProcessActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_picture_item6:
		{
			Intent intent = new Intent(mContext, QrCodeImageActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_picture_item7:
		{
			Intent intent = new Intent(mContext, KenBurnsActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_picture_item8:
		{
			Intent intent = new Intent(mContext, CustomImageActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_picture_item9:
		{
			Intent intent = new Intent(mContext, PhotoMainActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_picture_item10:
		{
			Intent intent = new Intent(mContext, TextDrawableActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
