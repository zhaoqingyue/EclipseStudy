package com.zhaoqy.app.demo.picture.round;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class RoundImageActivity extends Activity implements OnClickListener
{
	private ImageView   mBack;
	private TextView    mTitle;
	private ImageView   mImageView;
	private Button      mPaly;
	private Button      mStop;
	private MediaPlayer mMediaPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_round);
	
		initView();
		setListener();
		initData();
	}
	
	private void initView()
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mImageView = (ImageView) findViewById(R.id.id_circle_image);
		mPaly = (Button) findViewById(R.id.id_circle_play);
		mStop = (Button) findViewById(R.id.id_circle_stop);
	}
	
	private void setListener()
	{
		mBack.setOnClickListener(this);
		mPaly.setOnClickListener(this);
		mStop.setOnClickListener(this);
	}
	
	private void initData()
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.picture_item2);
	}
	
	private void playMusic() 
	{
		//mMediaPlayer = MediaPlayer.create(this, R.raw.quiet);
		mMediaPlayer = MediaPlayer.create(this, R.raw.beep);
		//mMediaPlayer = MediaPlayer.create(this, R.raw.angel);
		mMediaPlayer.start();

		Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.circle);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		if (operatingAnim != null) 
		{
			mImageView.startAnimation(operatingAnim);
		}

		mMediaPlayer.setOnCompletionListener(new OnCompletionListener() 
		{
			@Override
			public void onCompletion(MediaPlayer mp) 
			{
				mp.stop();
				mImageView.clearAnimation();
			}
		});
	}

	private void stopMusic() 
	{
		mMediaPlayer.stop();
		mImageView.clearAnimation();
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
		case R.id.id_circle_play:
		{
			playMusic();
			break;
		}
		case R.id.id_circle_stop:
		{
			stopMusic();
			break;
		}
		default:
			break;
		}
	}
}
