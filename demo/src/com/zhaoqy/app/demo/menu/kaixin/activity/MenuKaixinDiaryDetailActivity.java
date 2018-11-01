/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinDiaryDetailActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 日记内容显示
 * @author: zhaoqy
 * @date: 2015-11-9 下午9:24:30
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.DiaryResult;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuKaixinDiaryDetailActivity extends MenuKaixinBaseActivity 
{
	private ImageView mBack;
	private TextView  mTitle;
	private TextView mDiaryTitle;
	private TextView mDiaryTime;
	private TextView mDiaryContent;
	private Button   mComment;
	private Button   mRepost;
	private String   mUid;  //日记所属用户的ID
	private String   mName; //日记所属用户的姓名
	private DiaryResult    mDiaryResult; //日记的具体内容
	private ProgressDialog mDialog;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_diary_detail);
		findViewById();
		setListener();
		init();
	}

	private void findViewById() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mDiaryTitle = (TextView) findViewById(R.id.diarydetail_title);
		mDiaryTime = (TextView) findViewById(R.id.diarydetail_time);
		mDiaryContent = (TextView) findViewById(R.id.diarydetail_content);
		mComment = (Button) findViewById(R.id.diarydetail_comment);
		mRepost = (Button) findViewById(R.id.diarydetail_repost);
		mBack.setVisibility(View.VISIBLE);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//关闭当前界面
				finish();
			}
		});
		mRepost.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//显示发送转帖进度条,并在500ms后隐藏并显示提示信息
				mDialog.show();
				handler.sendEmptyMessageDelayed(0, 500);
			}
		});
	}

	private void init() 
	{
		//初始化进度条
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("正在发送转帖请求...");
		//获取当前日记所属用户的ID和姓名以及日记内容
		mUid = getIntent().getStringExtra("uid");
		mName = getIntent().getStringExtra("name");
		mDiaryResult = getIntent().getParcelableExtra("result");

		//根据用户的ID显示不同的内容,如果是当前用户则不显示转帖
		if (mUid == null) 
		{
			mTitle.setText("我的日记");
			mRepost.setVisibility(View.GONE);
		} 
		else 
		{
			mTitle.setText(mName + "的日记");
			mRepost.setVisibility(View.VISIBLE);
		}
		//添加日记的具体内容
		mDiaryTitle.setText(mDiaryResult.getTitle());
		mDiaryTime.setText(mDiaryResult.getTime());
		mDiaryContent.setText(new TextUtil(mApplication).replace(mDiaryResult.getContent()));
		mComment.setText(mDiaryResult.getComment_count() + "");
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			//如果进度条存在则隐藏并显示提示信息
			if (mDialog != null && mDialog.isShowing()) 
			{
				mDialog.dismiss();
				Toast.makeText(MenuKaixinDiaryDetailActivity.this, "转帖成功!你的好友会通过好友状态看到此转帖", Toast.LENGTH_SHORT).show();
			}
		}
	};
}
