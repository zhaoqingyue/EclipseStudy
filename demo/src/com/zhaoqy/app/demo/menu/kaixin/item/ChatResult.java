/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: ChatResult.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.item
 * @Description: 聊天记录的数据实体(模拟数据不包含发送的人的姓名和头像)
 * @author: zhaoqy
 * @date: 2015-11-6 上午10:44:30
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.item;

public class ChatResult 
{
	private String time;    //发送时间
	private String content; //发送内容
	private int    type;    //内容类型(1-发出,2-接收)

	public String getTime() 
	{
		return time;
	}

	public void setTime(String time) 
	{
		this.time = time;
	}

	public String getContent() 
	{
		return content;
	}

	public void setContent(String content) 
	{
		this.content = content;
	}

	public int getType() 
	{
		return type;
	}

	public void setType(int type) 
	{
		this.type = type;
	}
}
