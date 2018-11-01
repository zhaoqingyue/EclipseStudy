/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinImageFilterCropActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 图片剪切类
 * @author: zhaoqy
 * @date: 2015-11-9 上午9:38:55
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.util.PhotoUtil;
import com.zhaoqy.app.demo.menu.kaixin.view.CropImage;
import com.zhaoqy.app.demo.menu.kaixin.view.CropImageView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MenuKaixinImageFilterCropActivity extends MenuKaixinBaseActivity implements OnClickListener
{
	public static final int SHOW_PROGRESS = 0;
	public static final int REMOVE_PROGRESS = 1;
	private CropImageView mDisplay;
	private ProgressBar   mProgressBar;
	private Context   mContext;
	private TextView  mCancel;
	private TextView  mTitle;
	private TextView  mDetermine;
	private Button    mLeft;
	private Button    mRight;
	private String    mPath;      //修改的图片地址
	private Bitmap    mBitmap;    //修改的图片
	private CropImage mCropImage; //裁剪工具类

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_image_filter_crop);
		mContext = this;
		
		initView();
		setListener();
		init();
	}

	private void initView() 
	{
		mCancel = (TextView) findViewById(R.id.id_title_left_text);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mDetermine = (TextView) findViewById(R.id.id_title_right_text);
		
		mDisplay = (CropImageView) findViewById(R.id.imagefilter_crop_display);
		mProgressBar = (ProgressBar) findViewById(R.id.imagefilter_crop_progressbar);
		mLeft = (Button) findViewById(R.id.imagefilter_crop_left);
		mRight = (Button) findViewById(R.id.imagefilter_crop_right);
	}

	private void setListener() 
	{
		mCancel.setOnClickListener(this);
		mDetermine.setOnClickListener(this);
		mLeft.setOnClickListener(this);
		mRight.setOnClickListener(this);
	}

	@SuppressLint("ShowToast")
	private void init() 
	{
		mCancel.setVisibility(View.VISIBLE);
		mCancel.setText("取消");
		mTitle.setText("照片裁剪");
		mDetermine.setVisibility(View.VISIBLE);
		mDetermine.setText("确定");
		
		//接收传递的图片地址
		mPath = getIntent().getStringExtra("path");
		try 
		{
			//获取修改的图片
			mBitmap = PhotoUtil.createBitmap(mPath, mScreenWidth, mScreenHeight);
			// 如果图片不存在,则返回,存在则初始化
			if (mBitmap == null) 
			{
				Toast.makeText(mContext, "没有找到图片", 0).show();
				setResult(RESULT_CANCELED);
				finish();
			} 
			else 
			{
				resetImageView(mBitmap);
			}
		} 
		catch (Exception e) 
		{
			Toast.makeText(mContext, "没有找到图片", 0).show();
			setResult(RESULT_CANCELED);
			finish();
		}
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_text:
		{
			backDialog();
			break;
		}
		case R.id.id_title_right_text:
		{
			//保存修改的图片到本地,并返回图片地址
			mPath = PhotoUtil.saveToLocal(mCropImage.cropAndSave());
			Intent intent = new Intent();
			intent.putExtra("path", mPath);
			setResult(RESULT_OK, intent);
			finish();
			break;
		}
		case R.id.imagefilter_crop_left:
		{
			//左旋转
			mCropImage.startRotate(270.f);
			break;
		}
		case R.id.imagefilter_crop_right:
		{
			//有旋转
			mCropImage.startRotate(90.f);
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 初始化图片显示
	 * @param b
	 */
	private void resetImageView(Bitmap b) 
	{
		mDisplay.clear();
		mDisplay.setImageBitmap(b);
		mDisplay.setImageBitmapResetBase(b, true);
		mCropImage = new CropImage(this, mDisplay, handler);
		mCropImage.crop(b);
	}

	/**
	 * 控制进度条
	 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case SHOW_PROGRESS:
				mProgressBar.setVisibility(View.VISIBLE);
				break;
			case REMOVE_PROGRESS:
				handler.removeMessages(SHOW_PROGRESS);
				mProgressBar.setVisibility(View.INVISIBLE);
				break;
			}
		}
	};

	/**
	 * 返回对话框
	 */
	private void backDialog() 
	{
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("开心网");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage("你确定要取消编辑吗?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
				setResult(RESULT_CANCELED);
				finish();
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

	public void onBackPressed() 
	{
		backDialog();
	}
}
