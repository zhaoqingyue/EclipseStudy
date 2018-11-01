package com.zhaoqy.app.demo.textview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.textview.badgecustom.BadgeCustomActivity;
import com.zhaoqy.app.demo.textview.badgejar.BadgeJarActivity;
import com.zhaoqy.app.demo.textview.dynamic.DynamicActivity;
import com.zhaoqy.app.demo.textview.typetextview.TypeTextActivity;

public class TextViewActivity extends Activity implements OnClickListener
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
		setContentView(R.layout.activity_textview);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mItem0 = (TextView) findViewById(R.id.id_textview_item0);
		mItem1 = (TextView) findViewById(R.id.id_textview_item1);
		mItem2 = (TextView) findViewById(R.id.id_textview_item2);
		mItem3 = (TextView) findViewById(R.id.id_textview_item3);
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
		mTitle.setText(R.string.main_item17);
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
		case R.id.id_textview_item0:
		{
			Intent intent = new Intent(mContext, TypeTextActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_textview_item1:
		{
			Intent intent = new Intent(mContext, BadgeCustomActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_textview_item2:
		{
			Intent intent = new Intent(mContext, BadgeJarActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_textview_item3:
		{
			Intent intent = new Intent(mContext, DynamicActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
