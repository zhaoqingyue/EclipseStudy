/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaMessageForwardActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 转发留言
 * @author: zhaoqy
 * @date: 2015-11-24 下午3:23:52
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.Result;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

@SuppressLint("HandlerLeak")
public class OSChinaMessageForwardActivity extends OSChinaBaseActivity implements OnClickListener
{
    private InputMethodManager mInputMethodManager;
    private ProgressDialog mProgress;
	private Context   mContext;
	private ImageView mBack;
	private EditText  mReceiver;
	private EditText  mContent;
	private Button    mPublish;
	private String    mContentText;
	private String    mReceiverText;
	private int       mUId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oschina_message_forward);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

    private void initView()
    {
    	mBack = (ImageView)findViewById(R.id.message_forward_back);
    	mPublish = (Button)findViewById(R.id.message_forward_publish);
    	mContent = (EditText)findViewById(R.id.message_forward_content);
    	mReceiver = (EditText)findViewById(R.id.message_forward_receiver);
    } 
    
    private void setListener() 
    {
    	mBack.setOnClickListener(this);
    	mPublish.setOnClickListener(this);
	}
    
    private void initData() 
    {
    	mInputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
    	String friend_name = "@" + getIntent().getStringExtra("friend_name") + " ";
		mUId = getIntent().getIntExtra("user_id", 0);
		mContentText = friend_name + getIntent().getStringExtra("message_content");
    	mContent.setText(mContentText);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.message_forward_back:
		{
			finish();
			break;
		}
		case R.id.message_forward_publish:
		{
			//隐藏软键盘
			mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);  
			mContentText = mContent.getText().toString();
			mReceiverText = mReceiver.getText().toString();
			if(StringUtils.isEmpty(mContentText))
			{
				UIHelper.ToastMessage(v.getContext(), "请输入留言内容");
				return;
			}
			if(StringUtils.isEmpty(mReceiverText))
			{
				UIHelper.ToastMessage(v.getContext(), "请输入对方的昵称");
				return;
			}
			if(!UserHelper.isLogin())
			{
				UIHelper.showLoginDialog(mContext);
				return;
			}
			
			mProgress = ProgressDialog.show(v.getContext(), null, "发送中···", true, true); 
			final Handler handler = new Handler()
			{
				public void handleMessage(Message msg) 
				{
					if(mProgress!=null)mProgress.dismiss();
					if(msg.what == 1)
					{
						Result res = (Result)msg.obj;
						UIHelper.ToastMessage(mContext, res.getErrorMessage());
						if(res.OK())
						{
							//发送通知广播
							if(res.getNotice() != null)
							{
								UIHelper.sendBroadCast(mContext, res.getNotice());
							}
							finish();
						}
					}
				}
			};
			new Thread()
			{
				public void run() 
				{
					Message msg =new Message();
					try 
					{
						Result res = UserHelper.forwardMessage(mContext, mUId, mReceiverText, mContentText);
						msg.what = 1;
						msg.obj = res;
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
			break;
		}
		default:
			break;
		}
	}
}
