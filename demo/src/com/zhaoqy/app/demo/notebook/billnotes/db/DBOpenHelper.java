package com.zhaoqy.app.demo.notebook.billnotes.db;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBOpenHelper extends SQLiteOpenHelper
{
	public DBOpenHelper(Context context, String name, CursorFactory factory, int version) 
	{
		super(context, "user.db", factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("create table note (id integer primary key autoincrement, title varchar(20),content varchar(50),dtimes varchar(20))");
		db.execSQL("create table bill (id integer primary key autoincrement,bill varchar(20), img varchar(20), billtype varchar(20), billstate varchar(20),  logtime varchar(30))");
		db.execSQL("create table account (id integer primary key autoincrement, accountname varchar(20),money varchar(20))");
		
		List<String> accountname = new ArrayList<String>();
		accountname.add("现金");
		accountname.add("储蓄卡");
		accountname.add("信用卡");
		accountname.add("网络账户");
		List<String> money = new ArrayList<String>();
		money.add("0");
		money.add("0");
		money.add("0");
		money.add("0");
		for(int i=0; i<accountname.size(); i++)
		{
			ContentValues values = new ContentValues();
			values.put(UserManager.ACCOUNT_NAME, accountname.get(i));
			values.put(UserManager.ACCOUNT_MEMORY, money.get(i));
			db.insert("account", null, values);
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
	}
}
