/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SafetyCrankTabhost2Activity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.activity
 * @Description: 标记管理
 * @author: zhaoqy
 * @date: 2015-12-11 上午11:35:31
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;

@SuppressLint("InlinedApi")
public class SafetyCrankTabhostMarkActivity extends Activity implements OnClickListener
{
	private Context        mContext;
	private ImageView      mImageView;
	private ImageView      mAddImg;
	private TextView       mAddText;
	private RelativeLayout mItem1;
	private RelativeLayout mItem2;
	private RelativeLayout mItem3;
	private RelativeLayout mItem4;
	private RelativeLayout mItem5;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_crank_tabhost_mark);
		mContext = this;
		
		initView();
		setListener();
	}
	
	private void initView() 
	{
		mImageView = (ImageView) findViewById(R.id.crank_show_imageview);
		mAddImg = (ImageView) findViewById(R.id.crank_addimg);
		mAddText = (TextView) findViewById(R.id.crank_addtext);
		mItem1 = (RelativeLayout) findViewById(R.id.tag_crankcall);
		mItem2 = (RelativeLayout) findViewById(R.id.tag_crankad);
		mItem3 = (RelativeLayout) findViewById(R.id.tag_crankhouse);
		mItem4 = (RelativeLayout) findViewById(R.id.tag_crankeat);
		mItem5 = (RelativeLayout) findViewById(R.id.tag_cranke_add);
	}
	
	private void setListener() 
	{
		mImageView.setOnClickListener(this);
		mItem1.setOnClickListener(this);
		mItem2.setOnClickListener(this);
		mItem3.setOnClickListener(this);
		mItem4.setOnClickListener(this);
		mItem5.setOnClickListener(this);
	}

	@SuppressLint("ShowToast")
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.crank_show_imageview:
		{
			Intent intent = new Intent(mContext, SafetyCrankMarkTagActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.tag_crankcall:
		{
			Intent intent1 = new Intent(mContext, SafetyCrankMarkCallActivity.class);
			startActivity(intent1);
			break;
		}
		case R.id.tag_crankad:
		{
			Intent intent2 = new Intent(mContext, SafetyCrankMarkAdActivity.class);
			startActivity(intent2);
			break;
		}
		case R.id.tag_crankhouse:
		{
			Intent intent3 = new Intent(mContext, SafetyCrankMarkHouseActivity.class);
			startActivity(intent3);
			break;
		}
		case R.id.tag_crankeat:
		{
			Intent intent4 = new Intent(mContext, SafetyCrankMarkEatActivity.class);
			startActivity(intent4);
			break;
		}
		case R.id.tag_cranke_add:
		{
			if (mAddText.getText().equals("添加新标记")) 
			{
				getwhereto();
				mAddImg.setVisibility(ImageView.GONE);
			} 
			else 
			{
				Toast.makeText(mContext, "没实现", 0).show();
			}
			break;
		}
		default:
			break;
		}
	}
	
	void getwhereto() 
	{
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		window.setContentView(R.layout.view_safety_crank_tabhost_mark_add);
		Button smsadd = (Button) window.findViewById(R.id.tag_addno);
		Button calladd = (Button) window.findViewById(R.id.tag_addoff);
		final EditText edittext = (EditText) window.findViewById(R.id.tag_editshow);
		smsadd.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if (!edittext.getText().equals("")) 
				{
					mAddText.setText(edittext.getText());
				}
				dialog.dismiss();
			}
		});
		calladd.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dialog.dismiss();
			}
		});
	}
}
