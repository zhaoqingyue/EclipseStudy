package com.zhaoqy.app.demo.camera.gallery.item;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class PhotoBucket implements Serializable
{
	public List<PhotoItem> mImageList;
	public String          mBucketName;
	public int             mCount = 0;
	
	public int getCount() 
	{
		return mCount;
	}
	
	public void setCount(int count) 
	{
		mCount = count;
	}
	
	public String getBucketName() 
	{
		return mBucketName;
	}
	
	public void setBucketName(String bucketName) 
	{
		mBucketName = bucketName;
	}
	
	public List<PhotoItem> getImageList() 
	{
		return mImageList;
	}
	
	public void setImageList(List<PhotoItem> imageList) 
	{
		mImageList = imageList;
	}
}
