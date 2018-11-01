package com.zhaoqy.app.demo.progress.selector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class SectorProgressActivity extends Activity implements OnClickListener
{
	private ImageView      mBack;
	private TextView       mTitle;
	private SectorProgress mProgress;
	private SeekBar        mPercent;
	private SeekBar        mAngle;
	private TextView       mTip;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_progress_selector);
		
		initView();
		setListener();
		initData();
		showTip();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mTip = (TextView) findViewById(R.id.tvTip);
		mProgress = (SectorProgress) findViewById(R.id.spv);
        mPercent = (SeekBar) findViewById(R.id.sbPercent);
        mAngle = (SeekBar) findViewById(R.id.sbStartAngle);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mPercent.setOnSeekBarChangeListener(onSeekBarChangeListener);
		mAngle.setOnSeekBarChangeListener(onSeekBarChangeListener);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.progress_item5);
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
	
	private void showTip() 
	{
		mTip.setText("percent: " + mPercent.getProgress() + ";  startAngle: " + mAngle.getProgress());
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() 
    {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
        {
            switch (seekBar.getId()) 
            {
			case R.id.sbPercent:
				mProgress.setPercent(progress);
				break;
			case R.id.sbStartAngle:
				mProgress.setStartAngle(progress);
				break;
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
}
