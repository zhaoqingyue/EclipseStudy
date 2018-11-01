package com.zhaoqy.app.demo.notebook.billnotes.db;

import java.util.ArrayList;
import java.util.List;
import com.zhaoqy.app.demo.notebook.billnotes.item.BillItem;
import com.zhaoqy.app.demo.notebook.billnotes.item.NoteItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserManager 
{
	public static final String TOTE_TITLE="title";
	public static final String TOTE_CONTENT="content";
	public static final String TOTE_DTIMES="dtimes";
	
	public static final String BILL_BILL="bill";
	public static final String BILL_IMG="img";
	public static final String BILL_BILLTYPE="billtype";
	public static final String BILL_SATTE="billstate";
	public static final String BILL_TIME="logtime";
	public static final String BILL_ID="id";

	public static final String ACCOUNT_NAME="accountname";
	public static final String ACCOUNT_MEMORY="money";

    DBOpenHelper mHelper;
	private BillItem     mItem;
	private String       mMoney;

	public UserManager(Context context) 
	{
		mHelper = new DBOpenHelper(context, "", null, 1);
	}

	public Long insertNote(NoteItem noteItem) 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TOTE_TITLE, noteItem.getTitle());
		values.put(TOTE_CONTENT, noteItem.getContent());
		values.put(TOTE_DTIMES, noteItem.getTime());
		long e = db.insert("note", null, values);
		db.close();
		return e;
	}

	public Long insertBill(BillItem billItem) 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(BILL_BILL, billItem.getBill());
		values.put(BILL_IMG, billItem.getImg());
		values.put(BILL_BILLTYPE, billItem.getType());
		values.put(BILL_SATTE, billItem.getState());
		values.put(BILL_TIME, billItem.getTime());
		long e = db.insert("bill", null, values);
		db.close();
		return e;
	}

	public Long delectNote(String id) 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		long e = db.delete("note", "id = ?", new String[] { id });
		db.close();
		return e;
	}

	public long updateNote(String id, ContentValues values) 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		long e = db.update("note", values, " id = ? ", new String[] { id });
		db.close();
		return e;
	}

	public long updateBill(String id, ContentValues values) 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		long e = db.update("bill", values, " id = ? ", new String[] { id });
		db.close();
		return e;
	}

	public long updateAccount(BillItem billItem) 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		Double oldmoney = Double.valueOf(getAccount(billItem.getName()).getBill().toString().trim());
		Double newmoney = Double.valueOf(billItem.getBill().toString().trim());
		int state = Integer.parseInt(billItem.getState().toString().trim());
		if (state == 0) 
		{
			mMoney = Double.toString(oldmoney - newmoney);
		} 
		else if (state == 1) 
		{
			mMoney = Double.toString(oldmoney + newmoney);
		}
		ContentValues values = new ContentValues();
		values.put("money", mMoney);
		long e = db.update("account", values, " accountname = ? ", new String[] { billItem.getName() });
		db.close();
		return e;
	}

	public List<NoteItem> getNotes() 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		Cursor cursor = db.query("note", null, null, null, null, null, "id desc");
		List<NoteItem> list = new ArrayList<NoteItem>();
		while (cursor.moveToNext()) 
		{
			String title = cursor.getString(cursor.getColumnIndex(TOTE_TITLE));
			String content = cursor.getString(cursor.getColumnIndex(TOTE_CONTENT));
			String dtimes = cursor.getString(cursor.getColumnIndex(TOTE_DTIMES));
			NoteItem entity = new NoteItem(title, content, dtimes);
			list.add(entity);
		}
		return list;
	}

	public List<BillItem> getBills() 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		Cursor cursor = db.query("bill", null, null, null, null, null,"id desc");
		List<BillItem> list = new ArrayList<BillItem>();
		while (cursor.moveToNext()) 
		{
			String dbill = cursor.getString(cursor.getColumnIndex(BILL_BILL));
			String dimg = cursor.getString(cursor.getColumnIndex(BILL_IMG));
			String dbstate = cursor.getString(cursor.getColumnIndex(BILL_SATTE));
			String dbtype = cursor.getString(cursor.getColumnIndex(BILL_BILLTYPE));
			BillItem entity = new BillItem();
			entity.setType(dbtype);
			entity.setState(dbstate);
			entity.setBill(dbill);
			entity.setImg(Integer.parseInt(dimg));
			list.add(entity);
		}
		return list;
	}

	public List<BillItem> getAccount() 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		Cursor cursor = db.query("account", null, null, null, null, null,"id asc");
		List<BillItem> list = new ArrayList<BillItem>();
		while (cursor.moveToNext()) 
		{
			String dbmoney = cursor.getString(cursor.getColumnIndex(ACCOUNT_MEMORY));
			String dbaccountname = cursor.getString(cursor.getColumnIndex(ACCOUNT_NAME));
			BillItem entity = new BillItem();
			entity.setName(dbaccountname);
			entity.setBill(dbmoney);
			list.add(entity);
		}
		return list;
	}

	public NoteItem getNotes(String id) 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		Cursor cursor = db.query("note", new String[] { TOTE_TITLE, TOTE_CONTENT, TOTE_DTIMES }, "id = ? ", new String[] { id },
				null, null, null);
		NoteItem noteItem = null;
		if (cursor.moveToFirst()) 
		{
			String title = cursor.getString(cursor.getColumnIndex(TOTE_TITLE));
			String content = cursor.getString(cursor.getColumnIndex(TOTE_CONTENT));
			String time = cursor.getString(cursor.getColumnIndex(TOTE_DTIMES));
			noteItem = new NoteItem(title, content, time);
		}
		return noteItem;
	}

	public BillItem getAccount(String accountname) 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		Cursor cursor = db.query("account", null, "accountname = ? ", new String[] { accountname }, null, null, null);
		BillItem billItem = null;
		if (cursor.moveToFirst()) 
		{
			String dbmoney = cursor.getString(cursor.getColumnIndex(ACCOUNT_MEMORY));
			billItem = new BillItem();
			billItem.setBill(dbmoney);
		}
		return billItem;
	}

	public BillItem getBills(String billtype, String time) 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		Cursor cursor = db.query("bill", null, "billtype=? and logtime=? ", new String[] { billtype, time }, null, null, null);
		while (cursor.moveToNext()) 
		{
			String dbill = cursor.getString(cursor.getColumnIndex(BILL_BILL));
			String dimg = cursor.getString(cursor.getColumnIndex(BILL_IMG));
			int img = Integer.parseInt(dimg);
			String dtype = cursor.getString(cursor.getColumnIndex(BILL_BILLTYPE));
			String dstate = cursor.getString(cursor.getColumnIndex(BILL_SATTE));
			String dtime = cursor.getString(cursor.getColumnIndex(BILL_TIME));
			String did = cursor.getString(cursor.getColumnIndex(BILL_ID));
			int id = Integer.parseInt(did);
			mItem = new BillItem();
			mItem.setBill(dbill);
			mItem.setState(dstate);
			mItem.setType(dtype);
			mItem.setId(id);
			mItem.setImg(img);
			mItem.setTime(dtime);
		}
		return mItem;
	}
}
