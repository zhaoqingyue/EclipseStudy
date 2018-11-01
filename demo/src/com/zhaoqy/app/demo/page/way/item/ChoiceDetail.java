package com.zhaoqy.app.demo.page.way.item;

import java.io.Serializable;

public class ChoiceDetail implements Serializable
{
	private static final long serialVersionUID = 642225199212241050L;
	private String image;
	private String poi;
	private String text;
	
	public String getImage() 
	{
		return image;
	}
	
	public void setImage(String image) 
	{
		this.image = image;
	}
	
	public String getPoi() 
	{
		return poi;
	}
	
	public void setPoi(String poi) 
	{
		this.poi = poi;
	}
	
	public String getText() 
	{
		return text;
	}
	
	public void setText(String text) 
	{
		this.text = text;
	}
}
