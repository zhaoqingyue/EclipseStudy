package com.zhaoqy.app.demo.progress.smooth.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.progress.smooth.smooth.SmoothProgressBar;
import com.zhaoqy.app.demo.progress.smooth.smooth.SmoothProgressBarUtil;
import com.zhaoqy.app.demo.progress.smooth.smooth.SmoothProgressDrawable;

public class SmoothProgressActivity extends Activity implements OnClickListener
{
	private Context           mContext;
	private ImageView         mBack;
	private TextView          mTitle;
	private ProgressBar       mProgressBar1;
	private SmoothProgressBar mPocketBar;
	private Button            mMake;
	private Button            mStart;
	private Button            mFinish;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_smooth);
        mContext = this;
        
        initView();
        setListener();
        initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mProgressBar1 = (ProgressBar) findViewById(R.id.progressbar2);
		mPocketBar = (SmoothProgressBar) findViewById(R.id.pocket);
		mMake = (Button) findViewById(R.id.button_make);
		mStart = (Button) findViewById(R.id.start);
		mFinish = (Button) findViewById(R.id.finish);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mMake.setOnClickListener(this);
		mStart.setOnClickListener(this);
		mFinish.setOnClickListener(this);
	}
	
	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.progress_item2);
		mProgressBar1.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(mContext).
				interpolator(new AccelerateInterpolator()).build());
		mPocketBar.setSmoothProgressDrawableBackgroundDrawable(SmoothProgressBarUtil.generateDrawableWithColors(getResources().
				getIntArray(R.array.pocket_background_colors),((SmoothProgressDrawable) mPocketBar.getIndeterminateDrawable()).getStrokeWidth()));
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
		case R.id.button_make:
		{
			Intent intent = new Intent(mContext, SmoothProgressMakeActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.start:
		{
			mPocketBar.progressiveStart();
			break;
		}
		case R.id.finish:
		{
			mPocketBar.progressiveStop();
			break;
		}
		default:
			break;
		}
	}
}
