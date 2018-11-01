/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinPhoneAlbumActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 手机图片文件类
 * @author: zhaoqy
 * @date: 2015-11-6 下午3:59:28
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.util.PhotoUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("DefaultLocale")
public class MenuKaixinPhoneAlbumActivity extends MenuKaixinBaseActivity implements OnClickListener, OnItemClickListener
{
	public static Activity mInstance;
	private Context  mContext;
	private TextView mCancel;
	private TextView mTitle;
	private ListView mDisplay;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_phone_album);
		mContext = this;
		
		initView();
		setListener();
		init();
	}

	private void initView() 
	{
		mCancel = (TextView) findViewById(R.id.id_title_left_text);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mDisplay = (ListView) findViewById(R.id.phonealbum_display);
	}

	private void setListener() 
	{
		mCancel.setOnClickListener(this);
		mDisplay.setOnItemClickListener(this);
	}

	private void init() 
	{
		mCancel.setVisibility(View.VISIBLE);
		mCancel.setText("取消");
		mTitle.setText("手机相册");
		
		mInstance = this;
		mApplication.mPhoneAlbum.clear();
		//获取手机里的图片内容
		new Thread(new Runnable() 
		{
			@SuppressLint("SdCardPath")
			public void run() 
			{
				File sdRootFolder = new File("/sdcard/");
				getFiles(sdRootFolder);
				runOnUiThread(new Runnable() 
				{
					public void run() 
					{
						mDisplay.setAdapter(new PhoneAlnumAdapter());
					}
				});
			}
		}).start();
	}
	
	@Override
	public void onClick(View v) 
	{
		//关闭当前界面
		finish();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		//传递文件夹地址到文件夹内容显示类
		Intent intent = new Intent();
		intent.setClass(mContext, MenuKaixinAlbumActivity.class);
		String path = (String) mApplication.mPhoneAlbum.keySet().toArray()[position];
		intent.putExtra("path", path);
		startActivity(intent);
	}

	private class PhoneAlnumAdapter extends BaseAdapter 
	{

		public int getCount() 
		{
			return mApplication.mPhoneAlbum.size();
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_phone_album, null);
				holder = new ViewHolder();
				holder.photo = (ImageView) convertView.findViewById(R.id.phonealbum_item_photo);
				holder.name = (TextView) convertView.findViewById(R.id.phonealbum_item_name);
				holder.count = (TextView) convertView.findViewById(R.id.phonealbum_item_count);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			List<Map<String, String>> list = mApplication.mPhoneAlbum.get(mApplication.mPhoneAlbum.keySet().toArray()[position]);
			holder.photo.setImageBitmap(PhotoUtil.getImageThumbnail(list.get(0).get("image_path"), 70, 70));
			holder.name.setText(list.get(0).get("image_parent_name"));
			holder.count.setText("(" + list.size() + ")");
			return convertView;
		}

		class ViewHolder 
		{
			ImageView photo;
			TextView  name;
			TextView  count;
		}
	}

	/**
	 * 获取文件目录下的图片内容
	 * @param folder
	 */
	private void getFiles(File folder) 
	{
		File[] files = folder.listFiles();
		if (files != null) 
		{
			for (int i = 0; i < files.length; i++) 
			{
				File file = files[i];
				if (getImageFile(file.getName())) 
				{
					if (mApplication.mPhoneAlbum.containsKey(folder.getAbsolutePath())) 
					{
						List<Map<String, String>> list = mApplication.mPhoneAlbum.get(folder.getAbsolutePath());
						Map<String, String> map = new HashMap<String, String>();
						map.put("image_name", file.getName());
						map.put("image_path", file.getAbsolutePath());
						map.put("image_parent_name", folder.getName());
						map.put("image_parent_path", folder.getAbsolutePath());
						list.add(map);
					} 
					else 
					{
						List<Map<String, String>> list = new ArrayList<Map<String, String>>();
						Map<String, String> map = new HashMap<String, String>();
						map.put("image_name", file.getName());
						map.put("image_path", file.getAbsolutePath());
						map.put("image_parent_name", folder.getName());
						map.put("image_parent_path", folder.getAbsolutePath());
						list.add(map);
						mApplication.mPhoneAlbum.put(folder.getAbsolutePath(), list);
					}
				}
				if (file.isDirectory()) 
				{
					getFiles(file);
				}
			}
		}
	}

	/**
	 * 判断是否为图片
	 * @param fName 文件的名字
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	private boolean getImageFile(String fName) 
	{
		boolean re;
		String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
		if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) 
		{
			re = true;
		}
		else 
		{
			re = false;
		}
		return re;
	}
}
