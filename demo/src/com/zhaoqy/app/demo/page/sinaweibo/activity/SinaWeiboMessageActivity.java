package com.zhaoqy.app.demo.page.sinaweibo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.zhaoqy.app.demo.R;

public class SinaWeiboMessageActivity extends Activity implements OnClickListener
{
	private Context mContext;
	private View    mView0;
	private View    mView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_sinaweibo_message);
		mContext = this;
		
		initView();
		setListener();
	}
	private void setListener() 
	{
		mView0.setOnClickListener(this);
		mView1.setOnClickListener(this);
		
	}
	private void initView() 
	{
		mView0 = findViewById(R.id.id_sinaweibo_message_at);
		mView1 = findViewById(R.id.id_sinaweibo_message_comment);
	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_sinaweibo_message_at:
		{
			Intent intent =	new Intent(mContext, SinaWeiboMessageDetailActivity.class);
			intent.putExtra("what", SinaWeiboMessageDetailActivity.AT_ME);
			startActivity(intent);
			break;
		}
		case R.id.id_sinaweibo_message_comment:
		{
			Intent intent =	new Intent(mContext, SinaWeiboMessageDetailActivity.class);
			intent.putExtra("what", SinaWeiboMessageDetailActivity.COMMENTS_ME);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
