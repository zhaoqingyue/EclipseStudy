package com.zhaoqy.app.demo.page.way.item;

import java.io.Serializable;

public class Choice implements Serializable
{
	private static final long serialVersionUID = 642225199212241050L;
	private String   title;
	private String   id;
	private String   pictureCount;
	private String   favoriteCount;
	private String   pubdate;
	private String[] dispCities;
	private String   ViewCount;
	private String   image;
	private UserInfo userInfo;
	private Comment[] comments;
	private String    foreword;
	private String    tourId;
	private String    cmtCount;
	
	public String getTitle() 
	{
		return title;
	}
	
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public String getId() 
	{
		return id;
	}
	
	public void setId(String id) 
	{
		this.id = id;
	}
	
	public String getPictureCount() 
	{
		return pictureCount;
	}
	
	public void setPictureCount(String pictureCount) 
	{
		this.pictureCount = pictureCount;
	}
	
	public String getFavoriteCount() 
	{
		return favoriteCount;
	}
	
	public void setFavoriteCount(String favoriteCount) 
	{
		this.favoriteCount = favoriteCount;
	}
	public String getPubdate() {
		return pubdate;
	}
	
	public void setPubdate(String pubdate) 
	{
		this.pubdate = pubdate;
	}
	
	public String[] getDispCities() 
	{
		return dispCities;
	}

	public String getViewCount() 
	{
		return ViewCount;
	}
	
	public void setViewCount(String viewCount) 
	{
		ViewCount = viewCount;
	}
	
	public String getImage() 
	{
		return image;
	}
	
	public void setImage(String image) 
	{
		this.image = image;
	}
	
	public UserInfo getUserInfo() 
	{
		return userInfo;
	}
	
	public void setUserInfo(UserInfo userInfo) 
	{
		this.userInfo = userInfo;
	}
	
	public void setDispCities(String[] citys) 
	{
		this.dispCities=citys;
	}
	
	public void setComments(Comment[] comments) 
	{
		this.comments = comments;
	}
	
	public Comment[] getComments() 
	{
		return comments;
	}

	public String getForeword() 
	{
		return foreword;
	}
	
	public void setForeword(String foreword) 
	{
		this.foreword = foreword;
	}
	
	public String getTourId() 
	{
		return tourId;
	}
	public void setTourId(String tourId) 
	{
		this.tourId = tourId;
	}
	
	public String getCmtCount() 
	{
		return cmtCount;
	}
	
	public void setCmtCount(String cmtCount) 
	{
		this.cmtCount = cmtCount;
	}
}
