/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: BillItem.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.notebook.billnotes.item
 * @Description: 账单Item
 * @author: zhaoqy
 * @date: 2015-12-30 上午11:48:51
 * @version: V1.0
 */

package com.zhaoqy.app.demo.notebook.billnotes.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BillItem implements Serializable
{
	private String    mName;
	private String    mBill;
	private String    mType;
	private String    mState;
	private String    mTime;
	private Integer[] mImgs;
	private String[]  mTypes;
	private int       mImg;
	private int       mId;
	
	public BillItem(Integer[] imgs, String[] types) 
	{
		super();
		mImgs = imgs;
		mTypes = types;
	}
	
	public BillItem() 
	{
		super();
	}
	
	public String getName() 
	{
		return mName;
	}
	
	public void setName(String name) 
	{
		mName = name;
	}
	
	public String getBill() 
	{
		return mBill;
	}
	
	public void setBill(String bill) 
	{
		mBill = bill;
	}
	
	public String getType() 
	{
		return mType;
	}
	
	public void setType(String type) 
	{
		mType = type;
	}
	
	public String getState() 
	{
		return mState;
	}
	
	public void setState(String state) 
	{
		mState = state;
	}
	
	public String getTime() 
	{
		return mTime;
	}
	
	public void setTime(String time) 
	{
		mTime = time;
	}
	
	public Integer[] getImgs() 
	{
		return mImgs;
	}
	
	public void setImgs(Integer[] imgs) 
	{
		mImgs = imgs;
	}
	
	public String[] getTypes() 
	{
		return mTypes;
	}
	public void setTypes(String[] types) 
	{
		mTypes = types;
	}
	
	public int getImg() 
	{
		return mImg;
	}
	
	public void setImg(int img) 
	{
		mImg = img;
	}
	
	public int getId() 
	{
		return mId;
	}
	
	public void setId(int id) 
	{
		mId = id;
	}
}
