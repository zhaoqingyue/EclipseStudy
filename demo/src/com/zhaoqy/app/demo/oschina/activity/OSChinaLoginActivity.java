/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaLoginActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: oschina用户登录
 * @author: zhaoqy
 * @date: 2015-11-17 下午3:09:25
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.common.ApiClient;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.Result;
import com.zhaoqy.app.demo.oschina.item.User;
import com.zhaoqy.app.demo.oschina.util.StringUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class OSChinaLoginActivity extends OSChinaBaseActivity implements OnClickListener
{
	public final static int LOGIN_OTHER = 0x00;
	public final static int LOGIN_MAIN = 0x01;
	public final static int LOGIN_SETTING = 0x02;
	
	private AutoCompleteTextView mAccount;
	private InputMethodManager mInputMethodManager;
	private AnimationDrawable  mAnimation;
	private ViewSwitcher mViewSwitcher;
	private ImageButton  mClose;
	private Context      mContext;
	private Button       mLogin;
	private EditText     mPwd;
	private View         mLoading;
	private CheckBox     mRemember;
	private int          mLoginType;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oschina_login);
        mContext = this;
        
        initView();
        setListener();
        initData();
    }
    
    private void initView() 
    {
    	 mViewSwitcher = (ViewSwitcher)findViewById(R.id.logindialog_view_switcher);       
         mLoading = (View)findViewById(R.id.login_loading);
         mAccount = (AutoCompleteTextView)findViewById(R.id.login_account);
         mPwd = (EditText)findViewById(R.id.login_password);
         mRemember = (CheckBox)findViewById(R.id.login_checkbox_rememberMe);
         mClose = (ImageButton)findViewById(R.id.login_close_button);
         mLogin = (Button)findViewById(R.id.login_btn_login);
	}

	private void setListener() 
	{
	    mClose.setOnClickListener(this);    
	    mLogin.setOnClickListener(this);
	}

	private void initData() 
	{
        mInputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        mLoginType = getIntent().getIntExtra("LOGINTYPE", LOGIN_OTHER);
		
		//是否显示登录信息
        User user = UserHelper.getLoginInfo(mContext);
        if(user == null || !user.isRememberMe()) return;
        if(!StringUtils.isEmpty(user.getAccount()))
        {
        	mAccount.setText(user.getAccount());
        	mAccount.selectAll();
        	mRemember.setChecked(user.isRememberMe());
        }
        if(!StringUtils.isEmpty(user.getPwd()))
        {
        	mPwd.setText(user.getPwd());
        }
	}

	//登录验证
    private void login(final String account, final String pwd, final boolean isRememberMe) 
    {
		new Thread()
		{
			public void run() 
			{
				Message msg =new Message();
				try 
				{
	                User user = UserHelper.loginVerify(mContext, account, pwd);
	                user.setAccount(account);
	                user.setPwd(pwd);
	                user.setRememberMe(isRememberMe);
	                Result res = user.getValidate();
	                if(res.OK())
	                {
	                	//保存登录信息
	                	UserHelper.saveLoginInfo(mContext, user);
	                	msg.what = 1;//成功
	                	msg.obj = user;
	                }
	                else
	                {
	                	//清除登录信息
	                	UserHelper.cleanLoginInfo(mContext);
	                	msg.what = 0;//失败
	                	msg.obj = res.getErrorMessage();
	                }
	            } 
				catch (Exception e) 
				{
	            	e.printStackTrace();
			    	msg.what = -1;
			    	msg.obj = e;
	            }
				handler.sendMessage(msg);
			}
		}.start();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
    	if(keyCode == KeyEvent.KEYCODE_BACK) 
    	{
    		onDestroy();
    	}
    	return super.onKeyDown(keyCode, event);
    }

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.login_close_button:
		{
			finish();
			break;
		}
		case R.id.login_btn_login:
		{
			//隐藏软键盘
			mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);  
			String account = mAccount.getText().toString();
			String pwd = mPwd.getText().toString();
			boolean isRememberMe = mRemember.isChecked();
			//判断输入
			if(StringUtils.isEmpty(account))
			{
				Toast.makeText(mContext, "请输入邮箱", Toast.LENGTH_SHORT).show();
				return;
			}
			if(StringUtils.isEmpty(pwd))
			{
				Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
				return;
			}
			
	        mClose.setVisibility(View.GONE);
	        mAnimation = (AnimationDrawable)mLoading.getBackground();
	        mAnimation.start();
	        mViewSwitcher.showNext();
	        login(account, pwd, isRememberMe);
			break;
		}
		default:
			break;
		}
	}
	
    @SuppressLint("HandlerLeak")
	private Handler handler = new Handler() 
    {
		public void handleMessage(Message msg) 
		{
			if(msg.what == 1)
			{
				User user = (User)msg.obj;
				if(user != null)
				{
					//清空原先cookie
					ApiClient.cleanCookie();
					//发送通知广播
					UIHelper.sendBroadCast(mContext, user.getNotice());
					//提示登陆成功
					UIHelper.ToastMessage(mContext, "登录成功");
					if(mLoginType == LOGIN_MAIN)
					{
						//跳转--加载用户动态
						Intent intent = new Intent(mContext, OSChinaMainActivity.class);
						intent.putExtra("LOGIN", true);
						startActivity(intent);
					}
					else if(mLoginType == LOGIN_SETTING)
					{
						//跳转--用户设置页面
						Intent intent = new Intent(mContext, OSChinaSettingActivity.class);
						intent.putExtra("LOGIN", true);
						startActivity(intent);
					}
					finish();
				}
			}
			else if(msg.what == 0)
			{
				mViewSwitcher.showPrevious();
				mClose.setVisibility(View.VISIBLE);
				UIHelper.ToastMessage(mContext, "登录失败"+msg.obj);
			}
			else if(msg.what == -1)
			{
				mViewSwitcher.showPrevious();
				mClose.setVisibility(View.VISIBLE);
			}
		}
	};
}
