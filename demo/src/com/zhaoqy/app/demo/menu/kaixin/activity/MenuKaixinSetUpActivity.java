/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinSetUpActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 设置类
 * @author: zhaoqy
 * @date: 2015-11-9 下午1:37:01
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.login.kaixin.LoginKaixinActivity;

public class MenuKaixinSetUpActivity extends MenuKaixinBaseActivity 
{
	private Context      mContext;
	private ImageView    mBack;
	private TextView     mTitle;
	private LinearLayout mExport;
	private LinearLayout mSetUp;
	private Button       mCancelAccount;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_setup);
		mContext = this;
		
		findViewById();
		setListener();
	}

	private void findViewById() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mExport = (LinearLayout) findViewById(R.id.setup_export);
		mSetUp = (LinearLayout) findViewById(R.id.setup_setup);
		mCancelAccount = (Button) findViewById(R.id.setup_cancel_account);
		mTitle.setText("账户设置");
	}

	private void setListener() 
	{
		mBack.setVisibility(View.VISIBLE);
		mBack.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//关闭当前界面
				finish();
			}
		});
		mExport.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//跳转到导入好友至手机通讯录类
				Intent intent = new Intent(mContext, MenuKaixinSetUpExportActivity.class);
				startActivity(intent);
			}
		});
		mSetUp.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//跳转到设置类
				Intent intent = new Intent(mContext, MenuKaixinSetUpSettingsActivity.class);
				startActivity(intent);
			}
		});
		mCancelAccount.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//注销对话框
				CancelAccountDialog();
			}
		});
	}

	/**
	 * 注销对话框
	 */
	private void CancelAccountDialog() 
	{
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("注销登录");
		builder.setMessage("确定注销登录吗?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				startActivity(new Intent(mContext, LoginKaixinActivity.class));
				finish();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		builder.create().show();
	}
}
