/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SmsDbHelper.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.util
 * @Description: 垃圾短信帮助类
 * @author: zhaoqy
 * @date: 2015-12-11 下午2:56:58
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SmsDbHelper extends SQLiteOpenHelper 
{
	public static final String DB_NAME = "sms.db";
	public static final String TABLE_NAME = "sms";
	public static final String _ID = "_id";
	public static final String PHOTONUM = "photonum";
	public static final String CONTENT = "content";
	public static final int VERSION = 1;
	
	public SmsDbHelper(Context context) 
	{
		super(context, DB_NAME, null, VERSION);
	}

	//创建短息表
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PHOTONUM
				   + " TEXT NOT NULL," + CONTENT + " TEXT NOT NULL)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
	}
}
