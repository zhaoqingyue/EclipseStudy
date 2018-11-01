/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinMainActivity.java
 * @Prject: DemoKaixin
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 主页面
 * @author: zhaoqy
 * @date: 2015-11-12 上午9:34:05
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.util.ActivityForResultUtil;
import com.zhaoqy.app.demo.menu.kaixin.util.PhotoUtil;

public class MenuKaixinMainActivity extends MenuKaixinBaseActivity implements OnClickListener
{
	private static final int INTERVAL = 2000;
	private long mExitTime;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mItem0;
	private TextView  mItem1;
	private TextView  mItem2;
	private TextView  mItem3;
	private TextView  mItem4;
	private TextView  mItem5;
	private TextView  mItem6;
	private TextView  mItem7;
	private TextView  mItem8;
	private TextView  mItem9;
	private TextView  mItem10;
	private TextView  mItem11;
	private TextView  mItem12;
	private TextView  mItem13;
	private TextView  mItem14;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_main);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mItem0 = (TextView) findViewById(R.id.id_menu_item0);
		mItem1 = (TextView) findViewById(R.id.id_menu_item1);
		mItem2 = (TextView) findViewById(R.id.id_menu_item2);
		mItem3 = (TextView) findViewById(R.id.id_menu_item3);
		mItem4 = (TextView) findViewById(R.id.id_menu_item4);
		mItem5 = (TextView) findViewById(R.id.id_menu_item5);
		mItem6 = (TextView) findViewById(R.id.id_menu_item6);
		mItem7 = (TextView) findViewById(R.id.id_menu_item7);
		mItem8 = (TextView) findViewById(R.id.id_menu_item8);
		mItem9 = (TextView) findViewById(R.id.id_menu_item9);
		mItem10 = (TextView) findViewById(R.id.id_menu_item10);
		mItem11 = (TextView) findViewById(R.id.id_menu_item11);
		mItem12 = (TextView) findViewById(R.id.id_menu_item12);
		mItem13 = (TextView) findViewById(R.id.id_menu_item13);
		mItem14 = (TextView) findViewById(R.id.id_menu_item14);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mItem0.setOnClickListener(this);
		mItem1.setOnClickListener(this);
		mItem2.setOnClickListener(this);
		mItem3.setOnClickListener(this);
		mItem4.setOnClickListener(this);
		mItem5.setOnClickListener(this);
		mItem6.setOnClickListener(this);
		mItem7.setOnClickListener(this);
		mItem8.setOnClickListener(this);
		mItem9.setOnClickListener(this);
		mItem10.setOnClickListener(this);
		mItem11.setOnClickListener(this);
		mItem12.setOnClickListener(this);
		mItem13.setOnClickListener(this);
		mItem14.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("开心网");
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
		case R.id.id_menu_item0:
		{
			//我的主页
			Intent intent = new Intent(mContext, MainUserActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item1:
		{
			//首页
			Intent intent = new Intent(mContext, MainHomeActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item2:
		{
			//消息
			Intent intent = new Intent(mContext, MainMessageActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item3:
		{
			//好友
			Intent intent = new Intent(mContext, MainFriendsActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item4:
		{
			//照片
			Intent intent = new Intent(mContext, MainPhotoActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item5:
		{
			//转帖
			Intent intent = new Intent(mContext, MainViewedActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item6:
		{
			//礼物
			Intent intent = new Intent(mContext, MainGiftsActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item7:
		{
			//游戏
			Intent intent = new Intent(mContext, MainRecommendActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item8:
		{
			//附近
			Intent intent = new Intent(mContext, MainLbsActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item9:
		{
			//设置
			Intent intent = new Intent(mContext, MenuKaixinSetUpActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item10:
		{
			//语音
			Intent intent = new Intent(mContext, MenuKaixinVoiceActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item11:
		{
			//拍照
			PhotoDialog(); 
			break;
		}
		case R.id.id_menu_item12:
		{
			//记录
			Intent intent = new Intent(mContext, MenuKaixinWriteRecordActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item13:
		{
			//签到
			Intent intent = new Intent(mContext, MenuKaixinCheckInActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_menu_item14:
		{
			//判断两次返回时间间隔,小于两秒则退出程序
			if (System.currentTimeMillis() - mExitTime > INTERVAL) 
			{
				Toast.makeText(this, "再按一次返回键,可直接退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} 
			else 
			{
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) 
		{
		case ActivityForResultUtil.REQUESTCODE_UPLOADPHOTO_CAMERA:
		{
			if (resultCode == RESULT_OK) 
			{
				if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) 
				{
					Toast.makeText(this, "SD不可用", Toast.LENGTH_SHORT).show();
					return;
				}
				//通过照相上传图片
				Intent intent = new Intent();
				intent.setClass(mContext, MenuKaixinImageFilterActivity.class);
				String path = PhotoUtil.saveToLocal(PhotoUtil.createBitmap(mApplication.mUploadPhotoPath, mScreenWidth, mScreenHeight));
				intent.putExtra("path", path);
				startActivity(intent);
			}
			else 
			{
				Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
			}
			break;	
		}
		default:
			break;
		}
	}
	
	private void PhotoDialog() 
	{
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("上传照片至开心网");
		builder.setItems(new String[] { "拍照上传", "上传手机中的照片" }, new DialogInterface.OnClickListener() 
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
					startActivityForResult(intent,ActivityForResultUtil.REQUESTCODE_UPLOADPHOTO_CAMERA);
					break;
				case 1:
					mContext.startActivity(new Intent(mContext, MenuKaixinPhoneAlbumActivity.class));
					break;
				}		
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
		builder.create().show();
	}
}
