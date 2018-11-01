package com.zhaoqy.app.demo.camera.gallery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.gallery.adapter.GalleryItemAdapter;
import com.zhaoqy.app.demo.camera.gallery.item.PhotoBucket;

public class CustomGalleryListActivity extends Activity implements OnClickListener, OnItemClickListener
{
	private GalleryItemAdapter mAdapter;
	private Context     mContext;
	private ImageView   mBack;
	private TextView    mTitle;
	private GridView    mGridView;
	private PhotoBucket mPhotoBucket;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_gallery_list);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView()
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mGridView = (GridView) findViewById(R.id.id_custom_gallery_list_gridview);
	}
	private void setListener()
	{
		mBack.setOnClickListener(this);
		mGridView.setOnItemClickListener(this);
	}
	
	private void initData() 
	{
		Intent intent = getIntent();
		mPhotoBucket = (PhotoBucket) intent.getSerializableExtra("imagelist");
		mAdapter = new GalleryItemAdapter(mPhotoBucket.getImageList(), mContext);
		mGridView.setAdapter(mAdapter);
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(mPhotoBucket.getBucketName());
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
		}
		default:
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		Intent intent = new Intent(mContext, PhotoBrowerActivity.class);
		intent.putExtra("position", position);
		intent.putExtra("imagelist", mPhotoBucket);
		startActivity(intent);
	}
}
