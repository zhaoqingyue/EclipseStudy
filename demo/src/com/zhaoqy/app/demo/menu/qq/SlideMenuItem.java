package com.zhaoqy.app.demo.menu.qq;

import com.zhaoqy.app.demo.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideMenuItem extends LinearLayout
{
	private ImageView mIcon;
	private TextView  mTitle;

	public SlideMenuItem(Context context) 
	{
		super(context);
		initViews(context);
	}

	public SlideMenuItem(Context context, int icon, int title) 
	{
		super(context);
		initViews(context);
		mIcon.setImageResource(icon);
		mTitle.setText(title);
	}

	public SlideMenuItem(Context context, int icon, String title) 
	{
		super(context);
		initViews(context);
		mIcon.setImageResource(icon);
		mTitle.setText(title);
	}

	private void initViews(Context context) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_slide_menu_item, this);
		mIcon = (ImageView) findViewById(R.id.id_slidemenu_item_icon);
		mTitle = (TextView) findViewById(R.id.id_slidemenu_item_title);
	}

	public void setIcon(int icon) 
	{
		mIcon.setImageResource(icon);
	}

	public void setTitle(int title) 
	{
		mTitle.setText(title);
	}

	public void setTitle(String title) 
	{
		mTitle.setText(title);
	}
}
