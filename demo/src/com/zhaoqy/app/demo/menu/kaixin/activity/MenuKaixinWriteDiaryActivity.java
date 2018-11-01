/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinWriteDiaryActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 写日记
 * @author: zhaoqy
 * @date: 2015-11-9 下午9:15:01
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.DiaryResult;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;
import com.zhaoqy.app.demo.menu.kaixin.util.Utils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MenuKaixinWriteDiaryActivity extends MenuKaixinBaseActivity 
{
	private LinearLayout mParent;
	private Context  mContext;
	private TextView mCancel;
	private TextView mTitle;
	private TextView mSubmit;
	private EditText mDiaryTitle;
	private EditText mContent;
	private Button   mFace;
	private Button   mPhoto;
	private Button   mLocation;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_write_diary);
		mContext = this;
		
		findViewById();
		setListener();
		init();
	}

	private void findViewById() 
	{
		mParent = (LinearLayout) findViewById(R.id.writediary_parent);
		mCancel = (TextView) findViewById(R.id.id_title_left_text);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mSubmit = (TextView) findViewById(R.id.id_title_right_text);
		mDiaryTitle = (EditText) findViewById(R.id.writediary_title);
		mContent = (EditText) findViewById(R.id.writediary_content);
		mFace = (Button) findViewById(R.id.writediary_face);
		mPhoto = (Button) findViewById(R.id.writediary_photo);
		mLocation = (Button) findViewById(R.id.writediary_location);
		
		mCancel.setVisibility(View.VISIBLE);
		mCancel.setText("取消");
		mTitle.setText("写新日记");
		mSubmit.setVisibility(View.VISIBLE);
		mSubmit.setText("发表");
	}

	private void setListener() 
	{
		mCancel.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//显示退出对话框
				showExitDialog();
			}
		});
		mSubmit.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//获取当前标题和内容数据
				String title = mDiaryTitle.getText().toString().trim();
				String content = mContent.getText().toString().trim();
				//标题和内容为空时提示消息,否则则关闭当前界面并返回数据
				if (TextUtils.isEmpty(title)) 
				{
					showErrorDialog("日记标题不能为空");
				} 
				else if (TextUtils.isEmpty(content)) 
				{
					showErrorDialog("日记内容不能为空");
				} 
				else 
				{
					DiaryResult result = new DiaryResult();
					result.setTitle(title);
					result.setContent(content);
					result.setTime(Utils.formatDate(mContext, System.currentTimeMillis()));
					mApplication.mMyDiaryResults.add(0, result);
					mApplication.mDraft_DiaryTitle = null;
					mApplication.mDraft_DiaryContent = null;
					setResult(RESULT_OK);
					finish();
				}
			}
		});
		mDiaryTitle.setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			public void onFocusChange(View v, boolean hasFocus) 
			{
				// 当标题获取到焦点时,表情不可用
				if (hasFocus) 
				{
					mFace.setBackgroundResource(R.drawable.menu_kaixin_write_face_disable);
					mFace.setEnabled(false);
				} 
				else 
				{
					mFace.setBackgroundResource(R.drawable.menu_kaixin_write_face_selector);
					mFace.setEnabled(true);
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
		mPhoto.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
			}
		});
		mLocation.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
			}
		});
		mFaceGridView.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				//获取当前光标所在位置
				int currentPosition = mContent.getSelectionStart();
				//添加含有表情的文本
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

	private void init() 
	{
		// 查看当前草稿箱是否有数据,有的话则显示
		if (!(mApplication.mDraft_DiaryTitle == null)) 
		{
			mDiaryTitle.setText(mApplication.mDraft_DiaryTitle);
		}
		if (!(mApplication.mDraft_DiaryContent == null)) 
		{
			mContent.setText(mApplication.mDraft_DiaryContent);
		}
	}

	/**
	 * 提示对话框
	 * @param message
	 */
	private void showErrorDialog(String message) 
	{
		new AlertDialog.Builder(mContext).setTitle("开心网")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setMessage(message)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		}).create().show();
	}

	/**
	 * 退出对话框
	 */
	private void showExitDialog() 
	{
		final String title = mDiaryTitle.getText().toString().trim();
		final String content = mContent.getText().toString().trim();
		if ((!TextUtils.isEmpty(title)) || (!TextUtils.isEmpty(content))) 
		{
			new AlertDialog.Builder(mContext)
			.setTitle("退出正在编辑的日记")
			.setItems(new String[] { "保存为日记草稿", "不保存" },
			new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					switch (which) 
					{
						case 0:
							mApplication.mDraft_DiaryTitle = title;
							mApplication.mDraft_DiaryContent = content;
							break;
						case 1:
							mApplication.mDraft_DiaryTitle = null;
							mApplication.mDraft_DiaryContent = null;
							break;
					}
					setResult(RESULT_CANCELED);
					finish();
				}
			})
			.setNegativeButton("取消",
			new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.cancel();
				}
			}).create().show();
		}
		else 
		{
			mApplication.mDraft_DiaryTitle = null;
			mApplication.mDraft_DiaryContent = null;
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	public void onBackPressed() 
	{
		//显示退出对话框
		showExitDialog();
	}
}
