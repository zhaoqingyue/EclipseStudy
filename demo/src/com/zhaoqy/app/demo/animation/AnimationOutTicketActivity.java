package com.zhaoqy.app.demo.animation;

import com.zhaoqy.app.demo.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AnimationOutTicketActivity extends Activity implements OnClickListener
{
	private ImageView      mBack;
	private TextView       mTitle;
	private RelativeLayout mPayInfo;
	private Animation      mAnimation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation_out_ticket);
		
		initView();
		setListener();
		initData();
		newThread();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mPayInfo = (RelativeLayout) findViewById(R.id.rl_payInfo);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.animation_out_ticket));
	}
	
	private void newThread() 
	{
		new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				try 
				{
					Thread.sleep(1500);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	/**
	 * 启动打印小票动画
	 */
	private void startAnimation() 
	{
		mAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down_in);
		mAnimation.setDuration(1000);
		mAnimation.setFillAfter(true);
		mPayInfo.startAnimation(mAnimation);
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() 
	{
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what) 
			{
			case 1:
				startAnimation();
				break;
			default:
				break;
			}
		};
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
}
