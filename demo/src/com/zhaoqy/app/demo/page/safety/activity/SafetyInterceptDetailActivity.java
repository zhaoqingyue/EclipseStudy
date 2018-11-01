/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyInterceptDetailActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 广告详情
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.item.AppInfo;

public class SafetyInterceptDetailActivity extends Activity implements OnClickListener
{
	public static boolean mIsUninstall=false;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mRight;
	private TextView  mName;
	private TextView  mAdNum;
	private TextView  mAdPluginName;
	private TextView  mAdPluginCount;
	private ImageView mIcon;
	private View      mAdPlugin;
	private Button    mSend;
	private Button    mCancel;
	private Button    mClose;
	private EditText  mEditText;
	private Button    mUninstall;
	private Button    mReturn;
	private AppInfo   mAppInfo;
	private String    mAdName;
	private int       mPosition;
	private int       mCount ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_intercept_detail);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mRight = (TextView) findViewById(R.id.id_title_right_text);
		
		mUninstall = (Button) findViewById(R.id.ad_detail_Uninstall);
		mReturn = (Button) findViewById(R.id.ad_detail_return);
		mName = (TextView) findViewById(R.id.ad_detail_name);
		mIcon = (ImageView) findViewById(R.id.ad_detail_icon);
		mAdNum = (TextView) findViewById(R.id.ad_detail_num);
		mAdPlugin = findViewById(R.id.ad_detail_rl8);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mRight.setOnClickListener(this);
		mUninstall.setOnClickListener(this);
		mReturn.setOnClickListener(this);
		mAdPlugin.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("广告详情");
		mBack.setVisibility(View.VISIBLE);
		mRight.setVisibility(View.VISIBLE);
		mRight.setTextColor(0xFF45C01A);
		mRight.setText("反馈");
		
		Intent intent = getIntent();
		mPosition = intent.getExtras().getInt("position");
		mCount = intent.getExtras().getInt("count");
		mAdName = intent.getExtras().getString("adname");
		mAppInfo = SafetyInterceptActivity.mAppList.get(mPosition);
		mName.setText(mAppInfo.getAppName());
		mIcon.setImageDrawable(mAppInfo.getAppIcon());
		mAdNum.setText("含有" + mCount + "款广告插件");
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
		case R.id.id_title_right_text:
		{
			showDialogFanKui();
			break;
		}
		case R.id.ad_detail_Uninstall:
		{
			Uri uri = Uri.parse("package:" + mAppInfo.getPackageName());
			Intent intent = new Intent(Intent.ACTION_DELETE, uri);
			startActivity(intent);
			mIsUninstall = true;
			break;
		}
		case R.id.ad_detail_return:
		{
			finish();
			break;
		}
		case R.id.ad_detail_rl8:
		{
			showDialogName();
			break;
		}
		default:
			break;
		}
	}
	
	public void showDialogFanKui()
	{
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_safety_intercept_feedback);	
		mEditText = (EditText) window.findViewById(R.id.ad_fankui_et);
		mEditText.setFocusable(false);
		mSend = (Button)window.findViewById(R.id.ad_fankui_tijiao);
		mCancel = (Button)window.findViewById(R.id.ad_fankui_quxiao);
		mEditText.setOnClickListener(new OnClickListener() 
		{				
			@Override
			public void onClick(View v) 
			{
				mEditText.setFocusableInTouchMode(true);
				showSoftKeyboard(mEditText, mContext);
			}
		});
		mSend.setOnClickListener(new OnClickListener() 
		{				
			@Override
			public void onClick(View v) 
			{
				dialog.dismiss();
				closeKeyboard(mEditText, mContext);
			}
		});
		mCancel.setOnClickListener(new OnClickListener() 
		{				
			@Override
			public void onClick(View v) 
			{
				dialog.dismiss();
				closeKeyboard(mEditText, mContext);
			}
		});

	}
	public void showDialogName()
	{
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_safety_intercept_detail);	
		mClose = (Button)window.findViewById(R.id.ad_datail_show_ad_off);
		mAdPluginName = (TextView)window.findViewById(R.id.ad_datail_show_ad_show);
		mAdPluginCount = (TextView)window.findViewById(R.id.ad_datail_show_ad_count);
		mAdPluginName.setText(mAdName);
		mAdPluginCount.setText(mCount + "款");
		mClose.setOnClickListener(new OnClickListener() 
		{				
			@Override
			public void onClick(View v) 
			{
				dialog.dismiss();
			}
		});
	}
	
	public static void showSoftKeyboard(EditText et, Context context)
	{
		InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(et,InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public static void closeKeyboard(EditText et, Context context) 
	{  
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);  
	} 
}
