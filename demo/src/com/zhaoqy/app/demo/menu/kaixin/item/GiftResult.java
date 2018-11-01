/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: GiftResult.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.item
 * @Description: 礼物
 * @author: zhaoqy
 * @date: 2015-11-6 上午10:41:15
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.item;

import android.os.Parcel;
import android.os.Parcelable;

public class GiftResult implements Parcelable 
{
	private String gid;     //礼物的编号
	private String name;    //礼物的名称
	private String content; //礼物的赠言

	public String getGid() 
	{
		return gid;
	}

	public void setGid(String gid) 
	{
		this.gid = gid;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getContent() 
	{
		return content;
	}

	public void setContent(String content) 
	{
		this.content = content;
	}

	public int describeContents() 
	{
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeString(gid);
		dest.writeString(name);
		dest.writeString(content);
	}

	public static final Parcelable.Creator<GiftResult> CREATOR = new Parcelable.Creator<GiftResult>() 
	{
		public GiftResult createFromParcel(Parcel source) 
		{
			GiftResult result = new GiftResult();
			result.setGid(source.readString());
			result.setName(source.readString());
			result.setContent(source.readString());
			return result;
		}

		public GiftResult[] newArray(int size) 
		{
			return new GiftResult[size];
		}
	};
}
