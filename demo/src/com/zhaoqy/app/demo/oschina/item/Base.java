/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: Base.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.item
 * @Description: 实体基类：实现序列化
 * @author: zhaoqy
 * @date: 2015-11-18 上午11:07:18
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Base implements Serializable 
{
	public final static String UTF8 = "UTF-8";
	public final static String NODE_ROOT = "oschina";
	
	protected Notice notice;

	public Notice getNotice() 
	{
		return notice;
	}

	public void setNotice(Notice notice) 
	{
		this.notice = notice;
	}
}
