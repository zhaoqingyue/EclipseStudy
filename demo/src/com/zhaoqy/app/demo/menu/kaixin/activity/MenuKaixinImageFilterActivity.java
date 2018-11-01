/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinImageFilterActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 图片编辑类
 * @author: zhaoqy
 * @date: 2015-11-6 下午5:12:30
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.util.HashMap;
import java.util.Map;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.util.ActivityForResultUtil;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuKaixinImageFilterActivity extends MenuKaixinBaseActivity implements OnClickListener
{
	private Context     mContext;
	private TextView    mCancel;
	private TextView    mFinish;
	private ImageButton mBack;
	private ImageButton mForward;
	private ImageView   mDisplay;
	private Button 		mCut;
	private Button 		mEffect;
	private Button 		mFace;
	private Button 		mFrame;
	private String 		mOldPath;      //旧图片地址
	private Bitmap 		mOldBitmap;    //旧图片
	private String 		mNewPath;      //新图片地址
	private Bitmap 		mNewBitmap;    //新图片
	private boolean 	mIsOld = true; //是否是选择了旧图片
	private boolean 	mIsSetResult = false; //是否是要返回数据

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_image_filter);
		mContext = this;
		
		initView();
		setListener();
		init();
	}

	private void initView() 
	{
		mCancel = (TextView) findViewById(R.id.imagefilter_cancel);
		mFinish = (TextView) findViewById(R.id.imagefilter_finish);
		mBack = (ImageButton) findViewById(R.id.imagefilter_back);
		mForward = (ImageButton) findViewById(R.id.imagefilter_forward);
		mDisplay = (ImageView) findViewById(R.id.imagefilter_display);
		mCut = (Button) findViewById(R.id.imagefilter_cut);
		mEffect = (Button) findViewById(R.id.imagefilter_effect);
		mFace = (Button) findViewById(R.id.imagefilter_face);
		mFrame = (Button) findViewById(R.id.imagefilter_frame);
	}

	private void setListener() 
	{
		mCancel.setOnClickListener(this);
		mFinish.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mForward.setOnClickListener(this);
		mCut.setOnClickListener(this);
		mEffect.setOnClickListener(this);
		mFace.setOnClickListener(this);
		mFrame.setOnClickListener(this);
	}

	private void init() 
	{
		//初始化界面按钮设为不可用
		mBack.setImageResource(R.drawable.menu_kaixin_image_arrow_back_normal);
		mForward.setImageResource(R.drawable.menu_kaixin_image_arrow_forward_normal);
		mBack.setEnabled(false);
		mForward.setEnabled(false);
		//获取是否返回数据
		mIsSetResult = getIntent().getBooleanExtra("isSetResult", false);
		//接收传递的图片地址
		mOldPath = getIntent().getStringExtra("path");
		mNewPath = getIntent().getStringExtra("path");
		mOldBitmap = mApplication.getPhoneAlbum(mOldPath);
		mNewBitmap = mApplication.getPhoneAlbum(mNewPath);
		//显示图片
		mDisplay.setImageBitmap(mOldBitmap);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.imagefilter_cancel:
		{
			finish();
			break;
		}
		case R.id.imagefilter_finish:
		{
			//判断是否要返回数据
			if (mIsSetResult) 
			{
				// 根据是否选择旧图片返回图片地址
				Intent intent = new Intent();
				if (mIsOld) 
				{
					intent.putExtra("path", mOldPath);
				} 
				else 
				{
					intent.putExtra("path", mNewPath);
				}
				setResult(RESULT_OK, intent);
			} 
			else 
			{
				//根据是否选择旧图片添加一个新的图片并跳转到上传图片界面
				Map<String, String> map = new HashMap<String, String>();
				if (mIsOld) 
				{
					map.put("image_path", mOldPath);
				} 
				else 
				{
					map.put("image_path", mNewPath);
				}
				mApplication.mAlbumList.add(map);
				startActivity(new Intent(mContext, MenuKaixinPhotoShareActivity.class));
			}
			finish();
			break;
		}
		case R.id.imagefilter_back:
		{
			//选择旧图片
			mIsOld = true;
			mBack.setImageResource(R.drawable.menu_kaixin_image_arrow_back_normal);
			mForward.setImageResource(R.drawable.menu_kaixin_image_filter_forward);
			mBack.setEnabled(false);
			mForward.setEnabled(true);
			mDisplay.setImageBitmap(mOldBitmap);
			break;
		}
		case R.id.imagefilter_forward:
		{
			//选择新图片
			mIsOld = false;
			mBack.setImageResource(R.drawable.menu_kaixin_image_filter_back);
			mForward.setImageResource(R.drawable.menu_kaixin_image_arrow_forward_normal);
			mBack.setEnabled(true);
			mForward.setEnabled(false);
			mDisplay.setImageBitmap(mNewBitmap);
			break;
		}
		case R.id.imagefilter_cut:
		{
			//跳转到裁剪界面,并传递图片地址
			Intent intent = new Intent();
			intent.setClass(mContext, MenuKaixinImageFilterCropActivity.class);
			if (mIsOld) 
			{
				intent.putExtra("path", mOldPath);
			}
			else 
			{
				intent.putExtra("path", mNewPath);
			}
			startActivityForResult(intent, ActivityForResultUtil.REQUESTCODE_IMAGEFILTER_CROP);
			break;
		}
		case R.id.imagefilter_effect:
		{
			//跳转到特效界面,并传递图片地址
			Intent intent = new Intent();
			intent.setClass(mContext, MenuKaixinImageFilterEffectActivity.class);
			if (mIsOld) 
			{
				intent.putExtra("path", mOldPath);
			} 
			else 
			{
				intent.putExtra("path", mNewPath);
			}
			startActivityForResult(intent, ActivityForResultUtil.REQUESTCODE_IMAGEFILTER_EFFECT);
			break;
		}
		case R.id.imagefilter_face:
		{
			//跳转到表情界面,并传递图片地址
			Intent intent = new Intent();
			intent.setClass(mContext, MenuKaixinImageFilterFaceActivity.class);
			if (mIsOld) 
			{
				intent.putExtra("path", mOldPath);
			} 
			else 
			{
				intent.putExtra("path", mNewPath);
			}
			startActivityForResult(intent, ActivityForResultUtil.REQUESTCODE_IMAGEFILTER_FACE);
			break;
		}
		case R.id.imagefilter_frame:
		{
			//跳转到边框界面,并传递图片地址
			Intent intent = new Intent();
			intent.setClass(mContext, MenuKaixinImageFilterFrameActivity.class);
			if (mIsOld) 
			{
				intent.putExtra("path", mOldPath);
			} 
			else 
			{
				intent.putExtra("path", mNewPath);
			}
			startActivityForResult(intent, ActivityForResultUtil.REQUESTCODE_IMAGEFILTER_FRAME);
			break;
		}
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) 
		{
			//接收修改后的图片地址,并更新
			if (mIsOld) 
			{
				mNewPath = data.getStringExtra("path");
				mNewBitmap = mApplication.getPhoneAlbum(mNewPath);
			}
			else 
			{
				mOldPath = mNewPath;
				mOldBitmap = mNewBitmap;
				mNewPath = data.getStringExtra("path");
				mNewBitmap = mApplication.getPhoneAlbum(mNewPath);
			}
			mIsOld = false;
			mBack.setImageResource(R.drawable.menu_kaixin_image_filter_back);
			mForward.setImageResource(R.drawable.menu_kaixin_image_arrow_forward_normal);
			mBack.setEnabled(true);
			mForward.setEnabled(false);
			mDisplay.setImageBitmap(mNewBitmap);
		}
	}
}
