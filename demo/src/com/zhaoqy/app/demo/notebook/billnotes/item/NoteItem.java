/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: NoteItem.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.notebook.billnotes.item
 * @Description: 记事薄Item
 * @author: zhaoqy
 * @date: 2015-12-30 上午11:47:20
 * @version: V1.0
 */

package com.zhaoqy.app.demo.notebook.billnotes.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NoteItem implements Serializable
{
	private String mTitle;
	private String mContent;
	private String mTime;
	
	public NoteItem() 
	{
		super();
	}
	public NoteItem(String title, String content) 
	{
		super();
		mTitle = title;
		mContent = content;
	}
	
	public NoteItem(String title, String content, String time) 
	{
		super();
		mTitle = title;
		mContent = content;
		mTime = time;
	}
	
	public String getTitle() 
	{
		return mTitle;
	}
	
	public void setTitle(String title) 
	{
		mTitle = title;
	}
	
	public String getContent() 
	{
		return mContent;
	}
	
	public void setContent(String content) 
	{
		mContent = content;
	}
	
	public String getTime() 
	{
		return mTime;
	}
	
	public void setTime(String time) 
	{
		mTime = time;
	}
}
