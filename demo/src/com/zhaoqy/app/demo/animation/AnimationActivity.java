package com.zhaoqy.app.demo.animation;

import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimationActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mBasic;
	private TextView  mCustom;
	private TextView  mImage;
	private TextView  mTicket;
	private TextView  mAddShop;
	private TextView  mYouku;
	private TextView  mScane;
	private TextView  mTaobaoMenu1;
	private TextView  mTaobaoMenu2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mBasic = (TextView) findViewById(R.id.id_animation_basic);
		mCustom = (TextView) findViewById(R.id.id_animation_custom);
		mImage = (TextView) findViewById(R.id.id_animation_image);
		mTicket = (TextView) findViewById(R.id.id_animation_out_ticket);
		mAddShop = (TextView) findViewById(R.id.id_animation_add_shop);
		mYouku = (TextView) findViewById(R.id.id_animation_youku);
		mScane = (TextView) findViewById(R.id.id_animation_scane);
		mTaobaoMenu1 = (TextView) findViewById(R.id.id_animation_taobao_menu1);
		mTaobaoMenu2 = (TextView) findViewById(R.id.id_animation_taobao_menu2);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mBasic.setOnClickListener(this);
		mCustom.setOnClickListener(this);
		mImage.setOnClickListener(this);
		mTicket.setOnClickListener(this);
		mAddShop.setOnClickListener(this);
		mYouku.setOnClickListener(this);
		mScane.setOnClickListener(this);
		mTaobaoMenu1.setOnClickListener(this);
		mTaobaoMenu2.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.main_item6);
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
		case R.id.id_animation_basic:
		{
			Intent intent = new Intent(mContext, AnimationBasicActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_custom:
		{
			Intent intent = new Intent(mContext, AnimationCustomActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_image:
		{
			Intent intent = new Intent(mContext, AnimationImageActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_out_ticket:
		{
			Intent intent = new Intent(mContext, AnimationOutTicketActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_add_shop:
		{
			Intent intent = new Intent(mContext, AnimationAddShopActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_youku:
		{
			Intent intent = new Intent(mContext, AnimationYouKuActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_scane:
		{
			Intent intent = new Intent(mContext, AnimationScaneActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_taobao_menu1:
		{
			Intent intent = new Intent(mContext, AnimationtaobaoMenu1Activity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_animation_taobao_menu2:
		{
			Intent intent = new Intent(mContext, AnimationtaobaoMenu2Activity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
