/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaBaseActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: oschina基类
 * @author: zhaoqy
 * @date: 2015-11-17 下午2:14:51
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import android.app.Activity;
import android.os.Bundle;

import com.zhaoqy.app.demo.oschina.helper.OSChinaAppManager;

public class OSChinaBaseActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		//添加Activity到堆栈
		OSChinaAppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		
		//结束Activity&从堆栈中移除
		OSChinaAppManager.getAppManager().finishActivity(this);
	}
}
