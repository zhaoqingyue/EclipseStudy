package com.zhaoqy.app.demo.weixin.ui.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.register.ToastUtil;
import com.zhaoqy.app.demo.weixin.adapter.FriendCircleAdpter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FriendCircleActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private ImageView mTalk;
	private ListView  mListview;
	private View      mHeader;
	private TextView  mEmpty;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weixin_friend_circle);
		mContext = this;
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mTalk = (ImageView) findViewById(R.id.id_title_right_img);
		mHeader = getLayoutInflater().inflate(R.layout.view_friend_circle_header, null);
		mListview = (ListView) findViewById(R.id.id_friend_circle_listview);
		mEmpty = (TextView) findViewById(R.id.id_friend_circle_nochat);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mTalk.setOnClickListener(this);
		mTalk.setOnLongClickListener(new OnLongClickListener() 
		{
			@Override
			public boolean onLongClick(View v) 
			{
				ToastUtil.showLongToast(mContext, "长按发送文字");
				return true;
			}
		});
	}

	private void initData() 
	{
		mTitle.setText(R.string.friend_circle);
		mBack.setVisibility(View.VISIBLE);
		mTalk.setVisibility(View.VISIBLE);
		mTalk.setImageResource(R.drawable.weixin_friend_circle_talk);
		mListview.addHeaderView(mHeader);
		mListview.setEmptyView(mEmpty);
		mListview.setAdapter(new FriendCircleAdpter(mContext));
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
		case R.id.id_title_right_img:
		{
			Intent intent = new Intent(mContext, ShareActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
