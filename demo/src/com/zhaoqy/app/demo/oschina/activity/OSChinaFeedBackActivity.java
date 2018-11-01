/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaFeedBackActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: oschina用户反馈
 * @author: zhaoqy
 * @date: 2015-11-17 下午2:50:00
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

public class OSChinaFeedBackActivity extends OSChinaBaseActivity implements OnClickListener
{
	private Context     mContext;
	private ImageButton mClose;
	private EditText    mEditer;
	private Button      mPublish;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oschina_feedback);
		mContext = this;
		
		initView();
		setListener();
	}
	
	private void initView()
    {
    	mClose = (ImageButton)findViewById(R.id.feedback_close);
    	mEditer = (EditText)findViewById(R.id.feedback_content);
    	mPublish = (Button)findViewById(R.id.feedback_publish);
    }
	
	private void setListener() 
    {
    	mClose.setOnClickListener(this);
    	mPublish.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.feedback_close:
		{
			finish();
			break;
		}
		case R.id.feedback_content:
		{
			String content = mEditer.getText().toString();
			
			if(StringUtils.isEmpty(content)) 
			{
				Toast.makeText(mContext, "反馈信息不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Intent intent = new Intent(Intent.ACTION_SEND);  
			//i.setType("text/plain");         //模拟器
			intent.setType("message/rfc822") ; //真机
			intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ld@oschina.net"});  
			intent.putExtra(Intent.EXTRA_SUBJECT,"用户反馈-Android客户端");  
			intent.putExtra(Intent.EXTRA_TEXT,content);  
			startActivity(Intent.createChooser(intent, "Sending mail..."));
			finish();
			break;
		}
		default:
			break;
		}
	}
}
