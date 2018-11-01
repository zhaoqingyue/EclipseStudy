/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinSetUpSettingsActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 设置类
 * @author: zhaoqy
 * @date: 2015-11-9 下午2:21:12
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import com.zhaoqy.app.demo.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class MenuKaixinSetUpSettingsActivity extends PreferenceActivity implements OnPreferenceClickListener
{
	private Preference mMessageRefreshInterval;
	private Preference mNewMessageDetectInterval;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.kaixin_settings);
		findViewByKey();
		setListener();
	}

	@SuppressWarnings("deprecation")
	private void findViewByKey() 
	{
		mMessageRefreshInterval = (Preference) findPreference("message_refresh_interval");
		mNewMessageDetectInterval = (Preference) findPreference("new_message_detect_interval");
	}

	private void setListener() 
	{
		mMessageRefreshInterval.setOnPreferenceClickListener(this);
		mNewMessageDetectInterval.setOnPreferenceClickListener(this);
	}

	public boolean onPreferenceClick(Preference preference) 
	{
		if (preference == mMessageRefreshInterval) 
		{
			RefreshDialog();
		}
		else if (preference == mNewMessageDetectInterval) 
		{
			DetectDialog();
		}
		return false;
	}

	/**
	 * 刷新间隔对话框
	 */
	private void RefreshDialog() 
	{
		final String[] items = { "半小时", "1小时", "2小时", "4小时" };
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("刷新间隔");
		builder.setSingleChoiceItems(items, 0, new OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				mMessageRefreshInterval.setSummary(items[which]);
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 新消息检测时间间隔对话框
	 */
	private void DetectDialog() 
	{
		final String[] items = { "1分钟", "10分钟", "30分钟", "1小时", "2小时", "4小时" };
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("新消息检测时间间隔");
		builder.setSingleChoiceItems(items, 0, new OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				mNewMessageDetectInterval.setSummary(items[which]);
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
}
