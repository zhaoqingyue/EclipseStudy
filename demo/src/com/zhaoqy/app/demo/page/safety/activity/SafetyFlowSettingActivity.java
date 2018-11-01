/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyFlowSettingActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 流量设置
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

public class SafetyFlowSettingActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private View      mFlow;
	private Button    mSure;
	private Button    mCancel;
	private TextView  mTextView;
	private EditText  mEditText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_flow_setting);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mFlow = findViewById(R.id.flow_setting_rl_allf);
		mTextView = (TextView) findViewById(R.id.flow_setting_tv_allflow);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mFlow.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("流量设置");
		mBack.setVisibility(View.VISIBLE);
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
		case R.id.flow_setting_rl_allf:
		{
			final Dialog dialog = new Dialog(mContext);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
			Window window = dialog.getWindow();
	        window.setContentView(R.layout.dialog_safety_flow_setting);	
	        mEditText=(EditText) window.findViewById(R.id.flow_setting_et_allflow);
	        showSoftKeyboard(mEditText, mContext);
	        mEditText.setFocusable(true);
	        mSure = (Button)window.findViewById(R.id.flow_setting_allflow_ok);
	        mCancel = (Button)window.findViewById(R.id.flow_setting_allflow_no);
			mSure.setOnClickListener(new OnClickListener() 
			{				
				@Override
				public void onClick(View v) 
				{
					mTextView.setText(mEditText.getText());	
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
			break;
		}
		default:
			break;
		}
	}
	
	private void showSoftKeyboard(EditText et, Context context)
	{
		InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(et,InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
	private void closeKeyboard(EditText et, Context context) 
	{  
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);  
	} 
}
