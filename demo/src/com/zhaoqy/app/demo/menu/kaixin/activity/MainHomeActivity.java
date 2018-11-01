/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MainHomeActivity.java
 * @Prject: DemoKaixin
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 首页
 * @author: zhaoqy
 * @date: 2015-11-11 下午8:41:44
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.InputStream;

import org.json.JSONArray;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.HomeResult;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;

public class MainHomeActivity extends MenuKaixinBaseActivity implements OnClickListener, OnItemClickListener
{
	private Context     mContext;
	private ImageView   mBack;
	private TextView    mTitle;
	private ListView    mDisplay;
	private String[]    mPopupWindowItems = { "好友动态", "热门动态" };
	private PopupWindow mPopupWindow;
	private View        mPopView;
	private ListView    mPopDisplay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_desktop_home);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mDisplay = (ListView) findViewById(R.id.home_display);
		mPopView = LayoutInflater.from(mContext).inflate(R.layout.view_menu_kaixin_desktop_home_popupwindow, null);
		mPopDisplay = (ListView) mPopView.findViewById(R.id.home_popupwindow_display);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mPopDisplay.setOnItemClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("好友动态");
		getHome();
		mDisplay.setAdapter(new HomeAdapter());
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
			//显示菜单
			initPopupWindow();
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		switch (view.getId()) 
		{
		case R.id.home_popupwindow_display:
		{
			//这里只更改了标题栏,先不做数据的切换,因为数据有限
			mPopupWindow.dismiss();
			mTitle.setText(mPopupWindowItems[position]);
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 获取首页数据
	 */
	private void getHome() 
	{
		if (mApplication.mMyHomeResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = mContext.getAssets().open("data/my_home_friends.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				HomeResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new HomeResult();
					result.setUid(array.getJSONObject(i).getString("uid"));
					result.setName(array.getJSONObject(i).getString("name"));
					result.setAvatar(array.getJSONObject(i).getInt("avatar"));
					result.setType(array.getJSONObject(i).getString("type"));
					result.setTime(array.getJSONObject(i).getString("time"));
					result.setTitle(array.getJSONObject(i).getString("title"));
					if (array.getJSONObject(i).has("from")) 
					{
						result.setFrom(array.getJSONObject(i).getString("from"));
					}
					if (array.getJSONObject(i).has("comment_count")) 
					{
						result.setComment_count(array.getJSONObject(i).getInt("comment_count"));
					}
					if (array.getJSONObject(i).has("like_count")) 
					{
						result.setLike_count(array.getJSONObject(i).getInt("like_count"));
					}
					if (array.getJSONObject(i).has("photo")) 
					{
						result.setPhoto(array.getJSONObject(i).getString("photo"));
					}
					mApplication.mMyHomeResults.add(result);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 初始化菜单
	 */
	@SuppressWarnings("deprecation")
	private void initPopupWindow() 
	{
		PopupWindowAdapter adapter = new PopupWindowAdapter();
		mPopDisplay.setAdapter(adapter);
		if (mPopupWindow == null) 
		{
			mPopupWindow = new PopupWindow(mPopView, mTitle.getWidth(), LayoutParams.WRAP_CONTENT, true);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		}
		if (mPopupWindow.isShowing()) 
		{
			mPopupWindow.dismiss();
		}
		else 
		{
			mPopupWindow.showAsDropDown(mTitle, 0, -10);
		}
	}

	private class PopupWindowAdapter extends BaseAdapter 
	{

		public int getCount() 
		{
			return mPopupWindowItems.length;
		}

		public Object getItem(int position) 
		{
			return mPopupWindowItems[position];
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_desktop_home_popupwindow, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.home_popupwindow_item_name);
				convertView.setTag(holder);
			}
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(mPopupWindowItems[position]);
			return convertView;
		}

		class ViewHolder 
		{
			TextView name;
		}
	}

	private class HomeAdapter extends BaseAdapter 
	{

		public int getCount() 
		{
			return mApplication.mMyHomeResults.size();
		}

		public Object getItem(int position) 
		{
			return mApplication.mMyHomeResults.get(position);
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_desktop_home, null);
				holder = new ViewHolder();
				holder.viewed = (View) convertView.findViewById(R.id.home_item_viewed);
				holder.viewed_avatar = (ImageView) holder.viewed.findViewById(R.id.home_viewed_item_avatar);
				holder.viewed_name = (TextView) holder.viewed.findViewById(R.id.home_viewed_item_name);
				holder.viewed_time = (TextView) holder.viewed.findViewById(R.id.home_viewed_item_time);
				holder.viewed_title = (TextView) holder.viewed.findViewById(R.id.home_viewed_item_title);
				holder.viewed_all = (TextView) holder.viewed.findViewById(R.id.home_viewed_item_all);
				holder.photo = (View) convertView.findViewById(R.id.home_item_photo);
				holder.photo_avatar = (ImageView) holder.photo.findViewById(R.id.home_photo_item_avatar);
				holder.photo_name = (TextView) holder.photo.findViewById(R.id.home_photo_item_name);
				holder.photo_time = (TextView) holder.photo.findViewById(R.id.home_photo_item_time);
				holder.photo_title = (TextView) holder.photo.findViewById(R.id.home_photo_item_titie);
				holder.photo_photo = (ImageView) holder.photo.findViewById(R.id.home_photo_item_photo);
				holder.photo_from = (TextView) holder.photo.findViewById(R.id.home_photo_item_from);
				holder.photo_comment_count = (TextView) holder.photo.findViewById(R.id.home_photo_item_comment_count);
				holder.photo_like_count = (TextView) holder.photo.findViewById(R.id.home_photo_item_like_count);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			HomeResult result = mApplication.mMyHomeResults.get(position);
			if ("viewed".equals(result.getType())) 
			{
				holder.viewed.setVisibility(View.VISIBLE);
				holder.photo.setVisibility(View.GONE);
				holder.viewed_avatar.setImageBitmap(mApplication.getAvatar(result.getAvatar()));
				holder.viewed_name.setText(result.getName());
				holder.viewed_time.setText(result.getTime());
				holder.viewed_title.setText(result.getTitle());
				holder.viewed_all.setText("查看" + result.getName() + "的全部转帖");
			} 
			else 
			{
				holder.viewed.setVisibility(View.GONE);
				holder.photo.setVisibility(View.VISIBLE);
				holder.photo_avatar.setImageBitmap(mApplication.getAvatar(result.getAvatar()));
				holder.photo_name.setText(result.getName());
				holder.photo_time.setText(result.getTime());
				holder.photo_title.setText(result.getTitle());
				holder.photo_photo.setImageBitmap(mApplication.getHome(result.getPhoto()));
				holder.photo_from.setText(result.getFrom());
				holder.photo_comment_count.setText(result.getComment_count() + "");
				holder.photo_like_count.setText(result.getLike_count() + "");
			}
			return convertView;
		}

		class ViewHolder 
		{
			View viewed;
			ImageView viewed_avatar;
			TextView viewed_name;
			TextView viewed_time;
			TextView viewed_title;
			TextView viewed_all;

			View photo;
			ImageView photo_avatar;
			TextView photo_name;
			TextView photo_time;
			TextView photo_title;
			ImageView photo_photo;
			TextView photo_from;
			TextView photo_comment_count;
			TextView photo_like_count;
		}
	}
}
