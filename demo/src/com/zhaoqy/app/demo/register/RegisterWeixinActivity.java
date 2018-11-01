package com.zhaoqy.app.demo.register;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class RegisterWeixinActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private TextView  mTitle;
	private ImageView mBack;
	private Button    mRegister;
	private Button    mSend;
	private EditText  mUsertel;
	private EditText  mPassword;
	private EditText  mCode;
	private MyCount   mMyCount;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		setContentView(R.layout.activity_register_weixin);
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
		mUsertel = (EditText) findViewById(R.id.register_weixin_usertel);
		mPassword = (EditText) findViewById(R.id.register_weixin_password);
		mCode = (EditText) findViewById(R.id.register_weixin_code);
		mSend = (Button) findViewById(R.id.register_weixin_send);
		mRegister = (Button) findViewById(R.id.register_weixin_register);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mSend.setOnClickListener(this);
		mRegister.setOnClickListener(this);
		mUsertel.addTextChangedListener(new TelTextChange());
		mPassword.addTextChangedListener(new TextChange());
	}
	
	private void initData() 
	{
		mTitle.setText(mContext.getResources().getString(R.string.register));
		mBack.setVisibility(View.VISIBLE);
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
		case R.id.register_weixin_send:
		{
			if (mMyCount == null) 
			{
				//第一参数是总的时间，第二个是间隔时间
				mMyCount = new MyCount(60000, 1000); 
			}
			mMyCount.start();
			break;
		}
		case R.id.register_weixin_register:
		{
			getRegister();
			break;
		}
		default:
			break;
		}
	}

	private void getRegister() 
	{
		final String name = mUsertel.getText().toString();
		final String pwd = mPassword.getText().toString();
		String code = mCode.getText().toString();
		if (!CheckUtil.isMobileNO(name)) 
		{
			ToastUtil.showLongToast(mContext, "请使用手机号码注册账户！ ");
			return;
		}
		if (TextUtils.isEmpty(code)) 
		{
			ToastUtil.showLongToast(mContext, "请填写手机号码，并获取验证码！");
			return;
		}
		if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(code)) 
		{
			ToastUtil.showLongToast(mContext, "请填写核心信息！");
			return;
		}
	}

	//手机号 EditText监听器
	class TelTextChange implements TextWatcher 
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
			String phone = mUsertel.getText().toString();
			if (phone.length() == 11) 
			{
				if (CheckUtil.isMobileNO(phone)) 
				{
					mSend.setBackgroundDrawable(getResources().getDrawable( R.drawable.button_bg_green_selector));
					mSend.setTextColor(0xFFFFFFFF);
					mSend.setEnabled(true);
					mRegister.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg_green_selector));
					mRegister.setTextColor(0xFFFFFFFF);
					mRegister.setEnabled(true);
				} 
				else 
				{
					mUsertel.requestFocus();
					ToastUtil.showLongToast(mContext, "请输入正确的手机号码！");
				}
			} 
			else 
			{
				mSend.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_enable_green_selector));
				mSend.setTextColor(0xFFD0EFC6);
				mSend.setEnabled(false);
				mRegister.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_enable_green_selector));
				mRegister.setTextColor(0xFFD0EFC6);
				mRegister.setEnabled(true);
			}
		}
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
			boolean Sign1 = mCode.getText().length() > 0;
			boolean Sign2 = mUsertel.getText().length() > 0;
			boolean Sign3 = mPassword.getText().length() > 0;

			if (Sign1 & Sign2 & Sign3) 
			{
				mRegister.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg_green_selector));
				mRegister.setTextColor(0xFFFFFFFF);
				mRegister.setEnabled(true);
			} 
			else 
			{
				mRegister.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_enable_green_selector));
				mRegister.setTextColor(0xFFD0EFC6);
				mRegister.setEnabled(false);
			}
		}
	}

	/* 定义一个倒计时的内部类 */
	private class MyCount extends CountDownTimer 
	{
		public MyCount(long millisInFuture, long countDownInterval) 
		{
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() 
		{
			mSend.setEnabled(true);
			mSend.setText(mContext.getResources().getString(R.string.register_weixin_send_security_code));
		}

		@Override
		public void onTick(long millisUntilFinished) 
		{
			mSend.setEnabled(false);
			mSend.setText("(" + millisUntilFinished / 1000 + ")秒");
		}
	}
}
