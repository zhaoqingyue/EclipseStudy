package com.zhaoqy.app.demo.progress.number;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class NumberProgressActivity extends Activity implements OnClickListener, OnProgressBarListener 
{
	private ImageView         mBack;
	private TextView          mTitle;
	private Timer             mTimer;
	private NumberProgressBar mNumber;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progress_number);

		initView();
		setListener();
		initData();
		startTimer();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mNumber = (NumberProgressBar) findViewById(R.id.id_progress_number_bar1);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mNumber.setOnProgressBarListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.progress_item0);
	}
	
	private void startTimer()
	{
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() 
		{
			@Override
			public void run() 
			{
				runOnUiThread(new Runnable() 
				{
					@Override
					public void run() 
					{
						mNumber.incrementProgressBy(1);
					}
				});
			}
		}, 1000, 100);
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

	@Override
	public void onProgressChange(int current, int max) 
	{
		if (current == max) 
		{
			mNumber.setProgress(0);
		}
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		mTimer.cancel();
	}
}
