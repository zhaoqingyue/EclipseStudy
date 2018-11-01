package com.zhaoqy.app.demo.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.animation.CircleLayout.OnItemClickListener;
import com.zhaoqy.app.demo.animation.CircleLayout.OnItemSelectedListener;

public class AnimationCircleMenuActivity extends Activity implements OnClickListener, OnItemSelectedListener, OnItemClickListener
{
	private ImageView    mBack;
	private TextView     mTitle;
	private TextView     mSelected;
	private CircleLayout mCircleMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_circleimage);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mCircleMenu = (CircleLayout)findViewById(R.id.id_animation_circle_layout);
		mSelected = (TextView)findViewById(R.id.id_animation_circle_layout_selected);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mCircleMenu.setOnItemSelectedListener(this);
		mCircleMenu.setOnItemClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.animation_custom_item1));
		mSelected.setText(((CircleImageView)mCircleMenu.getSelectedItem()).getName());
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
	public void onItemSelected(View view, int position, long id, String name) 
	{		
		mSelected.setText(name);
	}

	@Override
	public void onItemClick(View view, int position, long id, String name) 
	{
		Toast.makeText(getApplicationContext(), getResources().getString(R.string.animation_custom_circle_start_app) + " " + name, Toast.LENGTH_SHORT).show();
	}
}
