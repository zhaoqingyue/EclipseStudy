package com.zhaoqy.app.demo.login.qq;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.register.RegisterQQActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class LoginQQActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private TextView  mTitle;
	private ImageView mBack;
	private EditText  mAccount;    //输入qq号码
	private EditText  mPassword;   //输入密码
	private Button    mLogin;      //登陆
	private Button    mRegister;   //注册
	private CheckBox  mRbpd;       //记住密码
	private CheckBox  mHdlg;       //隐藏登陆
	private CheckBox  mSllg;       //静音登陆
	private CheckBox  mPcol;       //允许手机/电脑同时在线
	private CheckBox  mRcgm;       //接收群消息
	private ImageView mMoreImage;  //“更多登录选项”的箭头图片
	private View      mMoreOption; //更多登录选项
	private View      mMoreOptionCase;     //“更多登录选项”中的内容view
	private boolean   mShowOption = false; //“更多登录选项”的内容是否显示
	private Dialog    mDialog = null;
	private ShareUtil mSpu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_qq);
		mContext = this;
		
		initView();
		initCheckBoxState();
		initListener();
	}
	
	private void initView() 
	{
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		
		mAccount = (EditText) findViewById(R.id.lgoin_qq_account);
		mPassword = (EditText) findViewById(R.id.login_qq_password);
		mLogin = (Button) findViewById(R.id.login_qq_login);
		mRegister = (Button) findViewById(R.id.login_qq_register);
		
		mRbpd = (CheckBox) findViewById(R.id.login_qq_remember_password);
		mHdlg = (CheckBox) findViewById(R.id.login_more_options_hide_login);
		mSllg = (CheckBox) findViewById(R.id.login_more_options_silence_login);
		mPcol = (CheckBox) findViewById(R.id.login_more_options_allow_phone_computer_online);
		mRcgm = (CheckBox) findViewById(R.id.login_more_options_receive_group_messages);
		
		mMoreImage = (ImageView) findViewById(R.id.login_more_options_title_image);
		mMoreOption = findViewById(R.id.login_more_options_title);
		mMoreOptionCase = findViewById(R.id.login_more_options_cases);
		mSpu = new ShareUtil(mContext, ShareUtil.SAVE_USER);
	}
	
	private void initListener()
	{
		mBack.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		mRegister.setOnClickListener(this);
		mMoreOption.setOnClickListener(this);
		
		mBack.setVisibility(View.VISIBLE);
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(mContext.getResources().getString(R.string.login));
		
		mRbpd.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				mSpu.setIsRememberPassword(isChecked);
			}
		});
		
		mHdlg.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				
				mSpu.setIsHideLogin(isChecked);
			}
		});
		
		mSllg.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				mSpu.setIsSilenceLogin(isChecked);
			}
		});
		
		mPcol.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				mSpu.setIsOnlineTogether(isChecked);
			}
		});
		
		mRcgm.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				mSpu.setIsReceiveGroupMessages(isChecked);
			}
		});
	}
	
	private void initCheckBoxState()
	{
		//是否记住密码
		if (mSpu.getIsRememberPassword())
		{
			mRbpd.setChecked(true);
			mAccount.setText(mSpu.getNumber());
			mPassword.setText(mSpu.getPasswd());
		}
		else
		{
			mRbpd.setChecked(false);
			mAccount.setText("");
			mPassword.setText("");
		}
		
		//是否隐身登陆
		if (mSpu.getIsHideLogin())
		{
			mHdlg.setChecked(true);
		}
		else
		{
			mHdlg.setChecked(false);
		}
		
		//静音登陆
		if (mSpu.getIsSilenceLogin())
		{
			mSllg.setChecked(true);
		}
		else
		{
			mSllg.setChecked(false);
		}
		
		//是否允许手机/电脑同时在线
		if (mSpu.getIsOnlineTogether())
		{
			mPcol.setChecked(true);
		}
		else
		{
			mPcol.setChecked(false);
		}
		
		//是否接收群消息
		if (mSpu.getIsReceiveGroupMessages())
		{
			mRcgm.setChecked(true);
		}
		else
		{
			mRcgm.setChecked(false);
		}
	}
	
	public void showMoreOption(boolean moreOption) 
	{
		if (moreOption) 
		{
			mMoreOptionCase.setVisibility(View.GONE);
			mMoreImage.setImageResource(R.drawable.login_more_up);
			mShowOption = true;
		} 
		else 
		{
			mMoreOptionCase.setVisibility(View.VISIBLE);
			mMoreImage.setImageResource(R.drawable.login_more_down);
			mShowOption = false;
		}
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
		case R.id.login_more_options_title:
		{
			showMoreOption(!mShowOption);
			break;
		}
		case R.id.login_qq_login:
		{
			submit();
			break;
		}
		case R.id.login_qq_register:
		{
			goRegisterActivity();
			break;
		}
		default:
			break;
		}
	}
	
	public void goRegisterActivity() 
	{
		Intent intent = new Intent();
		intent.setClass(mContext, RegisterQQActivity.class);
		startActivity(intent);
	}

	private void showRequestDialog() 
	{
		if (mDialog != null) 
		{
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(mContext, "正在验证账号...");
		mDialog.show();
	}
	
	private void submit() 
	{
		String accounts = mAccount.getText().toString();
		String password = mPassword.getText().toString();
		if (accounts.length() == 0 || password.length() == 0) 
		{
			DialogFactory.ToastDialog(mContext, "QQ登录", "亲！帐号或密码不能为空哦");
		}
		else 
		{
			if (isNetworkAvailable())
			{
				showRequestDialog();
				//调试, 默认登录成功
				/*Intent intent = new Intent(LoginActivity.this, FriendListActivity.class);
				startActivity(intent);*/
				
				//通过Socket验证信息
				if (mDialog.isShowing())
				{
					mDialog.dismiss();
				}
			}
			else
			{
				ShowDialog(mContext);
			}
		}
	}
	
	@Override
	// 添加菜单
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.login_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	// 菜单选项添加事件处理
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case R.id.login_menu_setting:
			break;
		case R.id.login_menu_exit:
			exitDialog(mContext, "QQ提示", "亲！您真的要退出吗？");
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() //捕获返回按键
	{
		exitDialog(mContext, "QQ提示", "亲！您真的要退出吗？");
	}
	
	/**
	 * 退出时的提示框
	 * @param context 上下文对象
	 * @param title 标题
	 * @param msg 内容
	 */
	private void exitDialog(Context context, String title, String msg) 
	{
		new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setPositiveButton("确定", 
		new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				finish();
			}
		}).setNegativeButton("取消", null).create().show();
	}

	/**
	 * 判断手机网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	private boolean isNetworkAvailable() 
	{
		ConnectivityManager mgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) 
		{
			for (int i = 0; i < info.length; i++) 
			{
				if (info[i].getState() == NetworkInfo.State.CONNECTED) 
				{
					return true;
				}
			}
		}
		return false;
	}

	private void ShowDialog(Context context) 
	{
		new AlertDialog.Builder(context).setTitle("温馨提示").setMessage("亲！您的网络连接未打开哦").setPositiveButton("前往打开",
		new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				startActivity(intent);
			}
		}).setNegativeButton("取消", null).create().show();
	}
}
