package com.zhaoqy.app.demo.animation;

import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimationSoundWaveActivity extends Activity implements OnClickListener
{
	private ImageView    mBack;
	private TextView     mTitle;
	private WaveAnimView mWave1;
	private WaveAnimView mWave2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation_sound_wave);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mWave1 = (WaveAnimView) findViewById(R.id.search_animation_wave);
		mWave2 = (WaveAnimView) findViewById(R.id.search_animation_wave2);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.animation_custom_item3));
		mWave1.startAnimation(true);
		mWave2.startAnimation(false);
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
