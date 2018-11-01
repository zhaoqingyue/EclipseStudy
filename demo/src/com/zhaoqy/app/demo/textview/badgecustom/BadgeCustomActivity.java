package com.zhaoqy.app.demo.textview.badgecustom;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;

public class BadgeCustomActivity extends Activity implements OnClickListener
{
	private ImageView mBack;
	private TextView  mTitle;
	private Button    btnPosition;
	private Button    btnColour;
	private Button    btnAnim1;
	private Button    btnAnim2;
	private Button    btnCustom;
	private Button    btnClick;
	private Button    btnIncrement;
	private BadgeView badge1;
	private BadgeView badge2;
	private BadgeView badge3;
	private BadgeView badge4;
	private BadgeView badge5;
	private BadgeView badge6;
	private BadgeView badge7;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_textview_badge_custom);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		
		//default badge
		View target = findViewById(R.id.default_target);
		BadgeView badge = new BadgeView(this, target);
		badge.setText("1");
		badge.show();

		//set position
		btnPosition = (Button) findViewById(R.id.position_target);
		badge1 = new BadgeView(this, btnPosition);
		badge1.setText("12");
		badge1.setBadgePosition(BadgeView.POSITION_CENTER);
		btnPosition.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				badge1.toggle();
			}
		});

		// badge/text size & colour
		btnColour = (Button) findViewById(R.id.colour_target);
		badge2 = new BadgeView(this, btnColour);
		badge2.setText("New!");
		badge2.setTextColor(Color.BLUE);
		badge2.setBadgeBackgroundColor(Color.YELLOW);
		badge2.setTextSize(12);
		btnColour.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				badge2.toggle();
			}
		});

		//default animation
		btnAnim1 = (Button) findViewById(R.id.anim1_target);
		badge3 = new BadgeView(this, btnAnim1);
		badge3.setText("84");
		btnAnim1.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				badge3.toggle(true);
			}
		});

		//custom animation
		btnAnim2 = (Button) findViewById(R.id.anim2_target);
		badge4 = new BadgeView(this, btnAnim2);
		badge4.setText("123");
		badge4.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
		badge4.setBadgeMargin(15, 10);
		badge4.setBadgeBackgroundColor(Color.parseColor("#A4C639"));
		btnAnim2.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				TranslateAnimation anim = new TranslateAnimation(-100, 0, 0, 0);
				anim.setInterpolator(new BounceInterpolator());
				anim.setDuration(1000);
				badge4.toggle(anim, null);
			}
		});

		//custom background
		btnCustom = (Button) findViewById(R.id.custom_target);
		badge5 = new BadgeView(this, btnCustom);
		badge5.setText("37");
		badge5.setBackgroundResource(R.drawable.menu_headline_setting_icon_new);
		badge5.setTextSize(16);
		btnCustom.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				badge5.toggle(true);
			}
		});

		//clickable badge
		btnClick = (Button) findViewById(R.id.click_target);
		badge6 = new BadgeView(this, btnClick);
		badge6.setText("click me");
		badge6.setBadgeBackgroundColor(Color.BLUE);
		badge6.setTextSize(16);
		badge6.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Toast.makeText(BadgeCustomActivity.this, "clicked badge", Toast.LENGTH_SHORT).show();
			}
		});
		btnClick.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				badge6.toggle();
			}
		});

		//increment
		btnIncrement = (Button) findViewById(R.id.increment_target);
		badge7 = new BadgeView(this, btnIncrement);
		badge7.setText("0");
		btnIncrement.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if (badge7.isShown()) 
				{
					badge7.increment(1);
				} 
				else 
				{
					badge7.show();
				}
			}
		});
		
		//linear layout container
		target = findViewById(R.id.linear_target);
		badge = new BadgeView(this, target);
		badge.setText("OK");
		badge.show();
		
		//relative layout container
		target = findViewById(R.id.relative_target);
		badge = new BadgeView(this, target);
		badge.setText("OK");
		badge.show();
		
		//frame layout container
		target = findViewById(R.id.frame_target);
		badge = new BadgeView(this, target);
		badge.setText("OK");
		badge.show();
		
		//table layout container
		target = findViewById(R.id.table_target);
		badge = new BadgeView(this, target);
		badge.setText("OK");
		badge.show();
		
		//linear layout
		target = findViewById(R.id.linear_group_target);
		badge = new BadgeView(this, target);
		badge.setText("OK");
		badge.show();
		
		//relative layout
		target = findViewById(R.id.relative_group_target);
		badge = new BadgeView(this, target);
		badge.setText("OK");
		badge.show();
		
		//frame layout
		target = findViewById(R.id.frame_group_target);
		badge = new BadgeView(this, target);
		badge.setText("OK");
		badge.show();
		
		//table layout
		target = findViewById(R.id.tablerow_group_target);
		badge = new BadgeView(this, target);
		badge.setText("OK");
		badge.show();
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.textview_item1);
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
