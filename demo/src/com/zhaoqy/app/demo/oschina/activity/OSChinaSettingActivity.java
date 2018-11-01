/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaSettingActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: oschina设置
 * @author: zhaoqy
 * @date: 2015-11-18 上午9:48:42
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import java.io.File;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.helper.OSChinaAppManager;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.util.FileUtils;
import com.zhaoqy.app.demo.oschina.util.MethodsCompat;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressWarnings("deprecation")
public class OSChinaSettingActivity extends PreferenceActivity implements OnPreferenceClickListener
{
	private CheckBoxPreference mHttpsLogin;
	private CheckBoxPreference mLoadImage;
	private CheckBoxPreference mScroll;
	private CheckBoxPreference mVoice;
	private Context    mContext;
	private Preference mAccount;
	private Preference mMyInfo;
	private Preference mCache;
	private Preference mFeedback;
	private Preference mAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		
		//添加Activity到堆栈
		OSChinaAppManager.getAppManager().addActivity(this);
		//设置显示Preferences
		addPreferencesFromResource(R.xml.oschina_settings);
		
		ListView localListView = getListView();
		localListView.setBackgroundColor(0);
		localListView.setCacheColorHint(0);
		((ViewGroup)localListView.getParent()).removeView(localListView);
		ViewGroup localViewGroup = (ViewGroup)getLayoutInflater().inflate(R.layout.oschina_setting, null);
		((ViewGroup)localViewGroup.findViewById(R.id.setting_content)).addView(localListView, -1, -1);
		setContentView(localViewGroup);
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mHttpsLogin = (CheckBoxPreference)findPreference("httpslogin");
		mLoadImage = (CheckBoxPreference)findPreference("loadimage");
		mScroll = (CheckBoxPreference)findPreference("scroll");
		mAccount = (Preference)findPreference("account");
		mVoice = (CheckBoxPreference)findPreference("voice");
		mMyInfo = (Preference)findPreference("myinfo");
		mCache = (Preference)findPreference("cache");
		mFeedback = (Preference)findPreference("feedback");
		mAbout = (Preference)findPreference("about");
	}

	private void setListener() 
	{
		mHttpsLogin.setOnPreferenceClickListener(this);
		mLoadImage.setOnPreferenceClickListener(this);
		mScroll.setOnPreferenceClickListener(this);
		mVoice.setOnPreferenceClickListener(this);
		mAccount.setOnPreferenceClickListener(this);
		mMyInfo.setOnPreferenceClickListener(this);
		mCache.setOnPreferenceClickListener(this);
		mFeedback.setOnPreferenceClickListener(this);
		mAbout.setOnPreferenceClickListener(this);
	}
	
	private void initData() 
	{
		mHttpsLogin.setChecked(UserHelper.isHttpsLogin(mContext));
		if(UserHelper.isHttpsLogin(mContext))
		{
			mHttpsLogin.setSummary("当前以 HTTPS 登录");
		}
		else
		{
			mHttpsLogin.setSummary("当前以 HTTP 登录");
		}
		
		mLoadImage.setChecked(UIHelper.isLoadImage(mContext));
		if(UIHelper.isLoadImage(mContext))
		{
			mLoadImage.setSummary("页面加载图片 (默认在WIFI网络下加载图片)");
		}
		else
		{
			mLoadImage.setSummary("页面不加载图片 (默认在WIFI网络下加载图片)");
		}
		
		mScroll.setChecked(UIHelper.isScroll(mContext));
		if(UIHelper.isScroll(mContext))
		{
			mScroll.setSummary("已启用左右滑动");
		}
		else
		{
			mScroll.setSummary("已关闭左右滑动");
		}
		
		mVoice.setChecked(UIHelper.isVoice(mContext));
		if(UIHelper.isVoice(mContext))
		{
			mVoice.setSummary("已开启提示声音");
		}
		else
		{
			mVoice.setSummary("已关闭提示声音");
		}
		

		if(UserHelper.isLogin())
		{
			mAccount.setTitle("注销登录");
		}
		else
		{
			mAccount.setTitle("用户登录");
		}
		
		//计算缓存大小		
		long fileSize = 0;
		String cacheSize = "0KB";		
		File filesDir = getFilesDir();
		File cacheDir = getCacheDir();
		
		fileSize += FileUtils.getDirSize(filesDir);
		fileSize += FileUtils.getDirSize(cacheDir);		
		//2.2版本才有将应用缓存转移到sd卡的功能
		if(MethodsCompat.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO))
		{
			File externalCacheDir = MethodsCompat.getExternalCacheDir(this);
			fileSize += FileUtils.getDirSize(externalCacheDir);
		}		
		if(fileSize > 0)
		{
			cacheSize = FileUtils.formatFileSize(fileSize);
		}
		mCache.setSummary(cacheSize);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) 
	{
		if (preference == mHttpsLogin)
		{
			UserHelper.setConfigHttpsLogin(mContext, mHttpsLogin.isChecked());
			if(mHttpsLogin.isChecked())
			{
				mHttpsLogin.setSummary("当前以 HTTPS 登录");
			}
			else
			{
				mHttpsLogin.setSummary("当前以 HTTP 登录");
			}
			return true;
		}
		else if (preference == mLoadImage)
		{
			UIHelper.changeSettingIsLoadImage(mContext, mLoadImage.isChecked());
			if(mLoadImage.isChecked())
			{
				mLoadImage.setSummary("页面加载图片 (默认在WIFI网络下加载图片)");
			}
			else
			{
				mLoadImage.setSummary("页面不加载图片 (默认在WIFI网络下加载图片)");
			}
			return true;
		}
		else if (preference == mScroll)
		{
			UIHelper.setConfigScroll(mContext, mScroll.isChecked());
			if(mScroll.isChecked())
			{
				mScroll.setSummary("已启用左右滑动");
			}
			else
			{
				mScroll.setSummary("已关闭左右滑动");
			}
			return true;
		}
		else if (preference == mVoice)
		{
			UIHelper.setConfigVoice(mContext, mVoice.isChecked());
			if(mVoice.isChecked())
			{
				mVoice.setSummary("已开启提示声音");
			}
			else
			{
				mVoice.setSummary("已关闭提示声音");
			}
			return true;
		}
		else if (preference == mAccount)
		{
			UIHelper.loginOrLogout(mContext);
			mAccount.setTitle("用户登录");
			return true;
		}
		else if (preference == mMyInfo)
		{
			UIHelper.showUserInfo(mContext);
			return true;
		}
		else if (preference == mCache)
		{
			UIHelper.clearAppCache(mContext);
			mCache.setSummary("0KB");
			return true;
		}
		else if (preference == mFeedback)
		{
			Intent intent = new Intent(OSChinaSettingActivity.this, OSChinaFeedBackActivity.class);
			startActivity(intent);
			return true;
		}
		else if (preference == mAbout)
		{
			Intent intent = new Intent(OSChinaSettingActivity.this, OSChinaAboutActivity.class);
			startActivity(intent);
			return true;
		}
		return false;
	}	
	
	public void back(View paramView)
	{
		finish();
	}
	
	@Override
	protected void onNewIntent(Intent intent) 
	{
		super.onNewIntent(intent);
		
		if(intent.getBooleanExtra("LOGIN", false))
		{
			mAccount.setTitle("注销登录");
		}				
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		//结束Activity&从堆栈中移除
		OSChinaAppManager.getAppManager().finishActivity(this);
	}
}
