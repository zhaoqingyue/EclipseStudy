/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaUserInfoActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 用户资料
 * @author: zhaoqy
 * @date: 2015-11-19 上午10:11:49
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.dialog.LoadingDialog;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.FriendList;
import com.zhaoqy.app.demo.oschina.item.MyInformation;
import com.zhaoqy.app.demo.oschina.item.Result;
import com.zhaoqy.app.demo.oschina.util.FileUtils;
import com.zhaoqy.app.demo.oschina.util.ImageUtils;
import com.zhaoqy.app.demo.oschina.util.StringUtils;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OSChinaUserInfoActivity extends OSChinaBaseActivity implements OnClickListener
{
	private final static String FILE_SAVEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/OSChina/Portrait/";
	private final static int CROP = 200;
	private Context       mContext;
	private ImageView     mBack;
	private ImageView     mRefresh;
	private ImageView     mFace;
	private ImageView     mGender;
	private Button        mEditer;
	private TextView      mName;
	private TextView      mJointime;
	private TextView      mFrom;
	private TextView      mDevplatform;
	private TextView      mExpertise;
	private TextView      mFollowers;
	private TextView      mFans;
	private TextView      mFavorites;
	private LinearLayout  mFavoritesView;
	private LinearLayout  mFollowersView;
	private LinearLayout  mFansView;
	private LoadingDialog mLoading;
	private MyInformation mUser;
	private Uri           mOrigUri;
	private Uri           mCropUri;
	private File          mProtraitFile;
	private Bitmap        mProtraitBitmap;
	private String        mProtraitPath;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oschina_user_info);		
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView()
	{
		mBack = (ImageView)findViewById(R.id.user_info_back);
		mRefresh = (ImageView)findViewById(R.id.user_info_refresh);
		mEditer = (Button)findViewById(R.id.user_info_editer);
		mFace = (ImageView)findViewById(R.id.user_info_userface);
		mGender = (ImageView)findViewById(R.id.user_info_gender);
		mName = (TextView)findViewById(R.id.user_info_username);
		mJointime = (TextView)findViewById(R.id.user_info_jointime);
		mFrom = (TextView)findViewById(R.id.user_info_from);
		mDevplatform = (TextView)findViewById(R.id.user_info_devplatform);
		mExpertise = (TextView)findViewById(R.id.user_info_expertise);
		mFollowers = (TextView)findViewById(R.id.user_info_followers);
		mFans = (TextView)findViewById(R.id.user_info_fans);
		mFavorites = (TextView)findViewById(R.id.user_info_favorites);
		mFavoritesView = (LinearLayout)findViewById(R.id.user_info_favorites_ll);
		mFollowersView = (LinearLayout)findViewById(R.id.user_info_followers_ll);
		mFansView = (LinearLayout)findViewById(R.id.user_info_fans_ll);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mRefresh.setOnClickListener(this);
		mEditer.setOnClickListener(this);
		mFavoritesView.setOnClickListener(this);
		mFansView.setOnClickListener(this);
		mFollowersView.setOnClickListener(this);
	}
	
	private void initData()
	{
		loadUserInfoThread(false);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.user_info_back:
		{
			finish();
			break;
		}
		case R.id.user_info_refresh:
		{
			loadUserInfoThread(true);
			break;
		}
		case R.id.user_info_editer:
		{
			CharSequence[] items = { "手机相册", "手机拍照"};
			imageChooseItem(items);
			break;
		}
		case R.id.user_info_followers_ll:
		{
			int mFollowers = mUser != null ? mUser.getFollowerscount() : 0;
			int mFans = mUser != null ? mUser.getFanscount() : 0;
			Intent intent = new Intent(mContext, OSChinaUserFriendActivity.class);
			intent.putExtra("friend_type", FriendList.TYPE_FOLLOWER);
			intent.putExtra("friend_followers", mFollowers);
			intent.putExtra("friend_fans", mFans);
			startActivity(intent);
			break;
		}
		case R.id.user_info_fans_ll:
		{
			int mFollowers = mUser!=null ? mUser.getFollowerscount() : 0;
			int mFans = mUser!=null ? mUser.getFanscount() : 0;
			Intent intent = new Intent(mContext, OSChinaUserFriendActivity.class);
			intent.putExtra("friend_type", FriendList.TYPE_FANS);
			intent.putExtra("friend_followers", mFollowers);
			intent.putExtra("friend_fans", mFans);
			startActivity(intent);
			break;
		}
		case R.id.user_info_favorites_ll:
		{
			Intent intent = new Intent(mContext, OSChinaUserFavoriteActivity.class);
		    startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
	
	private void loadUserInfoThread(final boolean isRefresh)
	{
		mLoading = new LoadingDialog(this);		
		mLoading.show();
		new Thread()
		{
			public void run() 
			{
				Message msg = new Message();
				try 
				{
					MyInformation mUser = UserHelper.getMyInformation(mContext, isRefresh);
					msg.what = 1;
	                msg.obj = mUser;
	            }
				catch (Exception e) 
				{
	            	e.printStackTrace();
	            	msg.what = -1;
	                msg.obj = e;
	            }
				mHandler.sendMessage(msg);
			}
		}.start();
	}
	
	/**
	 * 操作选择
	 * @param items
	 */
	public void imageChooseItem(CharSequence[] items )
	{
		AlertDialog imageDialog = new AlertDialog.Builder(this)
		.setTitle("上传头像").setIcon(android.R.drawable.btn_star)
		.setItems(items, new DialogInterface.OnClickListener() 
		{
			@SuppressLint("SimpleDateFormat")
			public void onClick(DialogInterface dialog, int item) 
			{
				//判断是否挂载了SD卡
				String storageState = Environment.getExternalStorageState();
				if (storageState.equals(Environment.MEDIA_MOUNTED)) 
				{
					File savedir = new File(FILE_SAVEPATH);
					if (!savedir.exists()) 
					{
						savedir.mkdirs();
					}
				} 
				else 
				{
					UIHelper.ToastMessage(mContext, "无法保存上传的头像，请检查SD卡是否挂载");
					return;
				}

				//输出裁剪的临时文件
				String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				//照片命名
				String origFileName = "osc_" + timeStamp + ".jpg";
				String cropFileName = "osc_crop_" + timeStamp + ".jpg";

				//裁剪头像的绝对路径
				mProtraitPath = FILE_SAVEPATH + cropFileName;
				mProtraitFile = new File(mProtraitPath);
				mOrigUri = Uri.fromFile(new File(FILE_SAVEPATH,origFileName));
				mCropUri = Uri.fromFile(mProtraitFile);

				if (item == 0) 
				{
					//相册选图
					startActionPickCrop(mCropUri);
				}
				else if (item == 1) 
				{
					//手机拍照
					startActionCamera(mOrigUri);
				}
			}
		}).create();
		imageDialog.show();
	}
	
	/**
	 * 选择图片裁剪
	 * @param output
	 */
	private void startActionPickCrop(Uri output) 
	{
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.putExtra("output", output);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);    // 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", CROP); // 输出图片大小
		intent.putExtra("outputY", CROP);
		startActivityForResult(Intent.createChooser(intent, "选择图片"),ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
	}
	
	/**
	 * 相机拍照
	 * @param output
	 */
	private void startActionCamera(Uri output) 
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
		startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
	}
	
	@Override 
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{ 
    	if(resultCode != RESULT_OK) return;
		
    	switch(requestCode)
    	{
    		case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
    		{
    			//拍照后裁剪
    			startActionCrop(mOrigUri, mCropUri);
    			break;
    		}
    		case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
    		case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
    		{
    			//上传新照片
    			uploadNewPhoto();
    			break;
    		}	
    	}
	}
	
	/**
	 * 拍照后裁剪
	 * @param data 原始图片
	 * @param output 裁剪后图片
	 */
	private void startActionCrop(Uri data, Uri output) 
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", output);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);    // 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", CROP); // 输出图片大小
		intent.putExtra("outputY", CROP);
		startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
	}
	
	/**
	 * 上传新照片
	 */
	@SuppressLint("HandlerLeak")
	private void uploadNewPhoto() 
	{
		final Handler handler = new Handler()
		{
			public void handleMessage(Message msg) 
			{
				if(mLoading != null)	mLoading.dismiss();
				if(msg.what == 1 && msg.obj != null)
				{
					Result res = (Result)msg.obj;
					//提示信息
					UIHelper.ToastMessage(mContext, res.getErrorMessage());
					if(res.OK())
					{
						//显示新头像
						mFace.setImageBitmap(mProtraitBitmap);
					}
				}
				else if(msg.what == -1 && msg.obj != null)
				{
					Toast.makeText(mContext, "上传头像失败", Toast.LENGTH_SHORT).show();
				}
			}
		};
			
		if(mLoading != null)
		{
			mLoading.setLoadText("正在上传头像···");
			mLoading.show();	
		}
		
		new Thread()
		{
			public void run() 
			{
	        	//获取头像缩略图
	        	if(!StringUtils.isEmpty(mProtraitPath) && mProtraitFile.exists())
	        	{
	        		mProtraitBitmap = ImageUtils.loadImgThumbnail(mProtraitPath, 200, 200);
	        	}
		        
				if(mProtraitBitmap != null)
				{	
					Message msg = new Message();
					try 
					{
						Result res = UserHelper.updatePortrait(mContext, mProtraitFile);
						if(res!=null && res.OK())
						{
							//保存新头像到缓存
							String filename = FileUtils.getFileName(mUser.getFace());
							ImageUtils.saveImage(mContext, filename, mProtraitBitmap);
						}
						msg.what = 1;
						msg.obj = res;
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
						msg.what = -1;
						msg.obj = e;
					} 
					handler.sendMessage(msg);
				}				
			};
		}.start();
    }
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			if(mLoading != null)	mLoading.dismiss();
			if(msg.what == 1 && msg.obj != null)
			{
				mUser = (MyInformation)msg.obj;
				//加载用户头像
				UIHelper.showUserFace(mFace, mUser.getFace());
				
				//用户性别
				if(mUser.getGender() == 1)
				{
					mGender.setImageResource(R.drawable.oschina_gender_man);
				}
				else
				{
					mGender.setImageResource(R.drawable.oschina_gender_woman);
				}
					
				//其他资料
				mName.setText(mUser.getName());
				mJointime.setText(StringUtils.friendly_time(mUser.getJointime()));
				mFrom.setText(mUser.getFrom());
				mDevplatform.setText(mUser.getDevplatform());
				mExpertise.setText(mUser.getExpertise());
				mFollowers.setText(mUser.getFollowerscount()+"");
				mFans.setText(mUser.getFanscount()+"");
				mFavorites.setText(mUser.getFavoritecount()+"");
			}
			else if(msg.obj != null)
			{
				Toast.makeText(mContext, "用户头像加载失败", Toast.LENGTH_SHORT).show();
			}
		}
	};		
}
