/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyInterceptNoteActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.safety.activity
 * @Description: 广告拦截日志
 * @author: zhaoqy
 * @date: 2015-12-9 下午9:31:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.adapter.InterceptNoteAdapter;

public class SafetyInterceptNoteActivity extends Activity implements OnClickListener
{
	private InterceptNoteAdapter mAdapter;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private ListView  mListView;
	private ImageView mLoad;
    private Button    mDelete;
    private Button    mSure;
    private Button    mCancel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_intercept_note);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mListView = (ListView) findViewById(R.id.ad_note_lv);
		mLoad = (ImageView) findViewById(R.id.ad_note_load);
		mDelete = (Button) findViewById(R.id.ad_note_delete);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mDelete.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText("广告拦截日志");
		mBack.setVisibility(View.VISIBLE);
		
		mAdapter = new InterceptNoteAdapter(mContext, SafetyInterceptActivity.mAppList);
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
		case R.id.ad_note_delete:
		{
			final Dialog dialog = new Dialog(mContext);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
			Window window = dialog.getWindow();
			window.setContentView(R.layout.dialog_safety_intercept_note);	
			mSure = (Button)window.findViewById(R.id.ad_note_dialog_ok);
			mCancel = (Button)window.findViewById(R.id.ad_note_dialog_no);
			mSure.setOnClickListener(new OnClickListener()
			{				
				@Override
				public void onClick(View v) 
				{
					dialog.dismiss();
					mListView.setVisibility(View.GONE);
					mLoad.setVisibility(View.VISIBLE);
					startAnim();
					new Thread(new Runnable() 
					{
						@Override
						public void run() 
						{
							mHandler.sendEmptyMessageDelayed(1, 500);
						}
					}).start();
				}
			});
			mCancel.setOnClickListener(new OnClickListener() 
			{				
				@Override
				public void onClick(View v) 
				{
					dialog.dismiss();
				}
			});
			break;
		}
		default:
			break;
		}
	}
	
	private void startAnim() 
	{
		Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.safety_intercept_load);
		mLoad.startAnimation(anim);
	}

	private void stopAnim() 
	{
		mLoad.clearAnimation();
		mLoad.setVisibility(View.GONE);
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case 1:
			{
				stopAnim();
				break;
			}
			default:
				break;
			}
		}
	};
}
