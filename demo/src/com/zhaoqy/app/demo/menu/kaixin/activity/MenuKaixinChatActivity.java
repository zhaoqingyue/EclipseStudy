/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinChatActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 聊天
 * @author: zhaoqy
 * @date: 2015-11-10 下午2:35:27
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.ChatResult;
import com.zhaoqy.app.demo.menu.kaixin.util.PhotoUtil;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;
import com.zhaoqy.app.demo.menu.kaixin.util.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MenuKaixinChatActivity extends MenuKaixinBaseActivity 
{
	private LinearLayout mParent;
	private Context      mContext;
	private ImageView    mBack;
	private TextView     mTitle;
	private TextView     mAction;
	private ListView     mDisplay;
	private Button       mFace;
	private EditText     mContent;
	private Button       mSend;
	private ChatAdapter  mAdapter;
	private String       mName;   //当前聊天用户名称
	private int          mAvatar; //当前聊天用户的头像

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_chat);
		mContext = this;
		
		findViewById();
		setListener();
		init();
	}

	private void findViewById() 
	{
		mParent = (LinearLayout) findViewById(R.id.chat_parent);
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mAction = (TextView) findViewById(R.id.id_title_right_text);
		mDisplay = (ListView) findViewById(R.id.chat_display);
		mFace = (Button) findViewById(R.id.chat_face);
		mContent = (EditText) findViewById(R.id.chat_content);
		mSend = (Button) findViewById(R.id.chat_send);
		
		mBack.setVisibility(View.VISIBLE);
		mAction.setVisibility(View.VISIBLE);
		mAction.setText("分享");
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
		mAction.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//查看聊天记录的对话框
				new AlertDialog.Builder(mContext)
				.setTitle("开心网")
				.setItems(new String[] { "聊天记录" },
				new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						//跳转到聊天记录界面,并传递当前聊天用户的姓名和头像
						Intent intent = new Intent();
						intent.setClass(mContext, MenuKaixinChatLogsActivity.class);
						intent.putExtra("name", mName);
						intent.putExtra("avatar", mAvatar); 
						startActivity(intent);
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
		});
		mFace.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//显示表情对话框
				showFace(mParent);
			}
		});
		mSend.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//获取当前输入的聊天内容
				String content = mContent.getText().toString().trim();
				//聊天内容不为空时执行
				if (!TextUtils.isEmpty(content)) 
				{
					//添加聊天信息
					ChatResult result = new ChatResult();
					result.setTime(Utils.getTime(mContext));
					result.setType(1);
					result.setContent(content);
					mApplication.mChatResults.add(result);
					//更新界面并滚动到最后一条信息,并清空输入框
					mAdapter.notifyDataSetChanged();
					mDisplay.setSelection(mApplication.mChatResults.size());
					mContent.setText("");
				}
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
		mFaceGridView.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				//获取当前光标所在位置
				int currentPosition = mContent.getSelectionStart();
				//添加有表情的文字
				mContent.setText(new TextUtil(mApplication).replace(mContent.getText().insert(currentPosition, mApplication.mFacesText.get(position))));
				// 关闭表情对话框
				dismissFace();
			}
		});
	}

	private void init() 
	{
		//获取当前聊天的用户的姓名和头像,并显示姓名到标题栏
		mName = getIntent().getStringExtra("name");
		mAvatar = getIntent().getIntExtra("avatar", -1);
		mTitle.setText(mName);
		//获取聊天记录
		getChat();
		//添加适配器并滚动到最后一条信息
		mAdapter = new ChatAdapter();
		mDisplay.setAdapter(mAdapter);
		mDisplay.setSelection(mApplication.mChatResults.size());
	}

	/**
	 * 获取聊天记录
	 */
	private void getChat() 
	{
		//判断存储的聊天记录是否已经存在,存在则不在获取
		if (mApplication.mChatResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = getAssets().open("data/chat.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				ChatResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new ChatResult();
					result.setTime(array.getJSONObject(i).getString("time"));
					result.setContent(array.getJSONObject(i).getString("content"));
					result.setType(array.getJSONObject(i).getInt("type"));
					mApplication.mChatResults.add(result);
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
		}
	}

	public class ChatAdapter extends BaseAdapter 
	{
		public int getCount() 
		{
			return mApplication.mChatResults.size();
		}

		public Object getItem(int position) 
		{
			return mApplication.mChatResults.get(position);
		}

		public long getItemId(int position) 
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ViewHolder holder = null;
			if (convertView == null) 
			{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_chat, null);
				holder = new ViewHolder();
				holder.in = (RelativeLayout) convertView.findViewById(R.id.chat_item_in_layout);
				holder.out = (RelativeLayout) convertView.findViewById(R.id.chat_item_out_layout);
				holder.inAvatar = (ImageView) convertView.findViewById(R.id.chat_item_in_avatar);
				holder.outAvatar = (ImageView) convertView.findViewById(R.id.chat_item_out_avatar);
				holder.inName = (TextView) convertView.findViewById(R.id.chat_item_in_name);
				holder.outName = (TextView) convertView.findViewById(R.id.chat_item_out_name);
				holder.inTime = (TextView) convertView.findViewById(R.id.chat_item_in_time);
				holder.outTime = (TextView) convertView.findViewById(R.id.chat_item_out_time);
				holder.inContent = (TextView) convertView.findViewById(R.id.chat_item_in_content);
				holder.outContent = (TextView) convertView.findViewById(R.id.chat_item_out_content);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			ChatResult result = mApplication.mChatResults.get(position);
			//根据消息的类型,显示不同的界面效果,1为用户自己发出的消息,2为用户收到的消息
			switch (result.getType()) 
			{
			case 1:
				holder.in.setVisibility(View.GONE);
				holder.out.setVisibility(View.VISIBLE);
				holder.outAvatar.setImageBitmap(PhotoUtil.toRoundCorner(BitmapFactory.decodeResource(getResources(), R.drawable.menu_kaixin_head_default), 15));
				holder.outName.setText("我");
				holder.outTime.setText(result.getTime());
				holder.outContent.setText(new TextUtil(mApplication).replace(result.getContent()));
				break;
			case 2:
				holder.out.setVisibility(View.GONE);
				holder.in.setVisibility(View.VISIBLE);
				holder.inAvatar.setImageBitmap(mApplication.getAvatar(mAvatar));
				holder.inName.setText(mName);
				holder.inTime.setText(result.getTime());
				holder.inContent.setText(new TextUtil(mApplication).replace(result.getContent()));
				break;
			}
			return convertView;
		}

		class ViewHolder 
		{
			RelativeLayout in;
			RelativeLayout out;
			ImageView inAvatar;
			ImageView outAvatar;
			TextView  inName;
			TextView  outName;
			TextView  inTime;
			TextView  outTime;
			TextView  inContent;
			TextView  outContent;
		}
	}
}
