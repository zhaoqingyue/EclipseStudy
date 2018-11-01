/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinEditSignatureActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 编辑签名类
 * @author: zhaoqy
 * @date: 2015-11-9 下午7:49:34
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MenuKaixinEditSignatureActivity extends MenuKaixinBaseActivity 
{
	private LinearLayout mParent;
	private TextView mCannel;
	private TextView mTitle;
	private TextView mSubmit;
	private EditText mContent;
	private Button   mFace;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_edit_signature);
		findViewById();
		setListener();
	}

	private void findViewById() 
	{
		mParent = (LinearLayout) findViewById(R.id.editsignature_parent);
		mCannel = (TextView) findViewById(R.id.id_title_left_text);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mSubmit = (TextView) findViewById(R.id.id_title_right_text);
		mContent = (EditText) findViewById(R.id.editsignature_content);
		mFace = (Button) findViewById(R.id.editsignature_face);
		
		mCannel.setVisibility(View.VISIBLE);
		mCannel.setText("取消");
		mTitle.setText("编辑个性签名");
		mSubmit.setVisibility(View.VISIBLE);
		mSubmit.setText("提交");
	}

	private void setListener() 
	{
		mCannel.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//关闭当前界面,并返回更新信息
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		mSubmit.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//获取当前输入框内容
				String content = mContent.getText().toString().trim();
				//内容为空时显示提示对话框,不为空则返回更新信息
				if (TextUtils.isEmpty(content)) 
				{
					//显示提示对话框
					new AlertDialog.Builder(MenuKaixinEditSignatureActivity.this)
					.setTitle("开心网")
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setMessage("状态信息不能为空")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int which) 
						{
							dialog.dismiss();
						}
					}).create().show();
				} 
				else 
				{
					//关闭当前界面,并返回更新信息
					Intent intent = new Intent();
					intent.putExtra("signature", content);
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

	public void onBackPressed() 
	{
		// 关闭当前界面,并返回更新信息
		setResult(RESULT_CANCELED);
		finish();
	}
}
