/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MainLbsActivity.java
 * @Prject: DemoKaixin
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 附近
 * @author: zhaoqy
 * @date: 2015-11-12 上午9:24:01
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.NearbyPeopleResult;
import com.zhaoqy.app.demo.menu.kaixin.item.NearbyPhotoDetailResult;
import com.zhaoqy.app.demo.menu.kaixin.item.NearbyPhotoResult;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;
import com.zhaoqy.app.demo.menu.kaixin.view.CustomListView;
import com.zhaoqy.app.demo.menu.kaixin.view.NearbyPhotoLayout;

public class MainLbsActivity extends MenuKaixinBaseActivity implements OnClickListener
{
	private NearbyPeopleAdapter mNearbyPeopleAdapter;
	private NearbyPhotoAdapter  mNearbyPhotoAdapter;
	private CustomListView      mDisplay;
	private Context    mContext;
	private ImageView  mBack;
	private TextView   mTitle;
	private ScrollView mDisplayLayout;
	private TextView   mNoDisplay;
	private Button     mCheckIn;
	private Button     mNearbyPeople;
	private Button     mNearbyPhoto;
	private int        mChoosePostition = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_desktop_lbs);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mDisplayLayout = (ScrollView) findViewById(R.id.lbs_display_layout);
		mDisplay = (CustomListView) findViewById(R.id.lbs_display);
		mNoDisplay = (TextView) findViewById(R.id.lbs_nodisplay);
		mCheckIn = (Button) findViewById(R.id.lbs_checkin);
		mNearbyPeople = (Button) findViewById(R.id.lbs_nearby_people);
		mNearbyPhoto = (Button) findViewById(R.id.lbs_nearby_photo);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mCheckIn.setOnClickListener(this);
		mNearbyPeople.setOnClickListener(this);
		mNearbyPhoto.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("位置");
		mDisplayLayout.setVisibility(View.GONE);
		mNoDisplay.setVisibility(View.VISIBLE);
		getNearbyPeople();
		getNearbyPhoto();
		mNearbyPeopleAdapter = new NearbyPeopleAdapter();
		mNearbyPhotoAdapter = new NearbyPhotoAdapter();
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
		case R.id.lbs_checkin:
		{
			if (mChoosePostition != 0) 
			{
				mChoosePostition = 0;
				mDisplayLayout.setVisibility(View.GONE);
				mNoDisplay.setVisibility(View.VISIBLE);
				mCheckIn.setBackgroundResource(R.drawable.menu_kaixin_bottom_left_selected);
				mNearbyPeople.setBackgroundResource(R.drawable.menu_kaixin_bottom_mid);
				mNearbyPhoto.setBackgroundResource(R.drawable.menu_kaixin_bottom_right);
			}
			break;
		}
		case R.id.lbs_nearby_people:
		{
			if (mChoosePostition != 1) 
			{
				mChoosePostition = 1;
				mDisplayLayout.setVisibility(View.VISIBLE);
				mNoDisplay.setVisibility(View.GONE);
				mDisplay.setDivider(new ColorDrawable(Color.parseColor("#aaaaaa")));
				mDisplay.setDividerHeight(1);
				mDisplay.setBackgroundColor(Color.parseColor("#00000000"));
				mCheckIn.setBackgroundResource(R.drawable.menu_kaixin_bottom_left);
				mNearbyPeople.setBackgroundResource(R.drawable.menu_kaixin_bottom_mid_selected);
				mNearbyPhoto.setBackgroundResource(R.drawable.menu_kaixin_bottom_right);
				mDisplay.setAdapter(mNearbyPeopleAdapter);
			}
			break;
		}
		case R.id.lbs_nearby_photo:
		{
			if (mChoosePostition != 2) 
			{
				mChoosePostition = 2;
				mDisplayLayout.setVisibility(View.VISIBLE);
				mNoDisplay.setVisibility(View.GONE);
				mDisplay.setDivider(null);
				mDisplay.setDividerHeight(0);
				mDisplay.setBackgroundResource(R.drawable.menu_kaixin_listbox);
				mCheckIn.setBackgroundResource(R.drawable.menu_kaixin_bottom_left);
				mNearbyPeople.setBackgroundResource(R.drawable.menu_kaixin_bottom_mid);
				mNearbyPhoto.setBackgroundResource(R.drawable.menu_kaixin_bottom_right_selected);
				mDisplay.setAdapter(mNearbyPhotoAdapter);
			}
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 获取附近的人的数据
	 */
	private void getNearbyPeople() 
	{
		if (mApplication.mMyNearByPeopleResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = mContext.getAssets().open("data/my_nearbypeople.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				NearbyPeopleResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new NearbyPeopleResult();
					result.setUid(array.getJSONObject(i).getString("uid"));
					result.setName(array.getJSONObject(i).getString("name"));
					result.setAvatar(array.getJSONObject(i).getInt("avatar"));
					result.setTime(array.getJSONObject(i).getString("time"));
					result.setDistance(array.getJSONObject(i).getString("distance"));
					result.setPicture(array.getJSONObject(i).getBoolean("picture"));
					result.setLocation(array.getJSONObject(i).getString("location"));
					mApplication.mMyNearByPeopleResults.add(result);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取附近的照片的数据
	 */
	private void getNearbyPhoto() 
	{
		if (mApplication.mMyNearbyPhotoResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = mContext.getAssets().open("data/my_nearbyphoto.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				NearbyPhotoResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new NearbyPhotoResult();
					result.setPid(array.getJSONObject(i).getString("pid"));
					result.setImage(array.getJSONObject(i).getString("image"));
					result.setTitle(array.getJSONObject(i).getString("title"));
					result.setCount(array.getJSONObject(i).getInt("count"));
					result.setLocation(array.getJSONObject(i).getString("location"));
					result.setDistance(array.getJSONObject(i).getString("distance"));
					JSONArray imagesArray = array.getJSONObject(i).getJSONArray("images");
					List<NearbyPhotoDetailResult> images = new ArrayList<NearbyPhotoDetailResult>();
					for (int j = 0; j < imagesArray.length(); j++) 
					{
						NearbyPhotoDetailResult nearbyPhotoDetailResult = new NearbyPhotoDetailResult();
						nearbyPhotoDetailResult.setImage(imagesArray.getJSONObject(j).getString("image"));
						nearbyPhotoDetailResult.setOwners_uid(imagesArray.getJSONObject(j).getJSONObject("owners").getString("uid"));
						nearbyPhotoDetailResult.setOwners_name(imagesArray.getJSONObject(j).getJSONObject("owners").getString("name"));
						nearbyPhotoDetailResult.setOwners_avatar(imagesArray.getJSONObject(j).getJSONObject("owners").getInt("avatar"));
						nearbyPhotoDetailResult.setTime(imagesArray.getJSONObject(j).getString("time"));
						nearbyPhotoDetailResult.setDescription(imagesArray.getJSONObject(j).getString("description"));
						nearbyPhotoDetailResult.setComment_count(imagesArray.getJSONObject(j).getInt("comment_count"));
						nearbyPhotoDetailResult.setLike_count(imagesArray.getJSONObject(j).getInt("like_count"));
						JSONArray commentsArray = imagesArray.getJSONObject(j).getJSONArray("comments");
						List<Map<String, Object>> comments = new ArrayList<Map<String, Object>>();
						for (int k = 0; k < commentsArray.length(); k++) 
						{
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("uid", commentsArray.getJSONObject(k).getString("uid"));
							map.put("avatar", commentsArray.getJSONObject(k).getString("avatar"));
							map.put("name", commentsArray.getJSONObject(k).getString("name"));
							map.put("time", commentsArray.getJSONObject(k).getString("time"));
							map.put("content", commentsArray.getJSONObject(k).getString("content"));
							if (commentsArray.getJSONObject(k).has("replys")) 
							{
								JSONArray replysArray = commentsArray.getJSONObject(k).getJSONArray("replys");
								List<Map<String, String>> replysResults = new ArrayList<Map<String, String>>();
								for (int l = 0; l < replysArray.length(); l++) 
								{
									Map<String, String> replyMap = new HashMap<String, String>();
									replyMap.put("uid", replysArray.getJSONObject(l).getString("uid"));
									replyMap.put("avatar",replysArray.getJSONObject(l).getString("avatar"));
									replyMap.put("name", replysArray.getJSONObject(l).getString("name"));
									replyMap.put("time", replysArray.getJSONObject(l).getString("time"));
									replyMap.put("content",replysArray.getJSONObject(l).getString("content"));
									replysResults.add(replyMap);
								}
								map.put("replys", replysResults);
							}
							comments.add(map);
						}
						nearbyPhotoDetailResult.setComments(comments);
						images.add(nearbyPhotoDetailResult);
					}
					result.setImages(images);
					mApplication.mMyNearbyPhotoResults.add(result);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	private class NearbyPeopleAdapter extends BaseAdapter 
	{
		public int getCount() 
		{
			return mApplication.mMyNearByPeopleResults.size();
		}

		public Object getItem(int position) 
		{
			return mApplication.mMyNearByPeopleResults.get(position);
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_desktop_lbs_nearby_people, null);
				holder = new ViewHolder();
				holder.avatar = (ImageView) convertView.findViewById(R.id.lbs_nearby_people_item_avatar);
				holder.name = (TextView) convertView.findViewById(R.id.lbs_nearby_people_item_name);
				holder.location = (TextView) convertView.findViewById(R.id.lbs_nearby_people_item_location);
				holder.time = (TextView) convertView.findViewById(R.id.lbs_nearby_people_item_time);
				holder.picture = (ImageView) convertView.findViewById(R.id.lbs_nearby_people_item_picture);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			NearbyPeopleResult result = mApplication.mMyNearByPeopleResults.get(position);
			holder.avatar.setImageBitmap(mApplication.getDefaultAvatar(result.getAvatar()));
			holder.name.setText(result.getName());
			holder.location.setText(result.getLocation());
			holder.time.setText(result.getTime() + "  " + result.getDistance());
			if (result.isPicture()) 
			{
				holder.picture.setVisibility(View.VISIBLE);
			} 
			else 
			{
				holder.picture.setVisibility(View.GONE);
			}
			if (position == 0) 
			{
				convertView.setBackgroundResource(R.drawable.menu_kaixin_lbs_photo_head_bg);
			} 
			else if (position == getCount() - 1) 
			{
				convertView.setBackgroundResource(R.drawable.menu_kaixin_lbs_photo_bottom_bg);
			} 
			else 
			{
				convertView.setBackgroundResource(R.drawable.menu_kaixin_lbs_photo_middle_bg);
			}
			return convertView;
		}

		class ViewHolder 
		{
			ImageView avatar;
			TextView  name;
			TextView  location;
			TextView  time;
			ImageView picture;
		}

	}

	private class NearbyPhotoAdapter extends BaseAdapter 
	{
		public int getCount() 
		{
			return mApplication.mMyNearbyPhotoResults.size();
		}

		public Object getItem(int position) 
		{
			return mApplication.mMyNearbyPhotoResults.get(position);
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_desktop_lbs_nearby_photo, null);
				holder = new ViewHolder();
				holder.location = (TextView) convertView.findViewById(R.id.lbs_nearby_photo_item_location);
				holder.distance = (TextView) convertView.findViewById(R.id.lbs_nearby_photo_item_distance);
				holder.display = (LinearLayout) convertView.findViewById(R.id.lbs_nearby_photo_item_display);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			NearbyPhotoResult result = mApplication.mMyNearbyPhotoResults.get(position);
			holder.location.setText(result.getLocation());
			holder.distance.setText(result.getDistance());
			if (result.getCount() > 0) 
			{
				holder.display.setVisibility(View.VISIBLE);
				holder.display.removeAllViews();
				for (int i = 0; i < result.getCount(); i++) 
				{
					NearbyPhotoDetailResult nearbyPhotoDetailResult = result.getImages().get(i);
					NearbyPhotoLayout layout = new NearbyPhotoLayout(mContext, mApplication);
					holder.display.addView(layout.getLayout());
					holder.display.invalidate();
					layout.setPhoto(nearbyPhotoDetailResult.getImage());
					layout.getLayout().setTag(nearbyPhotoDetailResult);
					layout.getLayout().setOnClickListener(new OnClickListener() 
					{
						public void onClick(View v) 
						{
							NearbyPhotoDetailResult result = (NearbyPhotoDetailResult) v.getTag();
						}
					});
				}
			} 
			else 
			{
				holder.display.setVisibility(View.GONE);
			}
			return convertView;
		}

		class ViewHolder 
		{
			TextView     location;
			TextView     distance;
			LinearLayout display;
		}
	}
}
