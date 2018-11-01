/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MainViewedActivity.java
 * @Prject: DemoKaixin
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 转帖
 * @author: zhaoqy
 * @date: 2015-11-11 下午9:46:48
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.ListView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.ViewedResult;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;

public class MainViewedActivity extends MenuKaixinBaseActivity implements OnClickListener
{
	private ViewedAdapter mAdapter;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private ListView  mDisplay;
	private Button    mFriends;
	private Button    mHot;
	private boolean   mIsFriends = true; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_desktop_viewed);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mDisplay = (ListView) findViewById(R.id.viewed_display);
		mFriends = (Button) findViewById(R.id.viewed_friends);
		mHot = (Button) findViewById(R.id.viewed_hot);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mFriends.setOnClickListener(this);
		mHot.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("转帖");
		getViewed();
		getViewedHot();
		mAdapter = new ViewedAdapter(mApplication.mMyViewedResults);
		mDisplay.setAdapter(mAdapter);
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
		case R.id.viewed_friends:
		{
			if (!mIsFriends) 
			{
				mIsFriends = true;
				mFriends.setBackgroundResource(R.drawable.menu_kaixin_bottom_left_selected);
				mHot.setBackgroundResource(R.drawable.menu_kaixin_bottom_right);
				mAdapter = new ViewedAdapter(mApplication.mMyViewedResults);
				mDisplay.setAdapter(mAdapter);
			}
			break;
		}
		case R.id.viewed_hot:
		{
			if (mIsFriends) 
			{
				mIsFriends = false;
				mFriends.setBackgroundResource(R.drawable.menu_kaixin_bottom_left);
				mHot.setBackgroundResource(R.drawable.menu_kaixin_bottom_right_selected);
				mAdapter = new ViewedAdapter(mApplication.mViewedHotResults);
				mDisplay.setAdapter(mAdapter);
			}
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 获取好友转帖数据
	 */
	private void getViewed() 
	{
		if (mApplication.mMyViewedResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = mContext.getAssets().open("data/my_viewed.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				ViewedResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new ViewedResult();
					result.setTitle(array.getJSONObject(i).getString("title"));
					result.setTime(array.getJSONObject(i).getString("time"));
					result.setOwners_name(array.getJSONObject(i).getJSONObject("owners").getString("name"));
					result.setOwners_uid(array.getJSONObject(i).getJSONObject("owners").getString("uid"));
					result.setOwners_avatar(array.getJSONObject(i).getJSONObject("owners").getInt("avatar"));
					result.setImage(array.getJSONObject(i).getInt("image"));
					result.setType(array.getJSONObject(i).getString("type"));
					result.setContent(array.getJSONObject(i).getString("content"));
					result.setForwarding_count(array.getJSONObject(i).getString("forwarding_count"));
					mApplication.mMyViewedResults.add(result);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取热门转帖数据
	 */
	private void getViewedHot() 
	{
		if (mApplication.mViewedHotResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = mContext.getAssets().open("data/viewed_hot.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				ViewedResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new ViewedResult();
					result.setTitle(array.getJSONObject(i).getString("title"));
					result.setTime(array.getJSONObject(i).getString("time"));
					result.setOwners_name(array.getJSONObject(i).getJSONObject("owners").getString("name"));
					result.setOwners_uid(array.getJSONObject(i).getJSONObject("owners").getString("uid"));
					result.setOwners_avatar(array.getJSONObject(i).getJSONObject("owners").getInt("avatar"));
					result.setImage(array.getJSONObject(i).getInt("image"));
					result.setType(array.getJSONObject(i).getString("type"));
					result.setContent(array.getJSONObject(i).getString("content"));
					result.setForwarding_count(array.getJSONObject(i).getString("forwarding_count"));
					mApplication.mViewedHotResults.add(result);
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	private class ViewedAdapter extends BaseAdapter 
	{
		private List<ViewedResult> mResults;

		public ViewedAdapter(List<ViewedResult> results) 
		{
			if (results != null) 
			{
				mResults = results;
			} 
			else 
			{
				mResults = new ArrayList<ViewedResult>();
			}
		}

		public int getCount() 
		{
			return mResults.size();
		}

		public Object getItem(int position) 
		{
			return mResults.get(position);
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_desktop_viewed, null);
				holder = new ViewHolder();
				holder.type = (ImageView) convertView.findViewById(R.id.viewed_item_type);
				holder.title = (TextView) convertView.findViewById(R.id.viewed_item_title);
				holder.image = (ImageView) convertView.findViewById(R.id.viewed_item_image);
				holder.time = (TextView) convertView.findViewById(R.id.viewed_item_time);
				holder.owners = (TextView) convertView.findViewById(R.id.viewed_item_owners);
				holder.content = (TextView) convertView.findViewById(R.id.viewed_item_content);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			ViewedResult result = mResults.get(position);
			if (result.getType().equals("url")) 
			{
				holder.type.setImageResource(R.drawable.menu_kaixin_viewed_default);
			}
			else if (result.getType().equals("video")) 
			{
				holder.type.setImageResource(R.drawable.menu_kaixin_other_video);
			}
			holder.title.setText(result.getTitle());
			holder.time.setText(result.getTime());
			holder.content.setText(result.getContent());
			if (mIsFriends) 
			{
				holder.image.setImageBitmap(mApplication.getViewed(result.getImage()));
				holder.owners.setText(result.getOwners_name() + "转帖");
			} 
			else 
			{
				holder.image.setImageBitmap(mApplication.getViewedHot(result.getImage()));
				holder.owners.setText(result.getForwarding_count() + "次转帖");
			}
			return convertView;
		}

		class ViewHolder 
		{
			ImageView type;
			TextView  title;
			ImageView image;
			TextView  time;
			TextView  owners;
			TextView  content;
		}
	}
}
