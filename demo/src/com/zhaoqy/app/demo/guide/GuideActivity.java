package com.zhaoqy.app.demo.guide;

import com.zhaoqy.app.demo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class GuideActivity extends Activity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mItem0;
	private TextView  mItem1;
	private TextView  mItem2;
	private TextView  mItem3;
	private TextView  mItem4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mItem0 = (TextView) findViewById(R.id.id_guide_item0);
		mItem1 = (TextView) findViewById(R.id.id_guide_item1);
		mItem2 = (TextView) findViewById(R.id.id_guide_item2);
		mItem3 = (TextView) findViewById(R.id.id_guide_item3);
		mItem4 = (TextView) findViewById(R.id.id_guide_item4);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mItem0.setOnClickListener(this);
		mItem1.setOnClickListener(this);
		mItem2.setOnClickListener(this);
		mItem3.setOnClickListener(this);
		mItem4.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.main_item3));
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
		case R.id.id_guide_item0:
		{
			Intent intent = new Intent(this, GuideZakerActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_guide_item1:
		{
			Intent intent = new Intent(this, GuideSceneActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_guide_item2:
		{
			Intent intent = new Intent(this, GuideViewPagerActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_guide_item3:
		{
			Intent intent = new Intent(this, GuideShowTimeActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_guide_item4:
		{
			Intent intent = new Intent(this, GuideJDActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
