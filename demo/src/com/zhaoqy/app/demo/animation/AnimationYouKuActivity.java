package com.zhaoqy.app.demo.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;

public class AnimationYouKuActivity extends Activity implements OnClickListener
{
	private ImageView      mBack;
	private TextView       mTitle;
	private RelativeLayout mLevel1;
	private RelativeLayout mLevel2;
	private RelativeLayout mLevel3;
	private Button         mShow;
	private TextView       mText;
	private boolean isDisplayMenu = false;		// 菜单的显示状态, 默认为不显示
    private boolean isDisplayLevel2 = false;	// 二级菜单的显示状态, 默认为不显示
    private boolean isDisplayLevel3 = false;	// 三级菜单的显示状态, 默认为不显示
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation_youku);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mLevel1 = (RelativeLayout) findViewById(R.id.rl_level1);
		mLevel2 = (RelativeLayout) findViewById(R.id.rl_level2);
		mLevel3 = (RelativeLayout) findViewById(R.id.rl_level3);
		mShow = (Button) findViewById(R.id.bt_show);
		mText = (TextView) findViewById(R.id.tv_text);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		findViewById(R.id.ib_home).setOnClickListener(this);
		findViewById(R.id.ib_menu).setOnClickListener(this);
		mShow.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.animation_youku));
		mLevel1.setVisibility(View.GONE);
		mLevel2.setVisibility(View.GONE);
		mLevel3.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) 
	{
		if(Utils.isRunningAnimation()) return;
		
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		case R.id.ib_home:
		{
			if(isDisplayLevel2) 
			{
				long startOffset = 0;
				if(isDisplayLevel3) 
				{
					Utils.startOutRotateAnimation(mLevel3, startOffset);
					isDisplayLevel3 = !isDisplayLevel3;
					startOffset = 200;
				}
				
				// 隐藏二级菜单
				Utils.startOutRotateAnimation(mLevel2, startOffset);
			} 
			else 
			{
				// 显示二级菜单
				Utils.startInRotateAnimation(mLevel2, 0);
			}
			isDisplayLevel2 = !isDisplayLevel2;
			break;
		}
		case R.id.ib_menu:
		{
			if(isDisplayLevel3) 
			{
				// 隐藏三级菜单
				Utils.startOutRotateAnimation(mLevel3, 0);
			} 
			else 
			{
				// 显示三级菜单
				Utils.startInRotateAnimation(mLevel3, 0);
			}
			isDisplayLevel3 = !isDisplayLevel3;
			break;
		}
		case R.id.bt_show:
		{
			if(showMeun())
			{
				mShow.setText("关闭优酷菜单");
			}
			else
			{
				mShow.setText("出现优酷菜单");
			}
			startAnimation();
			break;
		}
		default:
			break;
		}
	}
	
	/**
	 * 优酷菜单是否显示
	 * @return
	 */
	private boolean showMeun() 
	{
		if(Utils.isRunningAnimation()) return true;
		
		if(isDisplayMenu) {
			
			long startOffset = 0;
			// 隐藏菜单
			if(isDisplayLevel2) 
			{		// 二级菜单显示状态, 隐藏
				
				if(isDisplayLevel3) 
				{	// 三级菜单显示状态, 隐藏它
					Utils.startOutRotateAnimation(mLevel3, startOffset);
					startOffset = 200;
					isDisplayLevel3 = !isDisplayLevel3;
				}
				
				Utils.startOutRotateAnimation(mLevel2, startOffset);
				startOffset += 200;
				isDisplayLevel2 = !isDisplayLevel2;
			}
			
			// 隐藏一级菜单
			Utils.startOutRotateAnimation(mLevel1, startOffset);
		} else {		
			// 显示菜单
			// 判断菜单是否是为View.GONE的状态
			if(mLevel1.getVisibility() == View.GONE
					&& mLevel2.getVisibility() == View.GONE
					&& mLevel3.getVisibility() == View.GONE) {
				// 设置菜单显示
				mLevel1.setVisibility(View.VISIBLE);
				mLevel2.setVisibility(View.VISIBLE);
				mLevel3.setVisibility(View.VISIBLE);
			}
			
			Utils.startInRotateAnimation(mLevel1, 0);
			Utils.startInRotateAnimation(mLevel2, 200);
			Utils.startInRotateAnimation(mLevel3, 400);
			
			isDisplayLevel2 = !isDisplayLevel2;
			isDisplayLevel3 = !isDisplayLevel3;
		}
		
		return isDisplayMenu = !isDisplayMenu;
	}
	
	public void startAnimation()
	{
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.youku_text);
		mText.startAnimation(anim);
	}
}
