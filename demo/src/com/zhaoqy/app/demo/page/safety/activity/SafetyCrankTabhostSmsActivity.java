/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyCrankTabhost0Activity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.activity
 * @Description: 垃圾短信
 * @author: zhaoqy
 * @date: 2015-12-11 上午11:35:31
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import java.util.ArrayList;
import java.util.List;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.adapter.CrankSmsAdapter;
import com.zhaoqy.app.demo.page.safety.item.CrankSmsItem;
import com.zhaoqy.app.demo.page.safety.util.SmsDbHelper;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class SafetyCrankTabhostSmsActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener
{
	public static CrankSmsAdapter    mAdapter;
	public static List<CrankSmsItem> mShowlist;
	public static ListView           mListShowSms;
	public static LinearLayout       mMissLayout;
	private Context      mContext;
	private SmsDbHelper  mHelper;
	private CrankSmsItem mCrankSmsItem;
	private ImageView    mImageView;
	private String       mBody;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_crank_tabhost_sms);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mImageView = (ImageView) findViewById(R.id.crank_sms_imageview);
		mListShowSms = (ListView) findViewById(R.id.crank_sms_listview);
		mMissLayout = (LinearLayout) findViewById(R.id.crank_sms_missframlayout);
	}
	
	private void setListener() 
	{
		mImageView.setOnClickListener(this);
		mListShowSms.setOnItemClickListener(this);
		mListShowSms.setOnItemLongClickListener(this);
	}
	
	private void initData() 
	{
		mShowlist = showlist();
		mAdapter = new CrankSmsAdapter(mContext, mShowlist);
        mListShowSms.setAdapter(mAdapter);
	}
	
	//显示举报短息
	public List<CrankSmsItem> showlist() 
	{
		mHelper = new SmsDbHelper(mContext);
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.query("sms", null, null, null, null, null, null);
		List<CrankSmsItem> list = new ArrayList<CrankSmsItem>();
		if (cursor.moveToFirst()) 
		{
			int addrIdx = cursor.getColumnIndex("photonum");
			int bodyIdx = cursor.getColumnIndex("content");
			do 
			{
				String addr = cursor.getString(addrIdx);
				mBody = cursor.getString(bodyIdx);
				mCrankSmsItem = new CrankSmsItem();
				mCrankSmsItem.setPhoto(addr);
				mCrankSmsItem.setBody(mBody);
				list.add(mCrankSmsItem);
			} while (cursor.moveToNext());
		}
		return list;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) 
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("删除数据");
		dialog.setMessage("确定删除本条数据?");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() 
		{
			@SuppressLint("ShowToast")
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				mHelper = new SmsDbHelper(mContext);
				SQLiteDatabase db = mHelper.getReadableDatabase();
				db.delete("sms", "content=?", new String[] { mShowlist.get(position).getBody() });
				mShowlist.remove(position);
				mAdapter.notifyDataSetChanged();
				Toast.makeText(mContext, "删除成功", 1).show();
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		dialog.create();
		dialog.show();
		return false;
	}

	@SuppressWarnings({ "deprecation" })
	@SuppressLint("InlinedApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) 
	{
		View contentView = View.inflate(mContext, R.layout.view_safety_crank_sms_pop, null);
		RelativeLayout dele = (RelativeLayout) contentView.findViewById(R.id.crank_sms_dele);
		final PopupWindow pWindow = new PopupWindow(contentView,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		pWindow.setFocusable(true);
		Bitmap b = null;
		pWindow.setBackgroundDrawable(new BitmapDrawable(b));
		pWindow.setOutsideTouchable(true);
		pWindow.showAsDropDown(view);
		dele.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				mHelper = new SmsDbHelper(mContext);
				SQLiteDatabase db = mHelper.getReadableDatabase();
				db.delete("sms", "content=?", new String[] { mShowlist.get(position).getBody() });
				mShowlist.remove(position);
				mAdapter.notifyDataSetChanged();
				pWindow.dismiss();
			}
		});
		RelativeLayout weibo = (RelativeLayout)contentView.findViewById(R.id.crank_sms_weibo);
		weibo.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
			}
		});
	}

	@Override
	public void onClick(View v) 
	{
		Intent intent = new Intent(mContext, SafetyCrankTabhostSmsReportActivity.class);
		startActivity(intent);
	}
	
	public static void misslayout() 
	{
		mMissLayout.setVisibility(View.VISIBLE);
		mListShowSms.setVisibility(View.GONE);
	}

	public static void listShowSms() 
	{
		mListShowSms.setVisibility(View.VISIBLE);
		mMissLayout.setVisibility(View.GONE);
	}
}
