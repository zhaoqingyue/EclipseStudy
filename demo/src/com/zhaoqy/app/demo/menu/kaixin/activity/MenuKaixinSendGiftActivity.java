/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinSendGiftActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 送礼物
 * @author: zhaoqy
 * @date: 2015-11-10 上午9:18:46
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.util.HashMap;
import java.util.Map;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.GiftResult;
import com.zhaoqy.app.demo.menu.kaixin.util.ActivityForResultUtil;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;

public class MenuKaixinSendGiftActivity extends MenuKaixinBaseActivity 
{
	private LinearLayout mFriendsList;
	private Context   mContext;
	private TextView  mCancel;
	private TextView  mTitle;
	private TextView  mHandsel;
	private EditText  mMessage;
	private ImageView mMessageLine1;
	private ImageView mMessageLine2;
	private ImageView mAddGift;
	private CheckBox  mSendType;
	private CheckBox  mSendSMS;
	private boolean   mIsSelectGift;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_sendgift);
		mContext = this;
		
		findViewById();
		setListener();
		init();
	}

	private void findViewById() 
	{
		mCancel = (TextView) findViewById(R.id.id_title_left_text);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mHandsel = (TextView) findViewById(R.id.id_title_right_text);
		mFriendsList = (LinearLayout) findViewById(R.id.sendgift_friends_list);
		mMessage = (EditText) findViewById(R.id.sendgift_message);
		mMessageLine1 = (ImageView) findViewById(R.id.sendgift_message_line1);
		mMessageLine2 = (ImageView) findViewById(R.id.sendgift_message_line2);
		mAddGift = (ImageView) findViewById(R.id.sendgift_add_gift);
		mSendType = (CheckBox) findViewById(R.id.sendgift_send_type);
		mSendSMS = (CheckBox) findViewById(R.id.sendgift_send_sms);
		
		mCancel.setVisibility(View.VISIBLE);
		mCancel.setText("取消");
		mTitle.setText("送礼物");
		mHandsel.setVisibility(View.VISIBLE);
		mHandsel.setText("赠送");
	}

	private void setListener() 
	{
		mCancel.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//关闭当前界面
				finish();
			}
		});
		mHandsel.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//判断是否显示礼物,如果选择了则显示发送成功并关闭当前界面否则则显示对话框提示
				if (mIsSelectGift) 
				{
					Toast.makeText(mContext, "礼物发送成功", Toast.LENGTH_SHORT).show();
					finish();
				} 
				else 
				{
					new AlertDialog.Builder(mContext)
					.setTitle("开心网")
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setMessage("请选择礼物")
					.setPositiveButton("确定",
					new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int which) 
						{
							dialog.dismiss();
						}
					}).create().show();
				}
			}
		});
		mMessage.addTextChangedListener(new TextWatcher() 
		{
			//当文本修改时调用
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				//根据内容的行数添加文本线
				int lineCount = mMessage.getLineCount();
				switch (lineCount) 
				{
				case 1:
					mMessageLine2.setVisibility(View.GONE);
					break;

				case 2:
					mMessageLine2.setVisibility(View.VISIBLE);
					break;
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) 
			{
			}

			public void afterTextChanged(Editable s) 
			{
			}
		});
		mFriendsList.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//跳转到选择好友
				startActivityForResult(new Intent(mContext, MenuKaixinSelectFriendsActivity.class), ActivityForResultUtil.REQUESTCODE_SELECTFRIENDS);
			}
		});
		mAddGift.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//跳转到选择礼物
				startActivityForResult(new Intent(mContext, MenuKaixinSelectGiftActivity.class), ActivityForResultUtil.REQUESTCODE_SELECTGIFT);
			}
		});
		mSendType.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				//暂时不做任何操作
			}
		});
		mSendSMS.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				//暂时不做任何操作
			}
		});
	}

	private void init() 
	{
		//初始化显示一条线
		mMessageLine1.setVisibility(View.VISIBLE);
		mMessageLine2.setVisibility(View.GONE);
		//获取当前接收礼物的好友的ID、姓名以及头像
		String uid = getIntent().getStringExtra("uid");
		String name = getIntent().getStringExtra("name");
		int avatar = getIntent().getIntExtra("avatar", -1);
		//添加到送礼物列表中
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid);
		map.put("name", name);
		map.put("avatar", String.valueOf(avatar));
		mApplication.mGiftFriendsList.put(uid, map);
		//更新头像列表
		initFriendsList();
	}
	
	/**
	 * 修改头像框显示内容
	 */
	private void initFriendsList() 
	{
		mFriendsList.removeAllViews();
		for (Map.Entry<String, Map<String, String>> entry : mApplication.mGiftFriendsList.entrySet()) 
		{
			ImageView imageView = getImageView();
			imageView.setImageBitmap(mApplication.getAvatar(Integer.parseInt(entry.getValue().get("avatar"))));
			mFriendsList.addView(imageView);
			mFriendsList.invalidate();
		}
		ImageView imageView = getImageView();
		imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu_kaixin_gifts_add));
		mFriendsList.addView(imageView);
		mFriendsList.invalidate();
	}
	
	/**
	 * 获取头像框控件
	 */
	private ImageView getImageView() 
	{
		ImageView imageView = new ImageView(mContext);
		int widthAndHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthAndHeight, widthAndHeight);
		imageView.setLayoutParams(params);
		imageView.setPadding(4, 4, 4, 4);
		return imageView;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) 
		{
		case ActivityForResultUtil.REQUESTCODE_SELECTGIFT:
			if (resultCode == RESULT_OK) 
			{
				//获取礼物成功后,显示获取的礼物的内容和显示图片
				mIsSelectGift = true;
				GiftResult result = data.getParcelableExtra("result");
				mAddGift.setImageBitmap(mApplication.getGift(result.getGid() + ".jpg"));
				mMessage.setText(new TextUtil(mApplication).replace(result.getContent()));
			}
			break;

		case ActivityForResultUtil.REQUESTCODE_SELECTFRIENDS:
			if (resultCode == RESULT_OK) 
			{
				//更新头像列表
				initFriendsList();
			}
			break;
		}
	}

	protected void onDestroy() 
	{
		super.onDestroy();
		mApplication.mGiftFriendsList.clear();
	}
}
