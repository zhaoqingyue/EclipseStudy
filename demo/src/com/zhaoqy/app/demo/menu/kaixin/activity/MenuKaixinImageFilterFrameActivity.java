/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinImageFilterFrameActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 图片边框类
 * @author: zhaoqy
 * @date: 2015-11-9 上午11:39:12
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.IOException;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.util.PhotoUtil;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuKaixinImageFilterFrameActivity extends MenuKaixinBaseActivity implements OnClickListener
{
	private Context     mContext;
	private TextView    mCancel;
	private TextView    mTitle;
	private TextView    mDetermine;
	private ImageView   mDisplay;
	private ImageButton mFrame_1;
	private ImageButton mFrame_2;
	private ImageButton mFrame_3;
	private ImageButton mFrame_4;
	private ImageButton mFrame_5;
	private ImageButton mFrame_6;
	private ImageButton mFrame_7;
	private ImageButton mFrame_8;
	private ImageButton mFrame_9;
	private ImageButton mFrame_10;
	private String      mPath;        //图片地址
	private Bitmap      mOldBitmap;   //旧图片
	private Bitmap      mNewBitmap;   //新图片
	private int         mFrameId = 0; //边框编号

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_image_filter_frame);
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
		mDisplay = (ImageView) findViewById(R.id.imagefilter_frame_display);
		mFrame_1 = (ImageButton) findViewById(R.id.imagefilter_frame_frame1);
		mFrame_2 = (ImageButton) findViewById(R.id.imagefilter_frame_frame2);
		mFrame_3 = (ImageButton) findViewById(R.id.imagefilter_frame_frame3);
		mFrame_4 = (ImageButton) findViewById(R.id.imagefilter_frame_frame4);
		mFrame_5 = (ImageButton) findViewById(R.id.imagefilter_frame_frame5);
		mFrame_6 = (ImageButton) findViewById(R.id.imagefilter_frame_frame6);
		mFrame_7 = (ImageButton) findViewById(R.id.imagefilter_frame_frame7);
		mFrame_8 = (ImageButton) findViewById(R.id.imagefilter_frame_frame8);
		mFrame_9 = (ImageButton) findViewById(R.id.imagefilter_frame_frame9);
		mFrame_10 = (ImageButton) findViewById(R.id.imagefilter_frame_frame10);
	}

	private void setListener() 
	{
		mCancel.setOnClickListener(this);
		mDetermine.setOnClickListener(this);
		mFrame_1.setOnClickListener(this);
		mFrame_2.setOnClickListener(this);
		mFrame_3.setOnClickListener(this);
		mFrame_4.setOnClickListener(this);
		mFrame_5.setOnClickListener(this);
		mFrame_6.setOnClickListener(this);
		mFrame_7.setOnClickListener(this);
		mFrame_8.setOnClickListener(this);
		mFrame_9.setOnClickListener(this);
		mFrame_10.setOnClickListener(this);
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
			//如果id为0,代表没做任何操作,则无需返回值,否则则保存当前修改的图片并返回地址
			if (mFrameId == 0) 
			{
				setResult(RESULT_CANCELED);
				finish();
			} 
			else 
			{
				mPath = PhotoUtil.saveToLocal(mNewBitmap);
				Intent intent = new Intent();
				intent.putExtra("path", mPath);
				setResult(RESULT_OK, intent);
				finish();
			}
			break;
		}
		case R.id.imagefilter_frame_frame1:
		{
			mFrameId = 1;
			mNewBitmap = PhotoUtil.combinateFrame(mContext, mOldBitmap, "frame1");
			mDisplay.setImageBitmap(mNewBitmap);
			break;
		}
		case R.id.imagefilter_frame_frame2:
		{
			mFrameId = 2;
			mNewBitmap = PhotoUtil.combinateFrame(mContext, mOldBitmap, "frame2");
			mDisplay.setImageBitmap(mNewBitmap);
			break;
		}
		case R.id.imagefilter_frame_frame3:
		{
			mFrameId = 3;
			mNewBitmap = PhotoUtil.combinateFrame(mContext, mOldBitmap, "frame3");
			mDisplay.setImageBitmap(mNewBitmap);
			break;
		}
		case R.id.imagefilter_frame_frame4:
		{
			mFrameId = 4;
			mNewBitmap = PhotoUtil.combinateFrame(mContext, mOldBitmap, "frame4");
			mDisplay.setImageBitmap(mNewBitmap);
			break;
		}
		case R.id.imagefilter_frame_frame5:
		{
			mFrameId = 5;
			try 
			{
				mNewBitmap = PhotoUtil.addBigFrame(mOldBitmap, BitmapFactory.decodeStream(getAssets().open("frames/frame5/mist.png")));
				mDisplay.setImageBitmap(mNewBitmap);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			break;
		}
		case R.id.imagefilter_frame_frame6:
		{
			mFrameId = 6;
			try {
				mNewBitmap = PhotoUtil.addBigFrame(mOldBitmap, BitmapFactory.decodeStream(getAssets().open("frames/frame6/love.png")));
				mDisplay.setImageBitmap(mNewBitmap);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			break;
		}
		case R.id.imagefilter_frame_frame7:
		{
			mFrameId = 7;
			mNewBitmap = PhotoUtil.combinateFrame(mContext, mOldBitmap, "frame7");
			mDisplay.setImageBitmap(mNewBitmap);
			break;
		}
		case R.id.imagefilter_frame_frame8:
		{
			mFrameId = 8;
			try 
			{
				mNewBitmap = PhotoUtil.addBigFrame(mOldBitmap, BitmapFactory.decodeStream(getAssets().open("frames/frame8/transparent.png")));
				mDisplay.setImageBitmap(mNewBitmap);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			break;
		}
		case R.id.imagefilter_frame_frame9:
		{
			mFrameId = 9;
			try 
			{
				mNewBitmap = PhotoUtil.addBigFrame(mOldBitmap,BitmapFactory.decodeStream(getAssets().open("frames/frame9/black.png")));
				mDisplay.setImageBitmap(mNewBitmap);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			break;
		}
		case R.id.imagefilter_frame_frame10:
		{
			mFrameId = 10;
			mNewBitmap = PhotoUtil.combinateFrame(mContext, mOldBitmap, "frame10");
			mDisplay.setImageBitmap(mNewBitmap);
			break;
		}
		default:
			break;
		}
	}
	
	private void init() 
	{
		mCancel.setVisibility(View.VISIBLE);
		mCancel.setText("取消");
		mTitle.setText("边框");
		mDetermine.setVisibility(View.VISIBLE);
		mDetermine.setText("确定");
		
		mPath = getIntent().getStringExtra("path");
		mOldBitmap = mApplication.getPhoneAlbum(mPath);
		mDisplay.setImageBitmap(mOldBitmap);
	}

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
		//返回对话框
		backDialog();
	}
}
