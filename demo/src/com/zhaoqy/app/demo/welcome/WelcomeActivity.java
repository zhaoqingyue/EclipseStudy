package com.zhaoqy.app.demo.welcome;

import com.zhaoqy.app.demo.MainActivity;
import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelcomeActivity extends Activity implements OnViewChangeListener, OnClickListener
{
    private LinearLayout mPoint;
    private Scroll       mScroll;
    private ImageView[]  mImages;
    private Button       mStart;
    private int          mCurrent;
    private int          mCount;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        setListener();
        initData();
    }

	private void initView() 
	{
		mScroll = (Scroll)findViewById(R.id.id_welcome_scroll);
    	mPoint = (LinearLayout)findViewById(R.id.id_welcome_point);
        mStart = (Button)findViewById(R.id.id_welcome_start);
	}
	
	private void setListener()
	{
		mStart.setOnClickListener(this);
        mScroll.setOnViewChangeListener(this);
	}
	
	private void initData()
	{
		mCurrent = 0;
		mCount = mScroll.getChildCount();
		mImages = new ImageView[mCount];
		for (int i=0; i<mCount; i++)
	    {
	        mImages[i] = (ImageView)mPoint.getChildAt(i);
	        mImages[i].setEnabled(false);
	        mImages[i].setTag(i);
	    }
	    mImages[mCurrent].setEnabled(true);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
        {
            case R.id.id_welcome_start:
            {
            	Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.zoom_out_enter, R.anim.zoom_out_exit);
                break;
            }
        }
	}

	@Override
	public void OnViewChange(int position) 
	{
		setcurrentPoint(position);
	}
	
	private void setcurrentPoint(int position)
    {
        if (position < 0 || position > mCount-1 || mCurrent == position)
        {
            return;
        }
        mImages[mCurrent].setEnabled(false);
        mImages[position].setEnabled(true);
        mCurrent = position;
    }
}
