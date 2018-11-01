package com.zhaoqy.app.demo.listview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.listview.custom.activity.ListView0Activity;
import com.zhaoqy.app.demo.listview.custom.activity.ListView1Activity;
import com.zhaoqy.app.demo.listview.custom.activity.ListView2Activity;
import com.zhaoqy.app.demo.listview.paging.activity.PagingActivity;
import com.zhaoqy.app.demo.listview.pulltozoom.activity.PullToZoomActivity;

public class ListViewActivity extends Activity implements OnClickListener
{
	private Context   mContext;
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
		setContentView(R.layout.activity_listview);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mItem0 = (TextView) findViewById(R.id.id_listview_item0);
		mItem1 = (TextView) findViewById(R.id.id_listview_item1);
		mItem2 = (TextView) findViewById(R.id.id_listview_item2);
		mItem3 = (TextView) findViewById(R.id.id_listview_item3);
		mItem4 = (TextView) findViewById(R.id.id_listview_item4);
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
		mTitle.setText(getResources().getString(R.string.main_item9));
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
		case R.id.id_listview_item0:
		{
			Intent intent = new Intent(mContext, ListView0Activity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_listview_item1:
		{
			Intent intent = new Intent(mContext, ListView1Activity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_listview_item2:
		{
			Intent intent = new Intent(mContext, ListView2Activity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_listview_item3:
		{
			Intent intent = new Intent(mContext, PullToZoomActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_listview_item4:
		{
			Intent intent = new Intent(mContext, PagingActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
