/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MainActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo
 * @Description: 主页
 * @author: zhaoqy
 * @date: 2015-11-13 下午4:47:08
 * @version: V1.0
 */

package com.zhaoqy.app.demo;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.about.AboutActivity;
import com.zhaoqy.app.demo.animation.AnimationActivity;
import com.zhaoqy.app.demo.camera.CameraActivity;
import com.zhaoqy.app.demo.camera.gallery.util.ImageLoaderHelper;
import com.zhaoqy.app.demo.dialog.DialogActivity;
import com.zhaoqy.app.demo.guide.GuideActivity;
import com.zhaoqy.app.demo.index.IndexActivity;
import com.zhaoqy.app.demo.listview.ListViewActivity;
import com.zhaoqy.app.demo.login.LoginActivity;
import com.zhaoqy.app.demo.menu.MenuActivity;
import com.zhaoqy.app.demo.notebook.NoteBookActivity;
import com.zhaoqy.app.demo.notification.NotificationActivity;
import com.zhaoqy.app.demo.oschina.activity.OSChinaStartActivity;
import com.zhaoqy.app.demo.page.PageSlideActivity;
import com.zhaoqy.app.demo.picture.PictureActivity;
import com.zhaoqy.app.demo.progress.ProgressActivity;
import com.zhaoqy.app.demo.textview.TextViewActivity;
import com.zhaoqy.app.demo.weixin.ui.activity.WeiXinActivity;
import com.zhaoqy.app.demo.wifi.WiFiActivity;

public class MainActivity extends Activity implements OnClickListener 
{
	private Context  mContext;
	private TextView mItem0;
	private TextView mItem1;
	private TextView mItem2;
	private TextView mItem3;
	private TextView mItem4;
	private TextView mItem5;
	private TextView mItem6;
	private TextView mItem7;
	private TextView mItem8;
	private TextView mItem9;
	private TextView mItem10;
	private TextView mItem11;
	private TextView mItem12;
	private TextView mItem13;
	private TextView mItem14;
	private TextView mItem15;
	private TextView mItem16;
	private TextView mItem17;
	
	private Button   mExit;
	private View     mPopView;
	private TextView mPopCancle;
	private TextView mPopExit;
	private TextView mPopChange;
	private int      mBackCount = 0;
	private PopupWindow mPopupWindow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mItem0 = (TextView) findViewById(R.id.id_main_item0);
		mItem1 = (TextView) findViewById(R.id.id_main_item1);
		mItem2 = (TextView) findViewById(R.id.id_main_item2);
		mItem3 = (TextView) findViewById(R.id.id_main_item3);
		mItem4 = (TextView) findViewById(R.id.id_main_item4);
		mItem5 = (TextView) findViewById(R.id.id_main_item5);
		mItem6 = (TextView) findViewById(R.id.id_main_item6);
		mItem7 = (TextView) findViewById(R.id.id_main_item7);
		mItem8 = (TextView) findViewById(R.id.id_main_item8);
		mItem9 = (TextView) findViewById(R.id.id_main_item9);
		mItem10 = (TextView) findViewById(R.id.id_main_item10);
		mItem11 = (TextView) findViewById(R.id.id_main_item11);
		mItem12 = (TextView) findViewById(R.id.id_main_item12);
		mItem13 = (TextView) findViewById(R.id.id_main_item13);
		mItem14 = (TextView) findViewById(R.id.id_main_item14);
		mItem15 = (TextView) findViewById(R.id.id_main_item15);
		mItem16 = (TextView) findViewById(R.id.id_main_item16);
		mItem17 = (TextView) findViewById(R.id.id_main_item17);
		
		mExit = (Button) findViewById(R.id.id_mian_exit);
		mPopView = LayoutInflater.from(mContext).inflate(R.layout.view_pop_exit, null);
		mPopCancle = (TextView) mPopView.findViewById(R.id.id_pop_app_cancle);
		mPopChange = (TextView) mPopView.findViewById(R.id.id_pop_change_user);
		mPopExit = (TextView) mPopView.findViewById(R.id.id_pop_app_exit);
	}
	
	private void setListener() 
	{
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
		mItem15.setOnClickListener(this);
		mItem16.setOnClickListener(this);
		mItem17.setOnClickListener(this);
		
		mExit.setOnClickListener(this);
		mPopChange.setOnClickListener(this);
		mPopExit.setOnClickListener(this);
		mPopCancle.setOnClickListener(this);
	}
	
	private void initData() 
	{
		mPopupWindow = new PopupWindow(mPopView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		ImageLoaderHelper.initImageLoader(mContext);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_main_item0:
		{
			Intent intent = new Intent(mContext, LoginActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item1:
		{
			Intent intent = new Intent(mContext, WeiXinActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item2:
		{
			Intent intent = new Intent(mContext, OSChinaStartActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item3:
		{
			Intent intent = new Intent(mContext, GuideActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item4:
		{
			Intent intent = new Intent(mContext, PageSlideActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item5:
		{
			Intent intent = new Intent(mContext, MenuActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item6:
		{
			Intent intent = new Intent(mContext, AnimationActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item7:
		{
			Intent intent = new Intent(mContext, NotificationActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item8:
		{
			Intent intent = new Intent(mContext, DialogActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item9:
		{
			Intent intent = new Intent(mContext, ListViewActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item10:
		{
			Intent intent = new Intent(mContext, WiFiActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item11:
		{
			Intent intent = new Intent(mContext, CameraActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item12:
		{
			Intent intent = new Intent(mContext, PictureActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item13:
		{
			Intent intent = new Intent(mContext, NoteBookActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item14:
		{
			Intent intent = new Intent(mContext, ProgressActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item15:
		{
			Intent intent = new Intent(mContext, IndexActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item16:
		{
			Intent intent = new Intent(mContext, AboutActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_main_item17:
		{
			Intent intent = new Intent(mContext, TextViewActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_mian_exit:
		{
			finish();
			break;
		}
		case R.id.id_pop_change_user:
		{
			mPopupWindow.dismiss();
			Intent intent = new Intent(mContext, LoginActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_pop_app_exit:
		{
			mPopupWindow.dismiss();
			finish();
			break;
		}
		case R.id.id_pop_app_cancle:
		{
			mPopupWindow.dismiss();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		switch (keyCode) 
		{
		case KeyEvent.KEYCODE_MENU:
		{
			mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0000000")));
			mPopupWindow.showAtLocation(mExit, Gravity.BOTTOM, 0, 0);
			mPopupWindow.setAnimationStyle(R.style.app_pop);
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setFocusable(true);
			mPopupWindow.update();
			break;
		}
		case KeyEvent.KEYCODE_BACK:
		{
			if (mBackCount == 0)
			{
				mBackCount++;
				Toast.makeText(mContext, "再次按返回键退出", Toast.LENGTH_SHORT).show();
				Timer timer = new Timer();
				timer.schedule(new TimerTask() 
				{
					@Override
					public void run() 
					{
						mBackCount = 0;
					}
				}, 3000);
			}
			else
			{
				finish();
			}
			return true;
		}
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		ImageLoaderHelper.unInitImageLoader();
	}
}
