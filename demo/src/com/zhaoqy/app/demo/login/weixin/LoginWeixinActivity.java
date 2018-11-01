package com.zhaoqy.app.demo.login.weixin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.register.RegisterWeixinActivity;
import com.zhaoqy.app.demo.weixin.ui.activity.WebViewActivity;

public class LoginWeixinActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private TextView  mTitle;
	private TextView  mQuestion;
	private ImageView mBack;
	private Button    mLogin;
	private Button    mRegister;
	private EditText  mUsertel;
	private EditText  mPassword;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		setContentView(R.layout.activity_login_weixin);
		super.onCreate(savedInstanceState);
		mContext = this;
		initView(); 
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mUsertel = (EditText) findViewById(R.id.login_weixin_usertel);
		mPassword = (EditText) findViewById(R.id.login_weixin_password);
		mQuestion = (TextView) findViewById(R.id.login_weixin_question);
		mLogin = (Button) findViewById(R.id.login_weixin_login);
		mRegister = (Button) findViewById(R.id.login_weixin_register);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		mRegister.setOnClickListener(this);
		mQuestion.setOnClickListener(this);
		mUsertel.addTextChangedListener(new TextChange());
		mPassword.addTextChangedListener(new TextChange());
	}
	
	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(mContext.getResources().getString(R.string.login));
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
		case R.id.login_weixin_login:
		{
			getLogin();
			break;
		}
		case R.id.login_weixin_question:
		{
			Intent intent = new Intent();
			intent.setClass(mContext, WebViewActivity.class);
			intent.putExtra("Title", "帮助");
			intent.putExtra("URL", "http://weixin.qq.com/");
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.login_weixin_register:
		{
			Intent intent = new Intent();
			intent.setClass(mContext, RegisterWeixinActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			break;
		}
		default:
			break;
		}
	}
	
	private void getLogin() 
	{
	}

	//EditText监听器
	class TextChange implements TextWatcher 
	{
		@Override
		public void afterTextChanged(Editable arg0) 
		{
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
		{
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onTextChanged(CharSequence cs, int start, int before, int count) 
		{
			boolean Sign2 = mUsertel.getText().length() > 0;
			boolean Sign3 = mPassword.getText().length() > 4;
			if (Sign2 & Sign3) 
			{
				mLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg_green_selector));
				mLogin.setEnabled(true);
			}
			else 
			{
				mLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_enable_green_selector));
				mLogin.setTextColor(0xFFD0EFC6);
				mLogin.setEnabled(false);
			}
		}
	}
}
