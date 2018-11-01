/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: ProcessInfo.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.item
 * @Description: 应用包信息
 * @author: zhaoqy
 * @date: 2015-12-10 下午7:52:34
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.item;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ProcessInfo 
{
	public  String   pkgname ; 
	private String   appLabel; //应用程序标签
	private Drawable appIcon ; //应用程序图像
	public  boolean  Checked;
	private long     memSize;  //进程占用的内存大小
	private int      id;
	
	public ProcessInfo(Context context)
	{
	}

	public String getAppLabel() 
	{
		return appLabel;
	}

	public void setAppLabel(String appLabel) 
	{
		this.appLabel = appLabel;
	}

	public Drawable getAppIcon() 
	{
		return appIcon;
	}

	public boolean isChecked() 
	{
		return Checked;
	}

	public void setChecked(boolean checked) 
	{
		Checked = checked;
	}

	public void setAppIcon(Drawable appIcon) 
	{
		this.appIcon = appIcon;
	}

	public long getMemSize() 
	{
		return memSize;
	}

	public void setMemSize(long size) 
	{
		this.memSize = size;
	}
	public int getId() {
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public String getPkgname() 
	{
		return pkgname;
	}
	
	public void setPkgname(String pkgname) 
	{
		this.pkgname = pkgname;
	}
}
