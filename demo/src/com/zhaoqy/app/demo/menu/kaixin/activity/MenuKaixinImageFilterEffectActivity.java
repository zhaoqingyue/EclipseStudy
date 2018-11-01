/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinImageFilterEffectActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 图片特效类
 * @author: zhaoqy
 * @date: 2015-11-9 上午10:17:06
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.util.PhotoUtil;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuKaixinImageFilterEffectActivity extends MenuKaixinBaseActivity implements OnClickListener
{
	private Context     mContext;
	private TextView    mCancel;
	private TextView    mTitle;
	private TextView    mDetermine;
	private ImageView   mDisplay;
	private ImageButton mEffect_1;
	private ImageButton mEffect_2;
	private ImageButton mEffect_3;
	private ImageButton mEffect_4;
	private ImageButton mEffect_5;
	private ImageButton mEffect_6;
	private ImageButton mEffect_7;
	private ImageButton mEffect_8;
	private ImageButton mEffect_9;
	private ImageButton mEffect_10;
	private String      mPath;         //图片地址
	private Bitmap      mOldBitmap;    //旧图片
	private Bitmap      mNewBitmap;    //新图片
	private int         mEffectId = 1; //特效的编号

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_image_filter_effect);
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
		mDisplay = (ImageView) findViewById(R.id.imagefilter_effect_display);
		mEffect_1 = (ImageButton) findViewById(R.id.imagefilter_effect_effect1);
		mEffect_2 = (ImageButton) findViewById(R.id.imagefilter_effect_effect2);
		mEffect_3 = (ImageButton) findViewById(R.id.imagefilter_effect_effect3);
		mEffect_4 = (ImageButton) findViewById(R.id.imagefilter_effect_effect4);
		mEffect_5 = (ImageButton) findViewById(R.id.imagefilter_effect_effect5);
		mEffect_6 = (ImageButton) findViewById(R.id.imagefilter_effect_effect6);
		mEffect_7 = (ImageButton) findViewById(R.id.imagefilter_effect_effect7);
		mEffect_8 = (ImageButton) findViewById(R.id.imagefilter_effect_effect8);
		mEffect_9 = (ImageButton) findViewById(R.id.imagefilter_effect_effect9);
		mEffect_10 = (ImageButton) findViewById(R.id.imagefilter_effect_effect10);
	}

	private void setListener() 
	{
		mCancel.setOnClickListener(this);
		mDetermine.setOnClickListener(this);
		mEffect_1.setOnClickListener(this);
		mEffect_2.setOnClickListener(this);
		mEffect_3.setOnClickListener(this);
		mEffect_4.setOnClickListener(this);
		mEffect_5.setOnClickListener(this);
		mEffect_6.setOnClickListener(this);
		mEffect_7.setOnClickListener(this);
		mEffect_8.setOnClickListener(this);
		mEffect_9.setOnClickListener(this);
		mEffect_10.setOnClickListener(this);
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
			//如果是1代表原图,不做任何操作返回,否则则保存图片到本地并返回地址
			if (mEffectId == 1) 
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
		case R.id.imagefilter_effect_effect1:
		{
			//原图
			mDisplay.setImageBitmap(mOldBitmap);
			mEffectId = 1;
			break;
		}
		case R.id.imagefilter_effect_effect2:
		{
			//Lomo
			mNewBitmap = PhotoUtil.lomoFilter(mOldBitmap);
			mDisplay.setImageBitmap(mNewBitmap);
			mEffectId = 2;
			break;
		}
		case R.id.imagefilter_effect_effect3:
		{
			//印象派
			Toast.makeText(mContext, "暂时没有找到该效果", Toast.LENGTH_SHORT).show();
			break;
		}
		case R.id.imagefilter_effect_effect4:
		{
			//优雅
			Toast.makeText(mContext, "暂时没有找到该效果", Toast.LENGTH_SHORT).show();
			break;
		}
		case R.id.imagefilter_effect_effect5:
		{
			//小清新
			Toast.makeText(mContext, "暂时没有找到该效果", Toast.LENGTH_SHORT).show();
			break;
		}
		case R.id.imagefilter_effect_effect6:
		{
			//单纯
			mNewBitmap = PhotoUtil.handleImage(mOldBitmap, 0, 127, 127);
			mDisplay.setImageBitmap(mNewBitmap);
			mEffectId = 6;
			break;
		}
		case R.id.imagefilter_effect_effect7:
		{
			//沉静
			Toast.makeText(mContext, "暂时没有找到该效果", Toast.LENGTH_SHORT).show();
			break;
		}
		case R.id.imagefilter_effect_effect8:
		{
			//灿烂
			Toast.makeText(mContext, "暂时没有找到该效果", Toast.LENGTH_SHORT).show();
			break;
		}
		case R.id.imagefilter_effect_effect9:
		{
			//旧时光
			mNewBitmap = PhotoUtil.oldTimeFilter(mOldBitmap);
			mDisplay.setImageBitmap(mNewBitmap);
			mEffectId = 9;
			break;
		}
		case R.id.imagefilter_effect_effect10:
		{
			//暖意
			mNewBitmap = PhotoUtil.warmthFilter(mOldBitmap, mOldBitmap.getWidth() / 2, mOldBitmap.getHeight() / 2);
			mDisplay.setImageBitmap(mNewBitmap);
			mEffectId = 10;
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
		mTitle.setText("特效");
		mDetermine.setVisibility(View.VISIBLE);
		mDetermine.setText("确定");
		
		//获取图片地址、图片并显示图片
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
		backDialog();
	}
}
