package com.zhaoqy.app.demo.camera.gallery.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.gallery.adapter.PhotoAdapter;
import com.zhaoqy.app.demo.camera.gallery.item.PhotoBucket;
import com.zhaoqy.app.demo.camera.gallery.item.PhotoItem;

public class PhotoBrowerActivity extends FragmentActivity implements OnPageChangeListener
{
	private PhotoBucket     mPhotoBucket;
	private List<PhotoItem> mImageList;
	private PhotoAdapter    mAdapter;
	private ViewPager       mPager;
	private TextView        mPage;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_brower);
		findViews();
		setAdapters();
		setListeners();
	}
	
	private void findViews() 
	{
		mPager = (ViewPager) findViewById(R.id.id_photo_brower_viewpager);
		mPage =  (TextView) findViewById(R.id.id_photo_brower_page);
	}

	private void setListeners() 
	{
		mPager.setOnPageChangeListener(this);
	}

	private void setAdapters() 
	{
		Intent intent = getIntent();
		mPhotoBucket = (PhotoBucket) intent.getSerializableExtra("imagelist");
		mImageList = mPhotoBucket.getImageList();
		mAdapter = new PhotoAdapter(getSupportFragmentManager(), mImageList);
		mPager.setAdapter(mAdapter);
		
		int position = intent.getIntExtra("position", 0);
		mPage.setText(position+1 + "/" + mImageList.size());
		mPager.setCurrentItem(position);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) 
	{
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) 
	{
	}

	@Override
	public void onPageSelected(int arg0) 
	{
		mPage.setText(arg0+1 + "/" + mImageList.size());
	}
}
