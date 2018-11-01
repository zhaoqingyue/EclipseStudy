package com.zhaoqy.app.demo.page.vmall.item;

public class Product 
{
	private String name;
	private String picUrl;
	private String categoryUrl;

	public String getCategoryUrl() 
	{
		return categoryUrl;
	}

	public void setCategoryUrl(String categoryUrl) 
	{
		this.categoryUrl = "http://mw.vmall.com/" + categoryUrl;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getPicUrl() 
	{
		return picUrl;
	}

	public void setPicUrl(String picUrl) 
	{
		this.picUrl = picUrl;
	}
}
