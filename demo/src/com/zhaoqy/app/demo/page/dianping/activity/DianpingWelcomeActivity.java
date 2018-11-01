/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: DianpingWelcomeActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.dianping.activity
 * @Description: 大众点评欢迎页
 * @author: zhaoqy
 * @date: 2015-12-16 下午2:56:14
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.dianping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.dianping.util.SharedUtil;

public class DianpingWelcomeActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dianping_welcome);
		new Handler(new Handler.Callback()
		{
			@Override
			public boolean handleMessage(Message message) 
			{
				if(SharedUtil.isFirstStart(getBaseContext()))
				{
					startActivity(new Intent(getApplicationContext(), DianpingGuideActivity.class));
					SharedUtil.putIsFirstStart(getBaseContext(), false);	
				}
				else
				{
					startActivity(new Intent(getApplicationContext(), DianpingMainActivity.class));
				}
				finish();
				return false;
			}
		}).sendEmptyMessageDelayed(0, 1500);
	}
}
