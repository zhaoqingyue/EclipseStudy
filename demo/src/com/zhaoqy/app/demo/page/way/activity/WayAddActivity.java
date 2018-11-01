package com.zhaoqy.app.demo.page.way.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.zhaoqy.app.demo.R;

public class WayAddActivity extends Activity implements OnClickListener
{
	private Button mItem0;
	private Button mItem1;
	private Button mItem2;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_way_add);

		initViews();
		setListener();
	}

	private void initViews() 
	{
		mItem0 = (Button) findViewById(R.id.id_way_add_picture);
		mItem1 = (Button) findViewById(R.id.id_way_add_photo);
		mItem2 = (Button) findViewById(R.id.id_way_add_text);
	}
	
	private void setListener() 
	{
		mItem0.setOnClickListener(this);
		mItem1.setOnClickListener(this);
		mItem2.setOnClickListener(this);
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		return true;
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_way_add_picture:
		{
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, 1002);
			break;
		}
		case R.id.id_way_add_photo:
		{
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			startActivityForResult(intent, 1001);
			break;
		}
		case R.id.id_way_add_text:
		{
			break;
		}	
		default:
			break;
		}
	}
}
