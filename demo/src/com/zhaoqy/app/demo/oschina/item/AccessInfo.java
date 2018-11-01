/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: AccessInfo.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.item
 * @Description: 微博认证信息类：OAuth认证返回的数据集合
 * @author: zhaoqy
 * @date: 2015-11-17 下午4:13:37
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.item;

public class AccessInfo 
{
	private String userID;
	private String accessToken;
	private String accessSecret;
	private long expiresIn;

	public String getUserID() 
	{
		return userID;
	}

	public void setUserID(String userID) 
	{
		this.userID = userID;
	}

	public String getAccessToken() 
	{
		return accessToken;
	}

	public void setAccessToken(String accessToken) 
	{
		this.accessToken = accessToken;
	}

	public String getAccessSecret() 
	{
		return accessSecret;
	}

	public void setAccessSecret(String accessSecret) 
	{
		this.accessSecret = accessSecret;
	}

	public long getExpiresIn() 
	{
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) 
	{
		this.expiresIn = expiresIn;
	}
}
