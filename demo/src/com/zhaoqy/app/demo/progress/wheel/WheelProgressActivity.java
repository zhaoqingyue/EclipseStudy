package com.zhaoqy.app.demo.progress.wheel;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class WheelProgressActivity extends Activity implements OnClickListener
{
	private ProgressWheel mSpinWheel1;
	private ProgressWheel mSpinWheel2;
	private ProgressWheel mIncrementWheel;
	private ProgressBar   mColorBar = null;
	private ProgressBar   mLeftBar = null;
	private ProgressBar   mRightBar = null;
	private ImageView     mBack;
	private TextView      mTitle;
	private Button        mSpin1;
	private Button        mIncrement;
	private Button        mColor;
	private Button        mLeft;
	private Button        mRight;
	private int           mProgress = 0;
	private int           mColorCount = 0;
    private int           mLeftCount = 0;
    private int           mRightCount = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_wheel);
        
        initView();
        setListener();
        initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mSpin1 = (Button) findViewById(R.id.id_progress_spin_btn1);
		mIncrement = (Button) findViewById(R.id.id_progress_increment_btn);
		mSpinWheel1 = (ProgressWheel) findViewById(R.id.id_progress_spin_wheel1);
		mSpinWheel2 = (ProgressWheel) findViewById(R.id.id_progress_spin_wheel2);
		mIncrementWheel = (ProgressWheel) findViewById(R.id.id_progress_increment_wheel);
		mColorBar = (ProgressBar) findViewById(R.id.id_progress_custom_color_progress);
		mColor = (Button) findViewById(R.id.id_progress_custom_color_btn);
		mLeftBar = (ProgressBar) findViewById(R.id.id_progress_left_to_right_progress);
	    mLeft = (Button) findViewById(R.id.id_progress_left_to_right_btn);
		mRightBar = (ProgressBar) findViewById(R.id.id_progress_right_to_left_progress);
		mRight = (Button) findViewById(R.id.id_progress_right_to_left_btn);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mSpin1.setOnClickListener(this);
		mIncrement.setOnClickListener(this);
		mColor.setOnClickListener(this);
		mLeft.setOnClickListener(this);
		mRight.setOnClickListener(this);
	}
	
	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.progress_item4);
		
	    int[] pixels = new int[] { 0xFF2E9121, 0xFF2E9121, 0xFF2E9121, 0xFF2E9121, 0xFF2E9121, 0xFF2E9121, 0xFFFFFFFF, 0xFFFFFFFF};
	    Bitmap bm = Bitmap.createBitmap(pixels, 8, 1, Bitmap.Config.ARGB_8888);
	    Shader shader = new BitmapShader(bm, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
	    mSpinWheel2.setRimShader(shader);
	    mSpinWheel2.spin();
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
		case R.id.id_progress_spin_btn1:
		{
			if(mSpinWheel1.isSpinning) 
			{
				mSpinWheel1.stopSpinning();
			}
			mSpinWheel1.resetCount();
			mSpinWheel1.setText("Loading...");
			mSpinWheel1.spin();
			break;
		}
		case R.id.id_progress_increment_btn:
		{
			mProgress = 0;
			mIncrementWheel.resetCount();
			Thread s = new Thread(r);
			s.start();
			break;
		}
		case R.id.id_progress_custom_color_btn:
		{
			mColorCount = 0;
			mColorBar.setMax(100);
			mColorBar.setProgress(0);
			mColorBar.setIndeterminate(false);
			new Thread() 
			{
				public void run() 
				{
					try 
					{
						while (mColorCount <= 100) 
						{
							mColorBar.setProgress(mColorCount++);
							Thread.sleep(100);
						}
						if (mColorCount > 100) 
						{
							mColorCount = 0;
							mColorBar.setProgress(mColorCount);
						}
					} 
					catch (Exception ex) 
					{
					}
				}
			}.start();
			break;
		}
		case R.id.id_progress_left_to_right_btn:
		{
			mLeftCount = 0;
	        mLeftBar.setMax(100);
	        mLeftBar.setProgress(0);
	        mLeftBar.setIndeterminate(false);
	        new Thread()
	        {
	            public void run()
	            {
	                try
	                {
	                    while (mLeftCount <= 100)
	                    {
	                        mLeftBar.setProgress(mLeftCount++);
	                        Thread.sleep(100);
	                    }
	                    if (mLeftCount > 100)
	                    {
	                    	mLeftCount = 0;
	                    	mLeftBar.setProgress(mLeftCount);
	                    }
	                }
	                catch (Exception ex)
	                {
	                }
	            }
	        }.start();
			break;
		}
		case R.id.id_progress_right_to_left_btn:
		{
			mRightCount = 100;

	        mRightBar.setMax(100);
	        mRightBar.setProgress(0);
	        mRightBar.setIndeterminate(false);
	        new Thread()
	        {
	            public void run()
	            {
	                try
	                {
	                    while (mRightCount >= 0)
	                    {
	                        mRightBar.setProgress(mRightCount--);
	                        Thread.sleep(100);
	                    }
	                    if (mRightCount < 0)
	                    {
	                    	mRightCount = 100;
	                    	mRightBar.setProgress(mRightCount);
	                    }
	                }
	                catch (Exception ex)
	                {
	                }
	            }
	        }.start();
			break;
		}
		default:
			break;
		}
	}
	
	 final Runnable r = new Runnable() 
	 {
		public void run() 
		{
			while(mProgress < 361) 
			{
				mIncrementWheel.incrementProgress();
				mProgress++;
				try 
				{
					Thread.sleep(15);
				} 
				catch (InterruptedException e) 
				{	
					e.printStackTrace();
				}
			}
		}
     };
	
	@Override
	public void onPause() 
	{
		super.onPause();
		mProgress = 361;
		mSpinWheel1.stopSpinning();
		mSpinWheel1.resetCount();
		mIncrementWheel.stopSpinning();
		mIncrementWheel.resetCount();
		mIncrementWheel.setText("0%");
	}
}
