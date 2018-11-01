package com.zhaoqy.app.demo.notebook.pwdnotes.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.pwdnotes.util.StringUtil;
import com.zhaoqy.app.demo.notebook.pwdnotes.view.PwdView;
import com.zhaoqy.app.demo.notebook.pwdnotes.view.PwdView.OnCompleteListener;

public class NotesSetPwdActivity extends Activity implements OnClickListener, OnCompleteListener
{
	private Context mContext;
	private PwdView mPwdView;
	private Toast   mToast;
	private String  mPwd;
	private Button  mSave;
	private Button  mReset;
	private boolean mNeedverify = true;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwdnotes_setpwd);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mPwdView = (PwdView) findViewById(R.id.id_setpwd_pwdview);
		mSave = (Button) findViewById(R.id.tid_setpwd_save);
		mReset = (Button) findViewById(R.id.tid_setpwd_reset);
	}

	private void initData() 
	{
		//如果密码为空,直接输入密码
		if (mPwdView.isPasswordEmpty()) 
		{
			mNeedverify = false;
			showToast("请输入密码");
		}
	}

	private void setListener() 
	{
		mPwdView.setOnCompleteListener(this);
		mSave.setOnClickListener(this);
		mReset.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.tid_setpwd_save:
		{
			if (StringUtil.isNotEmpty(mPwd)) 
			{
				mPwdView.resetPassWord(mPwd);
				mPwdView.clearPassword();
				showToast("密码修改成功,请记住密码.");
				startActivity(new Intent(mContext, NotesLoginActivity.class));
				finish();
			} 
			else 
			{
				mPwdView.clearPassword();
				showToast("密码不能为空,请输入密码.");
			}
			break;
		}
		case R.id.tid_setpwd_reset:
		{
			mPwdView.clearPassword();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onComplete(String password) 
	{
		mPwd = password;
		if (mNeedverify) 
		{
			if (mPwdView.verifyPassword(password)) 
			{
				showToast("密码输入正确,请输入新密码!");
				mPwdView.clearPassword();
				mNeedverify = false;
			} 
			else 
			{
				showToast("错误的密码,请重新输入!");
				mPwdView.clearPassword();
				mPwd = "";
			}
		}
	}
	
	private void showToast(CharSequence message) 
	{
		if (null == mToast) 
		{
			mToast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
		} 
		else 
		{
			mToast.setText(message);
		}
		mToast.show();
	}
}
