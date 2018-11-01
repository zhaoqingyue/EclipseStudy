package com.zhaoqy.app.demo.guide;

import java.util.Timer;
import java.util.TimerTask;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.about.AboutActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class GuideShowTimeActivity extends Activity implements OnClickListener
{
	private int       mDelay = 2000; 
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_showtime);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.guide_item3));
		Timer timer = new Timer();
	    timer.schedule(task, mDelay);
	}
	
	private TimerTask task = new TimerTask()
    {
		@Override
		public void run() 
		{
			finish();
			Intent mainIntent = new Intent(mContext, AboutActivity.class);
			startActivity(mainIntent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
    };

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
	
	@Override
	protected void onDestroy() 
	{
		task.cancel();
		super.onDestroy();
	}
}
