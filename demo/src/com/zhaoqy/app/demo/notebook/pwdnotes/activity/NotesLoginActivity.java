package com.zhaoqy.app.demo.notebook.pwdnotes.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.pwdnotes.view.PwdView;
import com.zhaoqy.app.demo.notebook.pwdnotes.view.PwdView.OnCompleteListener;

public class NotesLoginActivity extends Activity implements OnClickListener, OnCompleteListener
{
	private Context  mContext;
	private PwdView  mPwdView;
	private Toast    mToast;
	private TextView mTip; 
	private Button   mDraw;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwdnotes_login);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}

	private void initView() 
	{
		mPwdView = (PwdView) findViewById(R.id.id_login_pwdview);
		mTip = (TextView) findViewById(R.id.id_login_tip);
		mDraw = (Button) findViewById(R.id.id_login_draw);
	}
	
	private void initData() 
	{
		//如果密码为空,则进入设置密码的界面
		if (mPwdView.isPasswordEmpty()) 
		{
			mPwdView.setVisibility(View.GONE);
			mDraw.setVisibility(View.VISIBLE);
			mTip.setText("请先绘制手势密码");
		} 
		else 
		{
			mTip.setText("请输入手势密码");
			mPwdView.setVisibility(View.VISIBLE);
			mDraw.setVisibility(View.GONE);
		}
	}
	
	private void setListener() 
	{
		mPwdView.setOnCompleteListener(this);
		mDraw.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.id_login_draw:
		{
			Intent intent = new Intent(mContext, NotesSetPwdActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onComplete(String password) 
	{
		if (mPwdView.verifyPassword(password)) 
		{
			showToast("登陆成功！");
			Intent intent = new Intent(mContext, NotesMainActivity.class);
			startActivity(intent);
			finish();
		} 
		else 
		{
			showToast("密码输入错误,请重新输入");
			mPwdView.clearPassword();
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
