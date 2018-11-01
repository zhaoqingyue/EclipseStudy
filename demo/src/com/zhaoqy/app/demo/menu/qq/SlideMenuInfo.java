package com.zhaoqy.app.demo.menu.qq;

import com.zhaoqy.app.demo.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideMenuInfo extends LinearLayout
{
	private ImageView mIcon;
	private TextView  mUserName;
	private TextView  mLever;

	public SlideMenuInfo(Context context) 
	{
		super(context);
		initViews(context);
	}

	public SlideMenuInfo(Context context, int icon, String title, String lever) 
	{
		super(context);
		initViews(context);
		mIcon.setImageResource(icon);
		mUserName.setText(title);
		mLever.setText(lever);
	}

	private void initViews(Context context) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_slide_menu_info, this);
		mIcon = (ImageView) findViewById(R.id.id_slide_menu_info_icon);
		mUserName = (TextView) findViewById(R.id.id_slide_menu_info_username);
		mLever = (TextView) findViewById(R.id.id_slide_menu_info_lever);
	}

	public void setIcon(int icon) 
	{
		mIcon.setImageResource(icon);
	}

	public void setTitle(String title) 
	{
		mUserName.setText(title);
	}

	public void setDengJi(String lever) 
	{
		mLever.setText(lever);
	}
}
