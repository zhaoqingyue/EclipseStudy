/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: Entity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.item
 * @Description: 实体类
 * @author: zhaoqy
 * @date: 2015-11-18 上午11:17:51
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.item;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;

@SuppressLint("SimpleDateFormat")
@SuppressWarnings("serial")
public class Entity extends Base
{
	public final static SimpleDateFormat SDF_IN = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat SDF_OUT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	protected int id;
	protected String cacheKey;

	public int getId() 
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getCacheKey() 
	{
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) 
	{
		this.cacheKey = cacheKey;
	}
}
