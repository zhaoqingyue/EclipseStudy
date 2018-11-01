package com.zhaoqy.app.demo.picture.kenburns.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.picture.kenburns.view.KenBurnsView;
import com.zhaoqy.app.demo.picture.kenburns.view.KenBurnsView.TransitionListener;
import com.zhaoqy.app.demo.picture.kenburns.view.Transition;

public class MultiImageActivity extends Activity implements OnClickListener, TransitionListener
{
	private static final int TRANSITIONS_TO_SWITCH = 3;
	private ImageView    mBack;
	private TextView     mTitle;
    private ViewSwitcher mViewSwitcher;
	private KenBurnsView mImg0;
	private KenBurnsView mImg1;
	private int          mTransitionsCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_kenburns_multiple);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView()
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
		mImg0 = (KenBurnsView) findViewById(R.id.id_picture_kenburns_img0);
		mImg1 = (KenBurnsView) findViewById(R.id.id_picture_kenburns_img1);
	}
	
	private void setListener()
	{
		mBack.setOnClickListener(this);
		mImg0.setTransitionListener(this);
		mImg1.setTransitionListener(this);
	}
	
	private void initData()
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.picture_kenburns_item1);
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		KenBurnsView currentImage = (KenBurnsView) mViewSwitcher.getCurrentView();
        currentImage.resume();
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
	public void onTransitionStart(Transition transition) 
	{
	}

	@Override
	public void onTransitionEnd(Transition transition) 
	{
		mTransitionsCount++;
        if (mTransitionsCount == TRANSITIONS_TO_SWITCH) 
        {
            mViewSwitcher.showNext();
            mTransitionsCount = 0;
        }
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		KenBurnsView currentImage = (KenBurnsView) mViewSwitcher.getCurrentView();
        currentImage.pause();
	}
}
