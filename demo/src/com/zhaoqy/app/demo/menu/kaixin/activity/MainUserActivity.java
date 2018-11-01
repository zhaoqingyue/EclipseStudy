/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MainUserActivity.java
 * @Prject: DemoKaixin
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 用户首页
 * @author: zhaoqy
 * @date: 2015-11-11 下午5:54:47
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.FriendInfoResult;
import com.zhaoqy.app.demo.menu.kaixin.item.StatusResult;
import com.zhaoqy.app.demo.menu.kaixin.item.VisitorsResult;
import com.zhaoqy.app.demo.menu.kaixin.util.ActivityForResultUtil;
import com.zhaoqy.app.demo.menu.kaixin.util.PhotoUtil;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;
import com.zhaoqy.app.demo.menu.kaixin.util.Utils;

public class MainUserActivity extends MenuKaixinBaseActivity implements OnClickListener
{
	private UserInfoAdapter mAdapter;
	private LinearLayout    mHead_Sig_Layout;
	private LinearLayout    mHead_Friends_List;
	private Context    mContext;
	private ImageView  mBack;
	private TextView   mTitle;
	private View       mUserHead;
	private ImageView  mHead_Wallpager;
	private ImageView  mHead_Avatar;
	private TextView   mHead_Name;
	private ImageView  mHead_Gender;
	private TextView   mHead_Constellation;
	private TextView   mHead_Sig;
	private TextView   mHead_About;
	private TextView   mHead_Photo;
	private TextView   mHead_Diary;
	private TextView   mHead_Friends;
	private Button     mHead_Friends_List_Count;
	private ListView   mDisplay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_menu_kaixin_user_info);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mUserHead = LayoutInflater.from(mContext).inflate(R.layout.view_menu_kaixin_user_info_head, null);
		mHead_Wallpager = (ImageView) mUserHead.findViewById(R.id.user_info_head_wallpager);
		mHead_Avatar = (ImageView) mUserHead.findViewById(R.id.user_info_head_avatar);
		mHead_Name = (TextView) mUserHead.findViewById(R.id.user_info_head_name);
		mHead_Gender = (ImageView) mUserHead.findViewById(R.id.user_info_head_gender);
		mHead_Constellation = (TextView) mUserHead.findViewById(R.id.user_info_head_constellation);
		mHead_Sig_Layout = (LinearLayout) mUserHead.findViewById(R.id.user_info_head_sig_layout);
		mHead_Sig = (TextView) mUserHead.findViewById(R.id.user_info_head_sig);
		mHead_About = (TextView) mUserHead.findViewById(R.id.user_info_head_about);
		mHead_Photo = (TextView) mUserHead.findViewById(R.id.user_info_head_photo);
		mHead_Diary = (TextView) mUserHead.findViewById(R.id.user_info_head_diary);
		mHead_Friends = (TextView) mUserHead.findViewById(R.id.user_info_head_friends);
		mHead_Friends_List = (LinearLayout) mUserHead.findViewById(R.id.user_info_head_friends_list);
		mHead_Friends_List_Count = (Button) mUserHead.findViewById(R.id.user_info_head_friends_list_count);
		mDisplay = (ListView) findViewById(R.id.user_info_display);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mHead_Wallpager.setOnClickListener(this);
		mHead_Avatar.setOnClickListener(this);
		mHead_Sig_Layout.setOnClickListener(this);
		mHead_Friends_List_Count.setOnClickListener(this);
		mHead_About.setOnClickListener(this);
		mHead_Photo.setOnClickListener(this);
		mHead_Diary.setOnClickListener(this);
		mHead_Friends.setOnClickListener(this);
	}
	
	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("我的主页");
		getInfo();
		getVisitors();
		getStatus();
		mDisplay.addHeaderView(mUserHead);
		mAdapter = new UserInfoAdapter(mContext, mApplication.mMyStatusResults);
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
		case R.id.user_info_head_wallpager:
		{
			//跳转到修改墙纸界面
			Intent intent = new Intent(mContext, MenuKaixinChangeWallpagerActivity.class);
			startActivityForResult(intent, ActivityForResultUtil.REQUESTCODE_CHANGEWALLPAGER);
			break;
		}
		case R.id.user_info_head_avatar:
		{
			changeAvatar();
			break;
		}
		case R.id.user_info_head_sig_layout:
		{
			//跳转到修改签名界面
			Intent intent = new Intent(mContext, MenuKaixinEditSignatureActivity.class);
			startActivityForResult(intent, ActivityForResultUtil.REQUESTCODE_EDITSIGNATURE);
			break;
		}
		case R.id.user_info_head_friends_list_count:
		{
			//跳转到最近来访列表界面
			Intent intent = new Intent(mContext, MenuKaixinVisitorsActivity.class);
			mContext.startActivity(intent);
			break;
		}
		case R.id.user_info_head_about:
		{
			//跳转到关于界面
			Intent intent = new Intent();
			intent.setClass(mContext, MenuKaixinAboutActivity.class);
			intent.putExtra("result", mApplication.mMyInfoResult);
			mContext.startActivity(intent);
			break;
		}
		case R.id.user_info_head_photo:
		{
			//跳转到照片界面
			Intent intent = new Intent(mContext, MenuKaixinPhotoActivity.class);
			mContext.startActivity(intent);
			break;
		}
		case R.id.user_info_head_diary:
		{
			//跳转到日记界面
			Intent intent = new Intent(mContext, MenuKaixinDiaryActivity.class);
			mContext.startActivity(intent);
			break;
		}
		case R.id.user_info_head_friends:
		{
			//跳转到好友界面
			Intent intent = new Intent();
			intent.setClass(mContext, MenuKaixinFriendsActivity.class);
			intent.putExtra("name", mApplication.mMyInfoResult.getName());
			mContext.startActivity(intent);
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 获取用户资料
	 */
	private void getInfo() 
	{
		mApplication.mMyInfoResult = new FriendInfoResult();
		InputStream inputStream;
		try 
		{
			inputStream = mContext.getAssets().open("data/my_info.KX");
			String json = new TextUtil(mApplication).readTextFile(inputStream);
			JSONObject object = new JSONObject(json);
			mApplication.mMyInfoResult.setName(object.getString("name"));
			mApplication.mMyInfoResult.setAvatar(object.getInt("avatar"));
			mApplication.mMyInfoResult.setGender(object.getInt("gender"));
			mApplication.mMyInfoResult.setConstellation(object.getString("constellation"));
			mApplication.mMyInfoResult.setSignature(object.getString("signature"));
			mApplication.mMyInfoResult.setPhoto_count(object.getInt("photo_count"));
			mApplication.mMyInfoResult.setDiary_count(object.getInt("diary_count"));
			mApplication.mMyInfoResult.setFriend_count(object.getInt("friend_count"));
			mApplication.mMyInfoResult.setVisitor_count(object.getInt("visitor_count"));
			mApplication.mMyInfoResult.setWallpager(object.getInt("wallpager"));
			mApplication.mMyInfoResult.setDate(object.getString("date"));

			//界面的数据赋值
			mHead_Name.setText(mApplication.mMyInfoResult.getName());
			mHead_Avatar.setImageBitmap(mApplication.getAvatar(mApplication.mMyInfoResult.getAvatar()));
			mHead_Gender.setImageBitmap(Utils.getGender(mContext.getResources(),mApplication.mMyInfoResult.getGender()));
			mHead_Constellation.setText(mApplication.mMyInfoResult.getConstellation());
			mHead_Sig.setText(new TextUtil(mApplication).replace(mApplication.mMyInfoResult.getSignature()));
			mHead_About.setText("关于");
			mHead_Photo.setText("照片 " + mApplication.mMyInfoResult.getPhoto_count());
			mHead_Diary.setText("日记 "+ mApplication.mMyInfoResult.getDiary_count());
			mHead_Friends.setText("好友 "+ mApplication.mMyInfoResult.getFriend_count());
			mHead_Friends_List_Count.setText(mApplication.mMyInfoResult.getVisitor_count() + "");

			/**
			 * 原有的墙纸取消,采用随机的墙纸,这样保证每次进入都不一样,其他用户采用自己设定的墙纸显示,如注释掉的代码
			 * mHead_Wallpager.setImageDrawable(mKXApplication.getTitleWallpager(mApplication.mMyInfoResult.getWallpager()));
			 */
			mHead_Wallpager.setImageBitmap(mApplication.getTitleWallpager(mApplication.mWallpagerPosition));
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}

	/**
	 * 获取用户最近来访
	 */
	private void getVisitors() 
	{
		InputStream inputStream;
		try 
		{
			inputStream = mContext.getAssets().open("data/my_visitors.KX");
			String json = new TextUtil(mApplication).readTextFile(inputStream);
			JSONArray array = new JSONArray(json);
			VisitorsResult result = null;
			for (int i = 0; i < array.length(); i++) 
			{
				result = new VisitorsResult();
				result.setUid(array.getJSONObject(i).getString("uid"));
				result.setName(array.getJSONObject(i).getString("name"));
				result.setAvatar(array.getJSONObject(i).getInt("avatar"));
				result.setTime(array.getJSONObject(i).getString("time"));
				mApplication.mMyVisitorsResults.add(result);

				//显示最近头像
				ImageView imageView = new ImageView(mContext);
				int widthAndHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, mContext.getResources().getDisplayMetrics());
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthAndHeight, widthAndHeight);
				imageView.setLayoutParams(params);
				imageView.setPadding(4, 4, 4, 4);
				imageView.setImageBitmap(mApplication.getAvatar(result.getAvatar()));
				imageView.setTag(result);
				mHead_Friends_List.addView(imageView);
				mHead_Friends_List.invalidate();
				imageView.setOnClickListener(new OnClickListener() 
				{
					public void onClick(View v) 
					{
						VisitorsResult result = (VisitorsResult) v.getTag();
						Intent intent = new Intent();
						intent.setClass(mContext, MenuKaixinFriendInfoActivity.class);
						intent.putExtra("uid", result.getUid());
						intent.putExtra("name", result.getName());
						intent.putExtra("avatar", result.getAvatar());
						mContext.startActivity(intent);
					}
				});
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}

	/**
	 * 获取用户的状态
	 */
	private void getStatus() 
	{
		InputStream inputStream;
		try 
		{
			inputStream = mContext.getAssets().open("data/my_status.KX");
			String json = new TextUtil(mApplication).readTextFile(inputStream);
			JSONArray array = new JSONArray(json);
			StatusResult result = null;
			for (int i = 0; i < array.length(); i++) 
			{
				result = new StatusResult();
				result.setName(array.getJSONObject(i).getString("name"));
				result.setTime(array.getJSONObject(i).getString("time"));
				result.setContent(array.getJSONObject(i).getString("content"));
				result.setFrom(array.getJSONObject(i).getString("from"));
				result.setComment_count(array.getJSONObject(i).getInt("comment_count"));
				result.setForward_count(array.getJSONObject(i).getInt("forward_count"));
				result.setLike_count(array.getJSONObject(i).getInt("like_count"));
				mApplication.mMyStatusResults.add(result);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}

	public void setWallpager() 
	{
		mHead_Wallpager.setImageBitmap(mApplication.getTitleWallpager(mApplication.mWallpagerPosition));
	}

	/**
	 * 修改签名
	 * @param arg0 修改的签名文本
	 */
	public void setSignature(String arg0) 
	{
		mHead_Sig.setText(new TextUtil(mApplication).replace(arg0));
	}

	/**
	 * 修改头像
	 * @param bitmap 修改的头像
	 */
	public void setAvatar(Bitmap bitmap) 
	{
		mHead_Avatar.setImageBitmap(bitmap);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) 
		{
		case ActivityForResultUtil.REQUESTCODE_CHANGEWALLPAGER:
		{
			if (resultCode == RESULT_OK) 
			{
				//切换墙纸时回调此方法,通知菜单界面和用户界面修改墙纸
				setWallpager();
			}
			break;
		}
		case ActivityForResultUtil.REQUESTCODE_EDITSIGNATURE:
		{
			if (resultCode == RESULT_OK) 
			{
				//修改签名时回调此方法,通知菜单界面和用户界面修改签名
				String arg0 = data.getStringExtra("signature");
				setSignature(arg0);
			}
			break;
		}
		case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CAMERA:
		{
			if (resultCode == RESULT_OK) 
			{
				
				if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) 
				{
					Toast.makeText(this, "SD不可用", Toast.LENGTH_SHORT).show();
					return;
				}
				//通过照相修改头像
				File file = new File(mApplication.mUploadPhotoPath);
				startPhotoZoom(Uri.fromFile(file));
			} 
			else 
			{
				Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION:
		{
			Uri uri = null;
			if (data == null) 
			{
				Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
				return;
			}
			if (resultCode == RESULT_OK) 
			{
				if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) 
				{
					Toast.makeText(this, "SD不可用", Toast.LENGTH_SHORT).show();
					return;
				}
				//通过本地修改头像
				uri = data.getData();
				startPhotoZoom(uri);
			}
			else 
			{
				Toast.makeText(this, "照片获取失败", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CROP:
		{
			if (data == null) 
			{
				Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
				return;
			} 
			else 
			{
				//裁剪修改的头像
				saveCropPhoto(data);
			}
			break;
		}
		default:
			break;
		}
	}
	
	/**
	 * 系统裁剪照片
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) 
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("scale", true);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CROP);
	}
	
	/**
	 * 保存裁剪的照片
	 * 
	 * @param data
	 */
	private void saveCropPhoto(Intent data) 
	{
		Bundle extras = data.getExtras();
		if (extras != null) 
		{
			Bitmap bitmap = extras.getParcelable("data");
			bitmap = PhotoUtil.toRoundCorner(bitmap, 15);
			if (bitmap != null) 
			{
				uploadPhoto(bitmap);
			}
		} 
		else 
		{
			Toast.makeText(this, "获取裁剪照片错误", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 更新头像
	 */
	private void uploadPhoto(Bitmap bitmap) 
	{
		setAvatar(bitmap);
	}

	/**
	 * 我的首页适配器
	 */
	private class UserInfoAdapter extends BaseAdapter 
	{
		private Context mContext;
		private List<StatusResult> mResults;

		public UserInfoAdapter(Context context, List<StatusResult> results) 
		{
			mContext = context;
			mResults = results;
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
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_user_info, null);
				holder = new ViewHolder();
				holder.avatar = (ImageView) convertView.findViewById(R.id.user_info_item_avatar);
				holder.name = (TextView) convertView.findViewById(R.id.user_info_item_name);
				holder.time = (TextView) convertView.findViewById(R.id.user_info_item_time);
				holder.content = (TextView) convertView.findViewById(R.id.user_info_item_content);
				holder.from = (TextView) convertView.findViewById(R.id.user_info_item_from);
				holder.comment_count = (TextView) convertView.findViewById(R.id.user_info_item_comment_count);
				holder.forward_count = (TextView) convertView.findViewById(R.id.user_info_item_forward_count);
				holder.like_count = (TextView) convertView.findViewById(R.id.user_info_item_like_count);
				convertView.setTag(holder);
			} 
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			StatusResult result = mResults.get(position);
			Bitmap avatar = PhotoUtil.toRoundCorner(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.menu_kaixin_head_default), 15);
			holder.avatar.setImageBitmap(avatar);
			holder.name.setText(result.getName());
			holder.time.setText(result.getTime());
			holder.content.setText(result.getContent());
			holder.from.setText(result.getFrom());
			holder.comment_count.setText(result.getComment_count() + "");
			holder.forward_count.setText(result.getForward_count() + "");
			holder.like_count.setText(result.getLike_count() + "");
			return convertView;
		}

		class ViewHolder 
		{
			ImageView avatar;
			TextView name;
			TextView time;
			TextView content;
			TextView from;
			TextView comment_count;
			TextView forward_count;
			TextView like_count;
		}
	}
	
	private void changeAvatar() 
	{
		new AlertDialog.Builder(mContext).setTitle("请选择").setItems(new String[] {"修改头像", "编辑我的资料", "设置为通讯录头像" }, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				switch (which) 
				{
					case 0:
					{
						new AlertDialog.Builder(mContext).setTitle("修改头像").setItems(new String[] {"拍照上传", "上传手机中的照片" }, new DialogInterface.OnClickListener() 
						{
							@SuppressLint("SdCardPath")
							public void onClick(DialogInterface dialog, int which) 
							{
								Intent intent = null;
								switch (which) 
								{
									case 0:
										intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
										File dir = new File("/sdcard/demo/Camera/");
										if (!dir.exists()) 
										{
											dir.mkdirs();
										}
										mApplication.mUploadPhotoPath = "/sdcard/demo/Camera/" + UUID.randomUUID().toString();
										File file = new File(mApplication.mUploadPhotoPath);
										if (!file.exists()) 
										{
											try 
											{
												file.createNewFile();
											} 
											catch (IOException e) 
											{
											}
										}
										intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
										startActivityForResult(intent, ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CAMERA);
										break;						
									case 1:
										intent = new Intent(Intent.ACTION_PICK,null);
										intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
										startActivityForResult(intent,ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION);
										break;
									}
								}
							})
						.setNegativeButton("取消", new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int which) 
							{
								dialog.cancel();
							}
						})
						.create().show();
						break;
					}
					case 1:
					{
						//跳转到关于界面
						Intent intent = new Intent();
						intent.setClass(mContext, MenuKaixinAboutActivity.class);
						intent.putExtra("result", mApplication.mMyInfoResult);
						mContext.startActivity(intent);
						break;
					}		
					case 2:
					{
						mContext.startActivity(new Intent(mContext, MenuKaixinContactsActivity.class));
						break;
					}	
				}
			}
		})
		.setNegativeButton("取消",new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		}).create().show();
	}
}
