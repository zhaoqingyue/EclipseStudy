package com.zhaoqy.app.demo.menu.headline.view;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.headline.activity.MenuSettingsActivity;
import com.zhaoqy.app.demo.menu.headline.slidemenu.SlidingMenu;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DrawerView implements OnClickListener
{
	private final Activity mActivity;
	private SlidingMenu    mSlidingMenu;
	private SwitchButton   mMode;
	private TextView       mText;
	private RelativeLayout mSetting;
	
	public DrawerView(Activity activity) 
	{
		mActivity = activity;
	}

	public SlidingMenu initSlidingMenu() 
	{
		mSlidingMenu = new SlidingMenu(mActivity);
		mSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);
		mSlidingMenu.setShadowWidthRes(R.dimen.menu_headline_slidemenu_shadow_width);
		mSlidingMenu.setShadowDrawable(R.drawable.menu_headline_shadow_left);
		mSlidingMenu.setBehindOffsetRes(R.dimen.menu_headline_slidemenu_offset);
		mSlidingMenu.setFadeDegree(0.35F);
		mSlidingMenu.attachToActivity(mActivity, SlidingMenu.RIGHT);
		mSlidingMenu.setMenu(R.layout.view_menu_headline_slidemenu_left);
		mSlidingMenu.setSecondaryMenu(R.layout.view_menu_headline_slidemenu_right);
		mSlidingMenu.setSecondaryShadowDrawable(R.drawable.menu_headline_shadow_right);
		mSlidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() 
		{
			public void onOpened() 
			{
			}
		});
		initView();
		return mSlidingMenu;
	}

	private void initView() 
	{
		mMode = (SwitchButton) mSlidingMenu.findViewById(R.id.id_slidemenu_left_item_night_mode);
		mText = (TextView) mSlidingMenu.findViewById(R.id.id_slidemenu_left_item_night_mode_name);
		mMode.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(isChecked)
				{
					mText.setText(mActivity.getResources().getString(R.string.menu_headline_setting_night_mode));
				}
				else
				{
					mText.setText(mActivity.getResources().getString(R.string.menu_headline_setting_day_mode));
				}
			}
		});
		mMode.setChecked(false);
		if(mMode.isChecked())
		{
			mText.setText(mActivity.getResources().getString(R.string.menu_headline_setting_night_mode));
		}
		else
		{
			mText.setText(mActivity.getResources().getString(R.string.menu_headline_setting_day_mode));
		}
		
		mSetting = (RelativeLayout) mSlidingMenu.findViewById(R.id.id_slidemenu_left_item_setting);
		mSetting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_slidemenu_left_item_setting:
		{
			mActivity.startActivity(new Intent(mActivity, MenuSettingsActivity.class));
			mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		}
		default:
			break;
		}
	}
}
