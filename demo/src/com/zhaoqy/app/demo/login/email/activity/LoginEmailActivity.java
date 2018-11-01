package com.zhaoqy.app.demo.login.email.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.login.email.util.PreferenceUtil;

public class LoginEmailActivity extends Activity implements OnClickListener, TextWatcher
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private EditText  mEmailAddress;
	private EditText  mPassword;
	private Button    mClearAddress;
	private Button    mEmailLogin;
	private CheckBox  mRemenber;
	private CheckBox  mAutoLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_email);
		mContext = this;
		
		initView();
		setListener();
		initData();
		
		isRemenberPwd();
	}

	private void initView() 
	{
		mBack =  (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		
		mEmailAddress = (EditText) findViewById(R.id.emailAddress);
		mPassword = (EditText) findViewById(R.id.password);
		mClearAddress = (Button) findViewById(R.id.clear_address);
		mEmailLogin = (Button) findViewById(R.id.login_btn);
		mRemenber = (CheckBox) findViewById(R.id.remenberPassword);
		mAutoLogin = (CheckBox) findViewById(R.id.autoLogin);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mClearAddress.setOnClickListener(this);
		mEmailAddress.addTextChangedListener(this);
		mEmailLogin.setOnClickListener(this);
		mRemenber.setOnClickListener(this);
		mAutoLogin.setOnClickListener(this);
	}
	
	private void initData()
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.login_item3);
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
		case R.id.clear_address:
		{
			mEmailAddress.setText("");
			break;
		}
		case R.id.login_btn:
		{
			Intent intent = new Intent(mContext, LoginEmailHomeActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		case R.id.remenberPassword:
		{
			remenberPwd();
			break;
		}
		default:
			break;
		}
	}
	
	/**
	 * 是否记住密码
	 */
	private void isRemenberPwd()
	{
		if(PreferenceUtil.getIsRdPwd(mContext))
		{
			String addr = PreferenceUtil.getEmail(mContext);
			String pwd = PreferenceUtil.getPwd(mContext);
			mEmailAddress.setText(addr);
			mPassword.setText(pwd);
			mRemenber.setChecked(true);
		}
	}
	
	/**
	 * 记住密码
	 */
	private void remenberPwd()
	{
		if(PreferenceUtil.getIsRdPwd(mContext))
		{
			PreferenceUtil.setIsRdPwd(mContext, false);
			mRemenber.setChecked(false);
		}
		else
		{
			PreferenceUtil.setIsRdPwd(mContext, true);
			PreferenceUtil.setEmail(mContext, mEmailAddress.getText().toString().trim());
			PreferenceUtil.setPwd(mContext, mPassword.getText().toString().trim());
			mRemenber.setChecked(true);
		}
	}
	
	/**
	 * 文本监听事件
	 * 
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) 
	{
		if (!TextUtils.isEmpty(s)) 
		{
			mClearAddress.setVisibility(View.VISIBLE);
		}
		else 
		{
			mClearAddress.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void afterTextChanged(Editable s) 
	{
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,	int after) 
	{
	}
}
