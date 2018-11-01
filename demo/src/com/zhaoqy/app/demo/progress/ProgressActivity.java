package com.zhaoqy.app.demo.progress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.progress.button.ButtonProgressActivity;
import com.zhaoqy.app.demo.progress.color.ColorRingProgressActivity;
import com.zhaoqy.app.demo.progress.number.NumberProgressActivity;
import com.zhaoqy.app.demo.progress.selector.SectorProgressActivity;
import com.zhaoqy.app.demo.progress.smooth.activity.SmoothProgressActivity;
import com.zhaoqy.app.demo.progress.square.SquareProgressActivity;
import com.zhaoqy.app.demo.progress.wheel.WheelProgressActivity;

public class ProgressActivity extends Activity implements OnClickListener
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
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        mContext = this;
        
        initView();
        setListener();
        initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mItem0 = (TextView) findViewById(R.id.id_progress_item0);
		mItem1 = (TextView) findViewById(R.id.id_progress_item1);
		mItem2 = (TextView) findViewById(R.id.id_progress_item2);
		mItem3 = (TextView) findViewById(R.id.id_progress_item3);
		mItem4 = (TextView) findViewById(R.id.id_progress_item4);
		mItem5 = (TextView) findViewById(R.id.id_progress_item5);
		mItem6 = (TextView) findViewById(R.id.id_progress_item6);
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
	}
	
	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.main_item14);
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
		case R.id.id_progress_item0:
		{
			Intent intent = new Intent(mContext, NumberProgressActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_progress_item1:
		{
			Intent intent = new Intent(mContext, SquareProgressActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_progress_item2:
		{
			Intent intent = new Intent(mContext, SmoothProgressActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_progress_item3:
		{
			Intent intent = new Intent(mContext, ButtonProgressActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_progress_item4:
		{
			Intent intent = new Intent(mContext, WheelProgressActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_progress_item5:
		{
			Intent intent = new Intent(mContext, SectorProgressActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_progress_item6:
		{
			Intent intent = new Intent(mContext, ColorRingProgressActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
