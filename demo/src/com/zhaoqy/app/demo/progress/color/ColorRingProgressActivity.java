package com.zhaoqy.app.demo.progress.color;

import com.zhaoqy.app.demo.R;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class ColorRingProgressActivity extends Activity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mTitle;
	private SeekBar   mPercent;
	private SeekBar   mAngle;
	private SeekBar   mWidth;
	private TextView  mTip;
	private TextView  mPercentValue;
	private ColorRingProgress mProgress;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_progress_color);
		
		initView();
		setListener();
		initData();
		showTip();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		
		mProgress = (ColorRingProgress) findViewById(R.id.crpv);
		mTip = (TextView) findViewById(R.id.tvTip);
        mPercent = (SeekBar) findViewById(R.id.sbPercent);
        mAngle = (SeekBar) findViewById(R.id.sbStartAngle);
        mWidth = (SeekBar) findViewById(R.id.sbStrokeWidth);
        mPercentValue = (TextView) findViewById(R.id.tvPercent);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mProgress.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.progress_item6);
		mPercent.setOnSeekBarChangeListener(onSeekBarChangeListener);
		mAngle.setOnSeekBarChangeListener(onSeekBarChangeListener);
		mWidth.setOnSeekBarChangeListener(onSeekBarChangeListener);
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
		case R.id.crpv:
		{
			ObjectAnimator anim = ObjectAnimator.ofFloat(v, "percent", 0, ((ColorRingProgress) v).getPercent());
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(1000);
            anim.start();
			break;
		}
		default:
			break;
		}
	}
	
	private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() 
	{
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
		{
			if (seekBar.getId() == R.id.sbPercent) 
			{
				mProgress.setPercent(progress);
				mPercentValue.setText(progress + "");
			} 
			else if (seekBar.getId() == R.id.sbStartAngle) 
			{
				mProgress.setStartAngle(progress);
			}
			else if (seekBar.getId() == R.id.sbStrokeWidth) 
			{
				mProgress.setStrokeWidthDp(progress);
			}
			showTip();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) 
		{
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) 
		{
		}
	};

	private void showTip() 
	{
		mTip.setText("percent: " + mPercent.getProgress() + ";  startAngle: " + mAngle.getProgress() +  ";  strokeWidth: " + mWidth.getProgress() + "dp");
	}
}
