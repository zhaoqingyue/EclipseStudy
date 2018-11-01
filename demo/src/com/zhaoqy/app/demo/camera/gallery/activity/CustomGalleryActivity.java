package com.zhaoqy.app.demo.camera.gallery.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.gallery.adapter.GalleryAdapter;
import com.zhaoqy.app.demo.camera.gallery.item.PhotoBucket;
import com.zhaoqy.app.demo.camera.gallery.util.PhotoHelper;
import com.zhaoqy.app.demo.camera.gallery.util.PhotoHelper.GetAlbumList;

public class CustomGalleryActivity extends Activity implements OnClickListener, OnItemClickListener
{
	private List<PhotoBucket> mList;
	private GalleryAdapter    mAdapter;
	private PhotoHelper mPhotoHelper;
	private Context     mContext;
	private ImageView   mBack;
	private TextView    mTitle;
	private ListView    mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_gallery);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mListView = (ListView) findViewById(R.id.id_custom_gallery_listview);
		mAdapter = new GalleryAdapter(mContext);
		mListView.setAdapter(mAdapter);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.gallery_custom);
		
		mPhotoHelper = PhotoHelper.getHelper();
		mPhotoHelper.init(CustomGalleryActivity.this);
		mPhotoHelper.setGetAlbumList(new GetAlbumList() 
		{
			@Override
			public void getAlbumList(List<PhotoBucket> list) 
			{
				mAdapter.setArrayList(list);
				mAdapter.notifyDataSetChanged();
				mList = list;
			}
		});
		mPhotoHelper.execute(false);
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		Intent intent = new Intent(mContext, CustomGalleryListActivity.class);
		intent.putExtra("imagelist", mList.get(position));
		startActivity(intent);
	}
}
