package com.zhaoqy.app.demo.index;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.index.contacts.activity.AddContactsActivity;
import com.zhaoqy.app.demo.index.indexable.activity.IndexableActivity;
import com.zhaoqy.app.demo.index.search.activity.IndexSearchActivity;

public class IndexActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mItem0;
	private TextView  mItem1;
	private TextView  mItem2;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mItem0 = (TextView) findViewById(R.id.id_index_item0);
		mItem1 = (TextView) findViewById(R.id.id_index_item1);
		mItem2 = (TextView) findViewById(R.id.id_index_item2);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mItem0.setOnClickListener(this);
		mItem1.setOnClickListener(this);
		mItem2.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.main_item15);
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
		case R.id.id_index_item0:
		{
			Intent intent = new Intent(mContext, AddContactsActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_index_item1:
		{
			Intent intent = new Intent(mContext, IndexableActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_index_item2:
		{
			Intent intent = new Intent(mContext, IndexSearchActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
