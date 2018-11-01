package com.zhaoqy.app.demo.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class AnimationtaobaoMenu2Activity extends Activity implements OnClickListener
{
	private ImageView      mBack;
	private TextView       mTitle;
	private ComposerLayout mLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation_taobao_menu2);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mLayout = (ComposerLayout) findViewById(R.id.id_animation_taobao_menu2_layout);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mLayout.setButtonsOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.animation_taobao_menu2));
		mLayout.init(new int[] { 
				R.drawable.animation_taobao_menu_composer_camera,
				R.drawable.animation_taobao_menu_composer_music, 
				R.drawable.animation_taobao_menu_composer_place,
				R.drawable.animation_taobao_menu_composer_sleep, 
				R.drawable.animation_taobao_menu_composer_thought,
				R.drawable.animation_taobao_menu_composer_with }, 
				R.drawable.animation_taobao_menu_composer_button,
				R.drawable.animation_taobao_menu_composer_icn_plus, 
				ComposerLayout.RIGHTCENTER, 180, 300);
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
