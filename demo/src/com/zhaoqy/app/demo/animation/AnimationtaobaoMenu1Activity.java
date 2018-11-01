package com.zhaoqy.app.demo.animation;

import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AnimationtaobaoMenu1Activity extends Activity implements OnClickListener
{
	private Context mContext;
	private ImageView  mBack;
	private TextView   mTitle;
	
	private boolean areButtonsShowing;
	private RelativeLayout composerButtonsWrapper;
	private ImageView composerButtonsShowHideButtonIcon;
	private RelativeLayout composerButtonsShowHideButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation_taobao_menu1);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		
		MyAnimations.initOffset(mContext);
		composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
		composerButtonsShowHideButton = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button);
		composerButtonsShowHideButtonIcon = (ImageView) findViewById(R.id.composer_buttons_show_hide_button_icon);

		composerButtonsShowHideButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if (!areButtonsShowing) 
				{
					MyAnimations.startAnimationsIn(composerButtonsWrapper, 300);
					composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(0, -270, 300));
				} 
				else 
				{
					MyAnimations.startAnimationsOut(composerButtonsWrapper, 300);
					composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(-270, 0, 300));
				}
				areButtonsShowing = !areButtonsShowing;
			}
		});
		for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) 
		{
			composerButtonsWrapper.getChildAt(i).setOnClickListener(new View.OnClickListener() 
			{
				@Override
				public void onClick(View arg0) 
				{
				}
			});
		}
		composerButtonsShowHideButton.startAnimation(MyAnimations.getRotateAnimation(0, 360, 200));
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.animation_taobao_menu1));
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
}
