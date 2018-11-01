package com.zhaoqy.app.demo.page.vmall.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class VmallSQLeteHelper extends SQLiteOpenHelper 
{
	public VmallSQLeteHelper(Context context, String name, CursorFactory factory, int version) 
	{
		super(context, name, factory, version);
	}

	public VmallSQLeteHelper(Context context) 
	{
		super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) 
	{
		String sql = "create table " + Constant.TABLE_NAME + " (" + Constant._ID + " varchar(225) primary key," + Constant.CONTENT + " text)";
		db.execSQL(sql);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
	}
}
