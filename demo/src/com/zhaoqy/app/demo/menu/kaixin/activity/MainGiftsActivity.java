/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MainGiftsActivity.java
 * @Prject: DemoKaixin
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 礼物
 * @author: zhaoqy
 * @date: 2015-11-12 上午9:05:43
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.InputStream;

import org.json.JSONArray;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.FriendsBirthdayResult;
import com.zhaoqy.app.demo.menu.kaixin.item.GiftResult;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;
import com.zhaoqy.app.demo.menu.kaixin.view.CustomGridView;
import com.zhaoqy.app.demo.menu.kaixin.view.CustomListView;

public class MainGiftsActivity extends MenuKaixinBaseActivity implements OnClickListener
{
	private CustomGridView mDisplay;
	private CustomListView mFriendsList;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private ImageView mMore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_desktop_gifts);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mMore = (ImageView) findViewById(R.id.gifts_more);
		mDisplay = (CustomGridView) findViewById(R.id.gifts_display);
		mFriendsList = (CustomListView) findViewById(R.id.gifts_friends_list);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mMore.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("礼物");
		
		getGifts();
		getFriendsBirthday();
		mDisplay.setAdapter(new GiftAdapter());
		mFriendsList.setAdapter(new BirthdayAdapter());
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
			break;
		}
		case R.id.gifts_more:
		{
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 获取礼物数据
	 */
	private void getGifts() 
	{
		if (mApplication.mGiftResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = mContext.getAssets().open("data/gifts.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				GiftResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new GiftResult();
					result.setGid(array.getJSONObject(i).getString("gid"));
					result.setName(array.getJSONObject(i).getString("name"));
					result.setContent(array.getJSONObject(i).getString("content"));
					mApplication.mGiftResults.add(result);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取好友生日数据
	 */
	private void getFriendsBirthday() 
	{
		if (mApplication.mMyFriendsBirthdayResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = mContext.getAssets().open("data/my_friendsbirthday.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				FriendsBirthdayResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new FriendsBirthdayResult();
					result.setUid(array.getJSONObject(i).getString("uid"));
					result.setName(array.getJSONObject(i).getString("name"));
					result.setAvatar(array.getJSONObject(i).getInt("avatar"));
					result.setBirthday_date(array.getJSONObject(i).getString("birthday_date"));
					mApplication.mMyFriendsBirthdayResults.add(result);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	private class GiftAdapter extends BaseAdapter 
	{
		public int getCount() 
		{
			return 8;
		}

		public Object getItem(int position) 
		{
			return null;
		}

		public long getItemId(int position) 
		{
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ViewHolder holder = null;
			if (convertView == null) 
			{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_desktop_gifts, null);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.gifts_item_image);
				holder.title = (TextView) convertView.findViewById(R.id.gifts_item_title);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			GiftResult result = mApplication.mGiftResults.get(position);
			holder.image.setImageBitmap(mApplication.getGift(result.getGid() + ".jpg"));
			holder.title.setText(result.getName());
			return convertView;
		}

		class ViewHolder 
		{
			ImageView image;
			TextView  title;
		}
	}

	private class BirthdayAdapter extends BaseAdapter 
	{
		public int getCount() 
		{
			return mApplication.mMyFriendsBirthdayResults.size();
		}

		public Object getItem(int position) 
		{
			return null;
		}

		public long getItemId(int position) 
		{
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ViewHolder holder = null;
			if (convertView == null) 
			{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_desktop_gifts_birthday, null);
				holder = new ViewHolder();
				holder.avatar = (ImageView) convertView.findViewById(R.id.gifts_birthday_item_avatar);
				holder.name = (TextView) convertView.findViewById(R.id.gifts_birthday_item_name);
				holder.date = (TextView) convertView.findViewById(R.id.gifts_birthday_item_date);
				holder.send = (Button) convertView.findViewById(R.id.gifts_birthday_item_send);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			FriendsBirthdayResult result = mApplication.mMyFriendsBirthdayResults.get(position);
			holder.avatar.setImageBitmap(mApplication.getAvatar(result.getAvatar()));
			holder.name.setText(result.getName());
			holder.date.setText(result.getBirthday_date());
			holder.send.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
				}
			});
			return convertView;
		}

		class ViewHolder 
		{
			ImageView avatar;
			TextView  name;
			TextView  date;
			Button    send;
		}
	}
}
