package com.zhaoqy.app.demo.page.way.item;

import java.io.Serializable;

public class Comment implements Serializable
{
	private static final long serialVersionUID = 642225199212241050L;
	private String Content;
	private UserInfo userInfo;
	private boolean isLike;
	
	public String getContent() 
	{
		return Content;
	}
	public void setContent(String content) 
	{
		Content = content;
	}
	public UserInfo getUserInfo() 
	{
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) 
	{
		this.userInfo = userInfo;
	}
	
	public boolean isLike() 
	{
		return isLike;
	}
	
	public void setLike(boolean isLike) 
	{
		this.isLike = isLike;
	}
}
