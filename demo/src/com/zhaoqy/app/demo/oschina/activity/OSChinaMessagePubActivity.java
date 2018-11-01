/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaMessagePubActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 发表留言
 * @author: zhaoqy
 * @date: 2015-11-24 下午3:22:10
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.common.AppConfig;
import com.zhaoqy.app.demo.oschina.common.AppProperty;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.Result;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class OSChinaMessagePubActivity extends OSChinaBaseActivity implements OnClickListener
{
	private InputMethodManager mInputMethodManager;
	private ProgressDialog mProgress;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mReceiver;
	private EditText  mContent;
	private Button    mPublish;
    private String    mMessageKey = AppConfig.TEMP_MESSAGE;
    private String    mFriendName;
	private String    mContentText;
	private int       mUId;
	private int       mFriendid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oschina_message_pub);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
    private void initView()
    {
    	mBack = (ImageView)findViewById(R.id.message_pub_back);
    	mPublish = (Button)findViewById(R.id.message_pub_publish);
    	mContent = (EditText)findViewById(R.id.message_pub_content);
    	mReceiver = (TextView)findViewById(R.id.message_pub_receiver);
    } 
    
    private void setListener() 
    {
    	mBack.setOnClickListener(this);
    	mPublish.setOnClickListener(this);
	}
    
    private void initData() 
    {
    	mInputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
    	
		mUId = getIntent().getIntExtra("user_id", 0);
		mFriendid = getIntent().getIntExtra("friend_id", 0);
		mFriendName = getIntent().getStringExtra("friend_name");
    	
		if(mFriendid > 0) mMessageKey = AppConfig.TEMP_MESSAGE + "_" + mFriendid;
		
		//编辑器添加文本监听
    	mContent.addTextChangedListener(UIHelper.getTextWatcher(this, mMessageKey));
    	
    	//显示临时编辑内容
    	UIHelper.showTempEditContent(this, mContent, mMessageKey);
    	
    	mReceiver.setText("发送留言给  " + mFriendName);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.message_pub_back:
		{
			finish();
			break;
		}
		case R.id.message_pub_publish:
		{
			//隐藏软键盘
			mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);  
			mContentText = mContent.getText().toString();
			if(StringUtils.isEmpty(mContentText))
			{
				UIHelper.ToastMessage(v.getContext(), "请输入留言内容");
				return;
			}
			
			if(!UserHelper.isLogin())
			{
				UIHelper.showLoginDialog(mContext);
				return;
			}
			
			mProgress = ProgressDialog.show(v.getContext(), null, "发送中···",true,true); 
			final Handler handler = new Handler()
			{
				public void handleMessage(Message msg) 
				{
					
					if(mProgress!=null) mProgress.dismiss();
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
							//清除之前保存的编辑内容
							AppProperty.removeProperty(mContext, mMessageKey);
							//返回刚刚发表的评论
							Intent intent = new Intent();
							intent.putExtra("COMMENT_SERIALIZABLE", res.getComment());
							setResult(RESULT_OK, intent);
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
						Result res = UserHelper.pubMessage(mContext, mUId, mFriendid, mContentText);
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
