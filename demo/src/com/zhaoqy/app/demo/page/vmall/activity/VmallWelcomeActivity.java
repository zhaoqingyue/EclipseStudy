/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: VmallWelcomeActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.vmall.activity
 * @Description: 华为商城欢迎页
 * @author: zhaoqy
 * @date: 2015-12-17 下午8:52:02
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.vmall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.zhaoqy.app.demo.R;

public class VmallWelcomeActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vmall_welcome);

		new Handler().postDelayed(new Runnable() 
		{  
            public void run() 
            {  
                Intent intent = new Intent(VmallWelcomeActivity.this, VmallMainActivity.class);  
                startActivity(intent);
                finish();  
            }  
        }, 1500);
	}
}
