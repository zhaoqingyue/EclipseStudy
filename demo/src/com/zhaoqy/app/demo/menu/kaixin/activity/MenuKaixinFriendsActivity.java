/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinFriendsActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 资料好友
 * @author: zhaoqy
 * @date: 2015-11-9 下午8:47:21
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.FriendsResult;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;

public class MenuKaixinFriendsActivity extends MenuKaixinBaseActivity 
{
	private List<FriendsResult> mFriendsResults;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private GridView  mDisplay;
	private TextView  mNoDisplay;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_friends);
		mContext = this;
		
		findViewById();
		setListener();
		init();
	}

	private void findViewById() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mDisplay = (GridView) findViewById(R.id.friends_display);
		mNoDisplay = (TextView) findViewById(R.id.friends_nodisplay);
	}

	private void setListener() 
	{
		mBack.setVisibility(View.VISIBLE);
		mBack.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				//关闭当前界面
				finish();
			}
		});
		mDisplay.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				//获取当前点击的好友数据并跳转到好友资料界面
				FriendsResult result = mFriendsResults.get(position);
				Intent intent = new Intent();
				intent.setClass(mContext, MenuKaixinFriendInfoActivity.class);
				intent.putExtra("uid", result.getUid());
				intent.putExtra("name", result.getName());
				intent.putExtra("avatar", result.getAvatar());
				startActivity(intent);
			}
		});
	}

	private void init() 
	{
		//获取当前好友所属用户的ID和姓名
		String uid = getIntent().getStringExtra("uid");
		String name = getIntent().getStringExtra("name");
		mTitle.setText(name + "的好友");
		//ID为空时为当前用户
		if (uid == null) 
		{
			//获取当前用户的好友数据
			getMyFriends();
		} 
		else 
		{
			//获取其他用户的好友数据
			getFriendFriends(uid);
		}
		//添加适配器
		mDisplay.setAdapter(new FriendsAdapter());
	}

	/**
	 * 获取当前用户的好友数据
	 */
	private void getMyFriends() 
	{
		if (mApplication.mMyFriendsResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = getAssets().open("data/my_friends.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				FriendsResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new FriendsResult();
					result.setUid(array.getJSONObject(i).getString("uid"));
					result.setName(array.getJSONObject(i).getString("name"));
					result.setAvatar(array.getJSONObject(i).getInt("avatar"));
					result.setName_pinyin(new TextUtil(mApplication).getStringPinYin(result.getName()));
					if (!TextUtils.isEmpty(result.getName_pinyin())) 
					{
						result.setName_first(result.getName_pinyin().substring(0, 1).toUpperCase());
					}
					mApplication.mMyFriendsResults.add(result);

					if (result.getName_first().matches("^[a-z,A-Z].*$")) 
					{
						if (mApplication.mMyFriendsFirstName.contains(result.getName_first())) {
							mApplication.mMyFriendsGroupByFirstName.get(result.getName_first()).add(result);
						} 
						else 
						{
							mApplication.mMyFriendsFirstName.add(result.getName_first());
							List<FriendsResult> list = new ArrayList<FriendsResult>();
							list.add(result);
							mApplication.mMyFriendsGroupByFirstName.put(result.getName_first(), list);
						}
					} 
					else 
					{
						if (mApplication.mMyFriendsFirstName.contains("#")) 
						{
							mApplication.mMyFriendsGroupByFirstName.get("#").add(result);
						} 
						else 
						{
							mApplication.mMyFriendsFirstName.add("#");
							List<FriendsResult> list = new ArrayList<FriendsResult>();
							list.add(result);
							mApplication.mMyFriendsGroupByFirstName.put("#", list);
						}
					}
				}
				Collections.sort(mApplication.mMyFriendsFirstName);
				int position = 0;
				for (int i = 0; i < mApplication.mMyFriendsFirstName.size(); i++) 
				{
					mApplication.mMyFriendsFirstNamePosition.put(mApplication.mMyFriendsFirstName.get(i),position);
					mApplication.mMyFriendsPosition.add(position);
					position += mApplication.mMyFriendsGroupByFirstName.get(mApplication.mMyFriendsFirstName.get(i)).size();
				}
				mFriendsResults = mApplication.mMyFriendsResults;
			}
			catch (Exception e) 
			{
				mFriendsResults = new ArrayList<FriendsResult>();
				mDisplay.setVisibility(View.GONE);
				mNoDisplay.setVisibility(View.VISIBLE);
			}
		} 
		else 
		{
			mFriendsResults = mApplication.mMyFriendsResults;
		}
	}

	/**
	 * 获取其他用户的好友数据
	 * @param uid 用户的ID
	 */
	@SuppressLint("DefaultLocale")
	private void getFriendFriends(String uid) 
	{
		if (mApplication.mFriendFriendsResults.containsKey(uid)) 
		{
			mFriendsResults = mApplication.mFriendFriendsResults.get(uid);
		}
		else 
		{
			InputStream inputStream;
			try 
			{
				inputStream = getAssets().open("data/" + uid + "_friends.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				FriendsResult result = null;
				mFriendsResults = new ArrayList<FriendsResult>();
				for (int i = 0; i < array.length(); i++) {
					result = new FriendsResult();
					result.setUid(array.getJSONObject(i).getString("uid"));
					result.setName(array.getJSONObject(i).getString("name"));
					result.setAvatar(array.getJSONObject(i).getInt("avatar"));
					result.setName_pinyin(new TextUtil(mApplication).getStringPinYin(result.getName()));
					if (!TextUtils.isEmpty(result.getName_pinyin())) 
					{
						result.setName_first(result.getName_pinyin().substring(0, 1).toUpperCase());
					}
					mFriendsResults.add(result);
				}
				mApplication.mFriendFriendsResults.put(uid, mFriendsResults);
			}
			catch (Exception e) 
			{
				mFriendsResults = new ArrayList<FriendsResult>();
				mDisplay.setVisibility(View.GONE);
				mNoDisplay.setVisibility(View.VISIBLE);
			}
		}
	}

	private class FriendsAdapter extends BaseAdapter 
	{
		public int getCount() 
		{
			return mFriendsResults.size();
		}

		public Object getItem(int position) 
		{
			return mFriendsResults.get(position);
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_friends, null);
				holder = new ViewHolder();
				holder.avatar = (ImageView) convertView.findViewById(R.id.friends_item_avatar);
				//padding为40dip,换算px值
				int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
				//设置显示头像的大小为屏幕宽度的1/4
				LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.width = (mScreenWidth - padding) / 4;
				params.height = (mScreenWidth - padding) / 4;
				holder.avatar.setLayoutParams(params);
				holder.name = (TextView) convertView.findViewById(R.id.friends_item_name);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			FriendsResult result = mFriendsResults.get(position);
			holder.avatar.setImageBitmap(mApplication.getAvatar(result.getAvatar()));
			holder.name.setText(result.getName());
			return convertView;
		}

		class ViewHolder 
		{
			ImageView avatar;
			TextView name;
		}
	}
}
