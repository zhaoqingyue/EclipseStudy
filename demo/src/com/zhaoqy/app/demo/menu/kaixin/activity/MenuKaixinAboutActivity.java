/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinAboutActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 资料关于
 * @author: zhaoqy
 * @date: 2015-11-9 下午5:48:11
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.kaixin.item.FriendInfoResult;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;
import com.zhaoqy.app.demo.menu.kaixin.util.Utils;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuKaixinAboutActivity extends MenuKaixinBaseActivity implements OnClickListener
{
	private ImageView   mBack;
	private TextView    mTitle;
	private TextView    mSubmit;
	private ImageButton mAvatar;
	private Button      mAvatarChange;
	private TextView    mName;
	private TextView    mSignature;
	private TextView    mGender;
	private TextView    mDate;
	private TextView    mConstellation;
	private ImageView   mDateIcon;
	private ImageView   mAddressIcon;
	private ImageView   mTelephoneIcon;
	private String      mUid;         //当前查看的用户Id
	private FriendInfoResult mResult; //当前查看的用户的资料数据

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_about);
		findViewById();
		setListener();
		init();
	}

	private void findViewById() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mSubmit = (TextView) findViewById(R.id.id_title_right_text);
		mAvatar = (ImageButton) findViewById(R.id.about_avatar);
		mAvatarChange = (Button) findViewById(R.id.about_avatar_change);
		mName = (TextView) findViewById(R.id.about_name);
		mSignature = (TextView) findViewById(R.id.about_signature);
		mGender = (TextView) findViewById(R.id.about_gender);
		mDate = (TextView) findViewById(R.id.about_date);
		mConstellation = (TextView) findViewById(R.id.about_constellation);
		mDateIcon = (ImageView) findViewById(R.id.about_date_icon);
		mAddressIcon = (ImageView) findViewById(R.id.about_address_icon);
		mTelephoneIcon = (ImageView) findViewById(R.id.about_telephone_icon);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mAvatarChange.setOnClickListener(this);
	}
	
	private void init() 
	{
		mUid = getIntent().getStringExtra("uid");// 接收传递过来的用户ID
		mResult = getIntent().getParcelableExtra("result");// 接收传递过来的用资料
		//当Id不存在时为当前登录用户,否则则是其他用户,根据用户的不同,显示不同界面效果
		if (mUid == null) 
		{
			mBack.setVisibility(View.VISIBLE);
			mTitle.setText("我的资料");
			mSubmit.setVisibility(View.VISIBLE);
			mSubmit.setText("提交");
			mAvatarChange.setVisibility(View.VISIBLE);
			mDateIcon.setVisibility(View.VISIBLE);
			mAddressIcon.setVisibility(View.VISIBLE);
			mTelephoneIcon.setVisibility(View.VISIBLE);
		} 
		else 
		{
			mBack.setVisibility(View.VISIBLE);
			mTitle.setText(mResult.getName() + "的资料");
			mSubmit.setVisibility(View.GONE);
			mAvatarChange.setVisibility(View.INVISIBLE);
			mDateIcon.setVisibility(View.GONE);
			mAddressIcon.setVisibility(View.GONE);
			mTelephoneIcon.setVisibility(View.GONE);
		}
		//填充界面数据
		mAvatar.setImageBitmap(mApplication.getAvatar(mResult.getAvatar()));
		mName.setText(mResult.getName());
		mSignature.setText(new TextUtil(mApplication).replace(mResult.getSignature()));
		mGender.setText(Utils.getGender(mResult.getGender()));
		mDate.setText(mResult.getDate());
		mConstellation.setText(mResult.getConstellation());
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
		case R.id.about_avatar_change:
		{
			//暂时不做任何操作
			break;
		}
		default:
			break;
		}
	}
}
