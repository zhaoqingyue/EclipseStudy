package com.zhaoqy.app.demo.register;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.login.qq.DialogFactory;

public class RegisterQQActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private TextView  mTitle;
	private ImageView mBack;
	private Button   mRegister;
	private EditText mEmail;
	private EditText mName;
	private EditText mPasswd;
	private EditText mConfirm;
	private Dialog   mDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_qq);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mRegister = (Button) findViewById(R.id.register_register);
		mEmail = (EditText) findViewById(R.id.register_input_email);
		mName = (EditText) findViewById(R.id.register_input_name);
		mPasswd = (EditText) findViewById(R.id.register_input_password);
		mConfirm = (EditText) findViewById(R.id.register_input_password_again);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mRegister.setOnClickListener(this);
	}
	
	private void initData() 
	{
		mTitle.setText("注册");
		mBack.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onBackPressed() 
	{
		finish();
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.register_register:
		{
			goRegister();
			break;
		}
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		default:
			break;
		}
	}
	
	private void goRegister() 
	{
		String email = mEmail.getText().toString();
		String name = mName.getText().toString();
		String passwd = mPasswd.getText().toString();
		String passwd2 = mConfirm.getText().toString();
		
		if (email.equals("") || name.equals("") || passwd.equals("") || passwd2.equals("")) 
		{
			DialogFactory.ToastDialog(mContext, "QQ注册", "亲！带*项是不能为空的哦");
		} 
		else 
		{
			if (passwd.equals(passwd2)) 
			{
				showRequestDialog();
				// 提交注册信息
			} 
			else 
			{
				DialogFactory.ToastDialog(mContext, "QQ注册", "亲！您两次输入的密码不同哦");
			}
		}
	}
	
	private void showRequestDialog() 
	{
		if (mDialog != null) 
		{
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在注册中...");
		mDialog.show();
	}
}
