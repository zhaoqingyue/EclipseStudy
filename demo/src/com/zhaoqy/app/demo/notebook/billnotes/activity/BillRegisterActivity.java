package com.zhaoqy.app.demo.notebook.billnotes.activity;

import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class BillRegisterActivity extends Activity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billnotes_register);
		
		initView();
		initData();
		setListener();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
	}
	
	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("注册");
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
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
