/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: FriendsResult.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.item
 * @Description: 好友数据的实体
 * @author: zhaoqy
 * @date: 2015-11-6 上午10:48:20
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.item;

public class FriendsResult 
{
	private String uid;         //好友的编号
	private int avatar;         //好友的头像编号
	private String name;        //好友的姓名
	private String name_pinyin; //好友的姓名拼音
	private String name_first;  //好友姓名的首字母

	public String getUid() 
	{
		return uid;
	}

	public void setUid(String uid) 
	{
		this.uid = uid;
	}

	public int getAvatar() 
	{
		return avatar;
	}

	public void setAvatar(int avatar) 
	{
		this.avatar = avatar;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getName_pinyin() 
	{
		return name_pinyin;
	}

	public void setName_pinyin(String name_pinyin) 
	{
		this.name_pinyin = name_pinyin;
	}

	public String getName_first() 
	{
		return name_first;
	}

	public void setName_first(String name_first) 
	{
		this.name_first = name_first;
	}
}
