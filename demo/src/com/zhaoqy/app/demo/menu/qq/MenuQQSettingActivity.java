package com.zhaoqy.app.demo.menu.qq;

import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuQQSettingActivity extends Activity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_qq_setting);

		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mTextView = (TextView) findViewById(R.id.id_menu_qq_setting_textview);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		Intent intent = getIntent();
		String text = intent.getStringExtra("text");
		mTextView.setText(text);
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(text);
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
		default:
			break;
		}
	}
}
