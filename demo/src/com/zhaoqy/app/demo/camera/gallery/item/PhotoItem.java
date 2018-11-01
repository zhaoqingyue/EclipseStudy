package com.zhaoqy.app.demo.camera.gallery.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PhotoItem implements Serializable
{
	private String  mImageId;            //图片ID
	private String  mImagePath;          //原图路径
	private boolean mIsSelected = false; //是否被选择

	public String getImageId() 
	{
		return mImageId;
	}

	public void setImageId(String imageId) 
	{
		mImageId = imageId;
	}

	public String getImagePath() 
	{
		return mImagePath;
	}

	public void setImagePath(String imagePath) 
	{
		mImagePath = imagePath;
	}

	public boolean isSelected() 
	{
		return mIsSelected;
	}

	public void setSelected(boolean isSelected) 
	{
		this.mIsSelected = isSelected;
	}
}
