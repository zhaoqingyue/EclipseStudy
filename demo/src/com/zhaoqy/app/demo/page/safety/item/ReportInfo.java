package com.zhaoqy.app.demo.page.safety.item;

import android.graphics.drawable.Drawable;

public class ReportInfo 
{
	public Drawable appIcon=null;
	public Drawable Icon=null;
	public String appName="";
	public boolean Checked;
	public boolean isReport;
	
	public Drawable getIcon() 
	{
		return Icon;
	}
	
	public void setIcon(Drawable icon) 
	{
		Icon = icon;
	}
	
	public boolean isReport() 
	{
		return isReport;
	}
	
	public void setReport(boolean isReport) 
	{
		this.isReport = isReport;
	}
	
	public boolean isChecked() 
	{
		return Checked;
	}
	
	public void setChecked(boolean checked) 
	{
		Checked = checked;
	}
	
	public Drawable getAppIcon() 
	{
		return appIcon;
	}
	
	public void setAppIcon(Drawable appIcon) 
	{
		this.appIcon = appIcon;
	}
	
	public String getAppName() 
	{
		return appName;
	}
	
	public void setAppName(String appName) 
	{
		this.appName = appName;
	}
}
