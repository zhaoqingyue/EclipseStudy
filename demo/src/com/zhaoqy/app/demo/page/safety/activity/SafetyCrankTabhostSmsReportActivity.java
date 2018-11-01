/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyCrankTabhostSmsReportActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 垃圾短信举报
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.adapter.CrankSmsReportAdapter;
import com.zhaoqy.app.demo.page.safety.item.CrankSmsItem;
import com.zhaoqy.app.demo.page.safety.util.SmsDbHelper;

public class SafetyCrankTabhostSmsReportActivity extends Activity implements OnClickListener, OnItemClickListener
{
	public Uri SMS_INBOX = Uri.parse("content://sms/inbox"); //短信收件箱的URI
	public CrankSmsReportAdapter mAdapter;
	private List<CrankSmsItem>   mList;
	private Context     mContext;
	private ImageView   mBack;
	private TextView    mTitle;
	private TextView    mAmsReport;
	private TextView    mAllItem;
	private ListView    mListView;
	private SmsDbHelper mHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_crank_tabhost_sms_report);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mAmsReport = (TextView) findViewById(R.id.crank_sms_report);
		mAllItem = (TextView) findViewById(R.id.crank_sms_allsms);
		mListView = (ListView) findViewById(R.id.crank_sms_listview2);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mAmsReport.setOnClickListener(this);
		mAllItem.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("举报垃圾短信");
		mBack.setVisibility(View.VISIBLE);
		
		mHelper = new SmsDbHelper(mContext);
		mList = readAllSMS();
		mAdapter = new CrankSmsReportAdapter(mContext, mList);
		mListView.setAdapter(mAdapter);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		case R.id.crank_sms_report:
		{
			getsmslist();
			finish();
			break;
		}
		case R.id.crank_sms_allsms:
		{
			for (int i = 0; i < mList.size(); i++) 
			{
				mList.get(i).setFlag(false);
			}
			mAdapter.notifyDataSetChanged();
			break;
		}
		default:
			break;
		}
	}
	
	//获得系统短信并放在显示所有短息list中
	@SuppressWarnings("deprecation")
	private List<CrankSmsItem> readAllSMS() 
	{
		Cursor cursor = managedQuery(SMS_INBOX, new String[] { "address", "body" }, null, null, null);
		List<CrankSmsItem> list2 = new ArrayList<CrankSmsItem>();
		if (cursor.moveToFirst()) 
		{
			int addrIdx = cursor.getColumnIndex("address");
			int bodyIdx = cursor.getColumnIndex("body");
			do 
			{
				String addr = cursor.getString(addrIdx);
				String body = cursor.getString(bodyIdx);
				CrankSmsItem bean = new CrankSmsItem();
				bean.setPhoto(addr);
				bean.setBody(body);
				list2.add(bean);
			} while (cursor.moveToNext());
		}
		return list2;
	}

	//将系统信息表插入到sms表中
	void getsmslist() 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		for (int i = 0; i < mList.size(); i++) 
		{
			boolean b = mList.get(i).getFlag();
			if (!b) 
			{
				String content = mList.get(i).getBody();
				String photonum = mList.get(i).getPhoto();
				ContentValues values = new ContentValues();
				values.put("photonum", photonum);
				values.put("content", content);
				db.insert("sms", null, values);
				SafetyCrankTabhostSmsActivity.mShowlist.add(mList.get(i));
			}
		}
		SafetyCrankTabhostSmsActivity.mAdapter.notifyDataSetChanged();
		db.close();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		CrankSmsItem b = (CrankSmsItem) arg0.getAdapter().getItem(arg2);
		if (b.getFlag()) 
		{
			mList.get(arg2).setFlag(false);
		} 
		else 
		{
			mList.get(arg2).setFlag(true);
		}
		mAdapter.notifyDataSetChanged();
	}
}
