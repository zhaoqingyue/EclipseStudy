/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinPhotoReplyActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 图片评论回复
 * @author: zhaoqy
 * @date: 2015-11-10 下午1:59:00
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;
import com.zhaoqy.app.demo.menu.kaixin.util.Utils;

public class MenuKaixinPhotoReplyActivity extends MenuKaixinBaseActivity 
{
	private LinearLayout mParent;
	private Context  mContext;
	private TextView mCancel;
	private TextView mTitle;
	private TextView mReply;
	private EditText mContent;
	private Button   mFace;
	private Button   mAt;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_photo_reply);
		mContext = this;
		
		findViewById();
		setListener();
	}

	private void findViewById() 
	{
		mParent = (LinearLayout) findViewById(R.id.photoreply_parent);
		mCancel = (TextView) findViewById(R.id.id_title_left_text);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mReply = (TextView) findViewById(R.id.id_title_right_text);
		mContent = (EditText) findViewById(R.id.photoreply_content);
		mFace = (Button) findViewById(R.id.photoreply_face);
		mAt = (Button) findViewById(R.id.photoreply_at);
		
		mCancel.setVisibility(View.VISIBLE);
		mCancel.setText("取消");
		mTitle.setText("回复");
		mReply.setVisibility(View.VISIBLE);
		mReply.setText("回复");
	}

	private void setListener() 
	{
		mCancel.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//关闭当前界面,返回消息
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		mReply.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//获取当前回复内容
				String content = mContent.getText().toString().trim();
				//回复内容为空时,显示提示对话框,否则关闭当前界面,返回回复的数据
				if (TextUtils.isEmpty(content)) 
				{
					new AlertDialog.Builder(mContext)
					.setTitle("开心网")
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setMessage("回复内容不能为空")
					.setPositiveButton("确定",
					new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int which) 
						{
							dialog.dismiss();
						}
					}).create().show();
				} 
				else 
				{
					Bundle bundle = new Bundle();
					bundle.putString("uid", "user");
					bundle.putString("name", "我");
					bundle.putString("avatar", "-1");
					bundle.putString("time", Utils.getTime(mContext));
					bundle.putString("content", content);
					Intent intent = new Intent();
					intent.putExtra("result", bundle);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
		mFace.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				//显示表情对话框
				showFace(mParent);
			}
		});
		mAt.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				Toast.makeText(mContext, "暂时不支持@功能", Toast.LENGTH_SHORT).show();
			}
		});
		mFaceGridView.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				//获取当前光标位置
				int currentPosition = mContent.getSelectionStart();
				//插入带有表情的文本
				mContent.setText(new TextUtil(mApplication).replace(mContent.getText().insert(currentPosition, mApplication.mFacesText.get(position))));
				//关闭表情对话框
				dismissFace();
			}
		});
		mFaceClose.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//关闭表情对话框
				dismissFace();
			}
		});
	}

	public void onBackPressed() 
	{
		//关闭当前界面,返回消息
		setResult(RESULT_CANCELED);
		finish();
	}
}
