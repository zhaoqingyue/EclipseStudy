package com.zhaoqy.app.demo.menu.translate;


import com.zhaoqy.app.demo.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuTranslateActivity extends Activity implements OnClickListener, TranslateMenuCallback
{
	private TranslateMenuView rbmView;
	private ImageView  mBack;
	private TextView   mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_translate);
		
		initView();
		setListener();
		initData();
	}
	
	@SuppressLint("NewApi")
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		
		rbmView = (TranslateMenuView) findViewById(R.id.ribbonMenuView1);
		rbmView.setMenuClickCallback(this);
		rbmView.setMenuItems(R.menu.ribbon_menu);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.menu_item3));
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			rbmView.toggleMenu();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void TranslateMenuItemClick(int itemId) 
	{
	}
}
