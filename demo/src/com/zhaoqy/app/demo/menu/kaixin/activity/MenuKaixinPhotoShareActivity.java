/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinPhotoShareActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 照片分享类
 * @author: zhaoqy
 * @date: 2015-11-6 下午4:45:31
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.InputStream;
import java.util.Map;
import org.json.JSONArray;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.LocationResult;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("deprecation")
public class MenuKaixinPhotoShareActivity extends MenuKaixinBaseActivity implements OnClickListener, OnItemClickListener
{
	private GalleryAdapter mAdapter;
	private Context   mContext;
	private TextView  mCancel;
	private TextView  mTitle;
	private TextView  mUpload;
	private Gallery   mDisplay;
	private ImageView mDisplaySingle;
	private TextView  mLocation;
	private Button    mDelete;
	private TextView  mAlbum;
	private int       mCurrentPosition;  //当前图片的编号
	private String    mCurrentPath;      //当前图片的地址
	private int       mLocationPosition; //当前选择的地理位置在列表的位置
	private String[]  mAlbums = new String[] { "手机相册" }; //相册
	private int       mAlbumPosition;    //当前选择的相册在列表的位置

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_photo_share);
		mContext = this;
		
		initView();
		setListener();
		init();
	}

	private void initView() 
	{
		mCancel = (TextView) findViewById(R.id.id_title_left_text);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mUpload = (TextView) findViewById(R.id.id_title_right_text);
		mDisplay = (Gallery) findViewById(R.id.photoshare_display);
		mDisplaySingle = (ImageView) findViewById(R.id.photoshare_display_single);
		mLocation = (TextView) findViewById(R.id.photoshare_location);
		mDelete = (Button) findViewById(R.id.photoshare_location_delete);
		mAlbum = (TextView) findViewById(R.id.photoshare_album);
	}

	private void setListener() 
	{
		mCancel.setOnClickListener(this);
		mUpload.setOnClickListener(this);
		mDisplay.setOnItemClickListener(this);
		mDisplaySingle.setOnClickListener(this);
		mLocation.setOnClickListener(this);
		mDelete.setOnClickListener(this);
		mAlbum.setOnClickListener(this);
	}

	private void init() 
	{
		mCancel.setVisibility(View.VISIBLE);
		mCancel.setText("取消");
		mTitle.setText("分享照片");
		mUpload.setVisibility(View.VISIBLE);
		mUpload.setText("上传");
		
		//判断照片的数量,根据数量选择控件显示,1张图片用ImageView显示,多张用Gallery显示
		if (mApplication.mAlbumList.size() > 1) 
		{
			mDisplaySingle.setVisibility(View.GONE);
			mDisplay.setVisibility(View.VISIBLE);
			mCurrentPosition = 0;
			mAdapter = new GalleryAdapter();
			mDisplay.setAdapter(mAdapter);
			mDisplay.setSelection(mCurrentPosition);
		} 
		else if (mApplication.mAlbumList.size() == 1) 
		{
			mDisplaySingle.setVisibility(View.VISIBLE);
			mDisplay.setVisibility(View.GONE);
			mCurrentPosition = 0;
			mCurrentPath = mApplication.mAlbumList.get(mCurrentPosition).get("image_path");
			mDisplaySingle.setImageBitmap(mApplication.getPhoneAlbum(mCurrentPath));
		}
		//获取地理位置数据
		getLocation();
		//显示默认地理位置、相册
		mLocation.setText(mApplication.mMyLocationResults.get(mLocationPosition).getName());
		mAlbum.setText(mAlbums[mAlbumPosition]);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_text:
		{
			finish();
			break;
		}
		case R.id.id_title_right_text:
		{
			//判断手机相册界面是否关闭,如果没关闭则关闭
			if (MenuKaixinPhoneAlbumActivity.mInstance != null && !MenuKaixinPhoneAlbumActivity.mInstance.isFinishing()) 
			{
				MenuKaixinPhoneAlbumActivity.mInstance.finish();
			}
			//显示提示信息并关闭当前界面
			Toast.makeText(mContext, "上传图片成功", Toast.LENGTH_SHORT).show();
			finish();
			break;
		}
		case R.id.photoshare_display_single:
		{
			//将照片地址传递到照片编辑类
			Intent intent = new Intent();
			intent.setClass(mContext, MenuKaixinImageFilterActivity.class);
			intent.putExtra("path", mCurrentPath);
			intent.putExtra("isSetResult", true);
			startActivityForResult(intent, 0);
			break;
		}
		case R.id.photoshare_location:
		{
			//显示地理位置对话框
			locationDialog();
			break;
		}
		case R.id.photoshare_location_delete:
		{
			//更换显示,设置地理位置编号
			mLocation.setText("选择当前位置");
			mLocationPosition = -1;
			break;
		}
		case R.id.photoshare_album:
		{
			//相册对话框
			AlbumDialog();
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		//获取当前的照片编号以及照片地址传递到照片编辑类
		mCurrentPosition = position;
		mCurrentPath = mApplication.mAlbumList.get(mCurrentPosition).get("image_path");
		Intent intent = new Intent();
		intent.setClass(mContext, MenuKaixinImageFilterActivity.class);
		intent.putExtra("path", mCurrentPath);
		intent.putExtra("isSetResult", true);
		startActivityForResult(intent, 0);
	}

	/**
	 * 获取地理位置数据
	 */
	private void getLocation() 
	{
		if (mApplication.mMyLocationResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = getAssets().open("data/my_location.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				LocationResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new LocationResult();
					result.setName(array.getJSONObject(i).getString("name"));
					result.setLocation(array.getJSONObject(i).getString("location"));
					mApplication.mMyLocationResults.add(result);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 地理位置对话框
	 */
	private void locationDialog() 
	{
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("选择当前位置");
		builder.setAdapter(new LocationAdapter(), new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				mLocationPosition = which;
				mLocation.setText(mApplication.mMyLocationResults.get(which).getName());
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("刷新", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		}).create().show();
	}

	/**
	 * 相册对话框
	 */
	private void AlbumDialog() 
	{
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("请选择相册");
		builder.setAdapter(new AlbumAdapter(), new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				mAlbumPosition = which;
				mAlbum.setText(mAlbums[which]);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		}).create().show();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) 
		{
			//获取新的照片地址
			mCurrentPath = data.getStringExtra("path");
			Map<String, String> map = mApplication.mAlbumList.get(mCurrentPosition);
			map.put("image_path", mCurrentPath);
			//更新界面显示
			if (mApplication.mAlbumList.size() > 1) 
			{
				mAdapter.notifyDataSetChanged();
			} 
			else if (mApplication.mAlbumList.size() == 1) 
			{
				mDisplaySingle.setImageBitmap(mApplication.getPhoneAlbum(mCurrentPath));
			}
		}
	}

	private class LocationAdapter extends BaseAdapter 
	{
		public int getCount() 
		{
			return mApplication.mMyLocationResults.size();
		}

		public Object getItem(int position) 
		{
			return mApplication.mMyLocationResults.get(position);
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_photoshare_location,null);
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView.findViewById(R.id.photoshare_activity_location_item_icon);
				holder.name = (TextView) convertView.findViewById(R.id.photoshare_activity_location_item_name);
				holder.location = (TextView) convertView.findViewById(R.id.photoshare_activity_location_item_location);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			LocationResult result = mApplication.mMyLocationResults.get(position);
			if (mLocationPosition == position) 
			{
				holder.icon.setVisibility(View.VISIBLE);
			} 
			else 
			{
				holder.icon.setVisibility(View.INVISIBLE);
			}
			holder.name.setText(result.getName());
			holder.location.setText(result.getLocation());
			return convertView;
		}

		class ViewHolder 
		{
			ImageView icon;
			TextView  name;
			TextView  location;
		}
	}

	private class AlbumAdapter extends BaseAdapter 
	{
		public int getCount() 
		{
			return mAlbums.length;
		}

		public Object getItem(int position) 
		{
			return mAlbums[position];
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_photoshare_album, null);
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView.findViewById(R.id.photoshare_activity_album_item_icon);
				holder.name = (TextView) convertView.findViewById(R.id.photoshare_activity_album_item_name);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.icon.setVisibility(View.VISIBLE);
			holder.name.setText(mAlbums[position]);
			return convertView;
		}

		class ViewHolder 
		{
			ImageView icon;
			TextView  name;
		}
	}

	private class GalleryAdapter extends BaseAdapter 
	{
		public int getCount() 
		{
			return mApplication.mAlbumList.size();
		}

		public Object getItem(int position) 
		{
			return mApplication.mAlbumList.get(position);
		}

		public long getItemId(int position) 
		{
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) 
		{
			ViewHolder holder = null;
			if (convertView == null) 
			{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_photoshare, null);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.photoshare_item_image);
				holder.delete = (Button) convertView.findViewById(R.id.photoshare_item_delete);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			Map<String, String> results = mApplication.mAlbumList.get(position);
			holder.image.setImageBitmap(mApplication.getPhoneAlbum(results.get("image_path")));
			if (mApplication.mAlbumList.size() > 1) 
			{
				holder.delete.setVisibility(View.VISIBLE);
			} 
			else if (mApplication.mAlbumList.size() == 1) 
			{
				holder.delete.setVisibility(View.GONE);
			}
			holder.delete.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					mApplication.mAlbumList.remove(position);
					notifyDataSetChanged();
				}
			});
			return convertView;
		}

		class ViewHolder 
		{
			ImageView image;
			Button    delete;
		}
	}

	protected void onDestroy() 
	{
		super.onDestroy();
		mApplication.mAlbumList.clear();
	}
}
