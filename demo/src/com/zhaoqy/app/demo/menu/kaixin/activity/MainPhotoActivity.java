/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MainPhotoActivity.java
 * @Prject: DemoKaixin
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 照片
 * @author: zhaoqy
 * @date: 2015-11-11 下午9:35:15
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.PhotoDetailResult;
import com.zhaoqy.app.demo.menu.kaixin.item.PhotoResult;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;

public class MainPhotoActivity extends MenuKaixinBaseActivity implements OnClickListener
{
	private PhotoAdapter mAdapter;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private GridView  mDisplay;
	private Button    mFriend;
	private Button    mMySelf;
	private boolean   mIsFriend = true; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_desktop_photo);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mDisplay = (GridView) findViewById(R.id.photo_display);
		mFriend = (Button) findViewById(R.id.photo_friend);
		mMySelf = (Button) findViewById(R.id.photo_myself);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mFriend.setOnClickListener(this);
		mMySelf.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("照片");
		getFriendPhoto();
		getMyPhoto();
		mAdapter = new PhotoAdapter(mApplication.mFriendPhotoResults.get("kx001"));
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
		case R.id.id_title_right_img:
		{
			break;
		}
		case R.id.photo_friend:
		{
			if (!mIsFriend) 
			{
				mIsFriend = true;
				mFriend.setBackgroundResource(R.drawable.menu_kaixin_bottom_left_selected);
				mMySelf.setBackgroundResource(R.drawable.menu_kaixin_bottom_right);
				mAdapter = new PhotoAdapter(mApplication.mFriendPhotoResults.get("kx001"));
				mDisplay.setAdapter(mAdapter);
			}
			break;
		}
		case R.id.photo_myself:
		{
			if (mIsFriend) 
			{
				mIsFriend = false;
				mFriend.setBackgroundResource(R.drawable.menu_kaixin_bottom_left);
				mMySelf.setBackgroundResource(R.drawable.menu_kaixin_bottom_right_selected);
				mAdapter = new PhotoAdapter(mApplication.mMyPhotoResults);
				mDisplay.setAdapter(mAdapter);
			}
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 获取好友照片数据
	 */
	private void getFriendPhoto() 
	{
		if (!mApplication.mFriendPhotoResults.containsKey("kx001")) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = mContext.getAssets().open("data/kx001_photo.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				getPhotos(json, true);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取我的照片数据
	 */
	private void getMyPhoto() 
	{
		if (mApplication.mMyPhotoResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = mContext.getAssets().open("data/my_photo.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				getPhotos(json, false);
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 解析Json数据
	 * @param json
	 * @param isFriend
	 */
	private void getPhotos(String json, boolean isFriend) 
	{
		try 
		{
			JSONArray array = new JSONArray(json);
			PhotoResult result = null;
			List<PhotoResult> list = new ArrayList<PhotoResult>();
			for (int i = 0; i < array.length(); i++) 
			{
				result = new PhotoResult();
				result.setPid(array.getJSONObject(i).getString("pid"));
				result.setImage(array.getJSONObject(i).getInt("image"));
				result.setTitle(array.getJSONObject(i).getString("title"));
				result.setCount(array.getJSONObject(i).getInt("count"));
				result.setTime(array.getJSONObject(i).getString("time"));
				result.setType(array.getJSONObject(i).getInt("type"));
				JSONArray imagesArray = array.getJSONObject(i).getJSONArray("images");
				List<PhotoDetailResult> images = new ArrayList<PhotoDetailResult>();
				for (int j = 0; j < imagesArray.length(); j++) 
				{
					PhotoDetailResult photoDetailResult = new PhotoDetailResult();
					photoDetailResult.setImage(imagesArray.getJSONObject(j).getInt("image"));
					photoDetailResult.setTime(imagesArray.getJSONObject(j).getString("time"));
					photoDetailResult.setDescription(imagesArray.getJSONObject(j).getString("description"));
					if (imagesArray.getJSONObject(j).has("comment_count")) 
					{
						photoDetailResult.setComment_count(imagesArray.getJSONObject(j).getInt("comment_count"));
					}
					if (imagesArray.getJSONObject(j).has("like_count")) 
					{
						photoDetailResult.setLike_count(imagesArray.getJSONObject(j).getInt("like_count"));
					}
					List<Map<String, Object>> comments = new ArrayList<Map<String, Object>>();
					if (imagesArray.getJSONObject(j).has("comments")) 
					{
						JSONArray commentsArray = imagesArray.getJSONObject(j).getJSONArray("comments");
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
								List<Map<String, String>> replyResults = new ArrayList<Map<String, String>>();
								for (int l = 0; l < replysArray.length(); l++) 
								{
									Map<String, String> replyMap = new HashMap<String, String>();
									replyMap.put("uid", replysArray.getJSONObject(l).getString("uid"));
									replyMap.put("avatar", replysArray.getJSONObject(l).getString("avatar"));
									replyMap.put("name", replysArray.getJSONObject(l).getString("name"));
									replyMap.put("time", replysArray.getJSONObject(l).getString("time"));
									replyMap.put("content", replysArray.getJSONObject(l).getString("content"));
									replyResults.add(replyMap);
								}
								map.put("replys", replyResults);
							}
							comments.add(map);
						}
						photoDetailResult.setComments(comments);
						images.add(photoDetailResult);
					} 
					else 
					{
						photoDetailResult.setComments(comments);
						images.add(photoDetailResult);
					}
				}
				result.setImages(images);
				list.add(result);
			}
			if (isFriend) 
			{
				mApplication.mFriendPhotoResults.put("kx001", list);
			}
			else 
			{
				mApplication.mMyPhotoResults = list;
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}

	private class PhotoAdapter extends BaseAdapter 
	{

		private List<PhotoResult> mResults = new ArrayList<PhotoResult>();

		public PhotoAdapter(List<PhotoResult> results) 
		{
			if (results != null) 
			{
				mResults = results;
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_desktop_photo, null);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.photo_item_img);
				int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, mContext.getResources().getDisplayMetrics());
				LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.width = (mScreenWidth - padding) / 3;
				params.height = (mScreenWidth - padding) / 3;
				holder.image.setLayoutParams(params);
				holder.title = (TextView) convertView.findViewById(R.id.photo_item_title);
				holder.description = (TextView) convertView.findViewById(R.id.photo_item_description);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			PhotoResult result = mResults.get(position);
			if (result.getType() == 0) 
			{
				holder.image.setImageBitmap(mApplication.getAvatar(result.getImage()));
			} 
			else 
			{
				holder.image.setImageBitmap(mApplication.getPhoto(result.getImage()));
			}

			holder.title.setText(result.getTitle() + "(" + result.getCount() + ")");
			if (mIsFriend) 
			{
				holder.description.setText("赵庆月");
			} 
			else 
			{
				holder.description.setText(result.getTime() + " 更新");
			}
			return convertView;
		}

		class ViewHolder 
		{
			ImageView image;
			TextView title;
			TextView description;
		}
	}
}
