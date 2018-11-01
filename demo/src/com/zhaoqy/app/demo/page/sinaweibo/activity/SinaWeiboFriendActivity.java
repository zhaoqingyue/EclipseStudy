package com.zhaoqy.app.demo.page.sinaweibo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.sinaweibo.item.User;
import com.zhaoqy.app.demo.page.sinaweibo.item.WeiBoData;

public class SinaWeiboFriendActivity extends Activity implements OnClickListener
{
	private ArrayAdapter<User> mAdapter;
	private Context        mContext;
	private ListView       mListView;
	private RelativeLayout mFindMap;
	private RelativeLayout mUserInfo;
	private ImageView      mUserIcon;
	private TextView       mUserName;
	private TextView       mStuCount;
	private TextView       mFavCount;
	private TextView       mFlowerCount;
	private LayoutInflater mInflater;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_sinaweibo_friend);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mListView = (ListView) findViewById(R.id.id_sinaweibo_friend_listview);
		mInflater = LayoutInflater.from(this);
		View head = mInflater.inflate(R.layout.view_page_slide_sinaweibo_friend_header, null);
		mListView.addHeaderView(head);
		mFindMap = (RelativeLayout) head.findViewById(R.id.id_sinaweibo_friend_header_map);
		mUserInfo = (RelativeLayout) head.findViewById(R.id.id_sinaweibo_friend_header_userinfo);
		mUserIcon = (ImageView) head.findViewById(R.id.id_sinaweibo_friend_header_user_icon);
		mUserName = (TextView)head. findViewById(R.id.id_sinaweibo_friend_header_user_name);
		mStuCount = (TextView)head. findViewById(R.id.id_sinaweibo_friend_header_user_status);
		mFavCount = (TextView) head.findViewById(R.id.id_sinaweibo_friend_header_user_fav);
		mFlowerCount = (TextView) head.findViewById(R.id.id_sinaweibo_friend_header_user_like);
	}

	private void setListener() 
	{
		mUserInfo.setOnClickListener(this);
		mFindMap.setOnClickListener(this);
		mUserIcon.setOnClickListener(this);
	}

	private void initData() 
	{
		showInfo(WeiBoData.getUser());
		mAdapter = new ArrayAdapter<User>(mContext, R.layout.view_page_slide_sinaweibo_flower_item, R.id.id_sinaweibo_friend_flower_name, WeiBoData.getUserList()) 
		{
			@Override
			public int getCount() 
			{
				if(WeiBoData.getUserList().size() == 0) return 1;
				return WeiBoData.getUserList().size();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) 
			{
				if(getCount() == 1)
				{
					return mInflater.inflate(R.layout.view_page_slide_sinaweibo_friend_loading, null);
				}
				View view = mInflater.inflate(R.layout.view_page_slide_sinaweibo_flower_item, null);
				User user = getItem(position);
				((TextView) view.findViewById(R.id.id_sinaweibo_friend_flower_name)).setText(user.getScreen_name());
				return view;
			}
		};
		mListView.setAdapter(mAdapter);
	}

	public void showInfo(User user) 
	{
		mUserName.setText(user.getScreen_name());
		mStuCount.setText(user.getStatuses_count() + "       微博");
		mFavCount.setText(user.getFavourites_count() + "          收藏");
		mFlowerCount.setText(user.getFollowers_count() + "        粉丝");
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_sinaweibo_friend_header_userinfo:
		{
			startActivity(new Intent(mContext, SinaWeiboUserInfoActivity.class));		
			break;
		}
		case R.id.id_sinaweibo_friend_header_map:
		{
			startActivity(new Intent(mContext, SinaWeiboFriendMapActivity.class));
			break;
		}
		case R.id.id_sinaweibo_friend_header_user_icon:
		{
			startActivity(new Intent(mContext, SinaWeiboUserInfoActivity.class));	
			break;
		}
		default:
			break;
		}
	}
}
