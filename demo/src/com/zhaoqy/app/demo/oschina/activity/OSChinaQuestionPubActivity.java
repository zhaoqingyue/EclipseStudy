/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaQuestionPubActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 发表帖子
 * @author: zhaoqy
 * @date: 2015-11-24 上午9:49:35
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.common.AppConfig;
import com.zhaoqy.app.demo.oschina.common.AppProperty;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.Post;
import com.zhaoqy.app.demo.oschina.item.Result;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

@SuppressLint("HandlerLeak")
public class OSChinaQuestionPubActivity extends OSChinaBaseActivity implements OnClickListener, OnItemSelectedListener
{
	private InputMethodManager mInputMethodManager;
	private ProgressDialog     mProgress;
	private Context   mContext;
	private ImageView mBack;
	private EditText  mTitle;
	private EditText  mContent;
	private Spinner   mCatalog;
	private CheckBox  mEmail;
	private Button    mPublish;
	private Post      mPost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oschina_question_pub);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView()
    {    	
    	mBack = (ImageView)findViewById(R.id.question_pub_back);
    	mPublish = (Button)findViewById(R.id.question_pub_publish);
    	mTitle = (EditText)findViewById(R.id.question_pub_title);
    	mContent = (EditText)findViewById(R.id.question_pub_content);
    	mEmail = (CheckBox)findViewById(R.id.question_pub_email);
    	mCatalog = (Spinner)findViewById(R.id.question_pub_catalog);
    }
    
    private void setListener() 
    {
    	mBack.setOnClickListener(this);
    	mPublish.setOnClickListener(this);
    	mCatalog.setOnItemSelectedListener(this);
	}
    
    private void initData() 
    {
    	mInputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
    	//显示临时编辑内容
    	UIHelper.showTempEditContent(mContext, mTitle, AppConfig.TEMP_POST_TITLE);
    	UIHelper.showTempEditContent(mContext, mContent, AppConfig.TEMP_POST_CONTENT);
    	//显示临时选择分类
    	String position = AppProperty.getProperty(mContext, AppConfig.TEMP_POST_CATALOG);
    	mCatalog.setSelection(StringUtils.toInt(position, 0));
    	//编辑器添加文本监听
    	mTitle.addTextChangedListener(UIHelper.getTextWatcher(mContext, AppConfig.TEMP_POST_TITLE));
    	mContent.addTextChangedListener(UIHelper.getTextWatcher(mContext, AppConfig.TEMP_POST_CONTENT));
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.question_pub_back:
		{
			finish();
			break;
		}
		case R.id.question_pub_publish:
		{
			//隐藏软键盘
			mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);  
			String title = mTitle.getText().toString();
			if(StringUtils.isEmpty(title))
			{
				UIHelper.ToastMessage(v.getContext(), "请输入标题");
				return;
			}
			String content = mContent.getText().toString();
			if(StringUtils.isEmpty(content))
			{
				UIHelper.ToastMessage(v.getContext(), "请输入提问内容");
				return;
			}
			if(!UserHelper.isLogin())
			{
				UIHelper.showLoginDialog(mContext);
				return;
			}
			
			mProgress = ProgressDialog.show(v.getContext(), null, "发布中···",true,true); 
			mPost = new Post();
			mPost.setAuthorId(UserHelper.getLoginUid());
			mPost.setTitle(title);
			mPost.setBody(content);
			mPost.setCatalog(mCatalog.getSelectedItemPosition()+1);
			if(mEmail.isChecked())
			{
				mPost.setIsNoticeMe(1);
			}
			
			new Thread()
			{
				public void run() 
				{
					Message msg = new Message();					
					try 
					{
						Result res = UserHelper.pubPost(mContext, mPost);
						msg.what = 1;
						msg.obj = res;
		            } 
					catch (Exception e) 
					{
		            	e.printStackTrace();
						msg.what = -1;
						msg.obj = e;
		            }
					mHandler.sendMessage(msg);
				}
			}.start();
			break;
		}
		default:
			break;
		}
	}
	
	final Handler mHandler = new Handler()
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
					//清除之前保存的编辑内容
					AppProperty.removeProperty(mContext, AppConfig.TEMP_POST_TITLE,AppConfig.TEMP_POST_CATALOG,AppConfig.TEMP_POST_CONTENT);
					//跳转到文章详情
					finish();
				}
			}
		}
	};

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
	{
		//保存临时选择的分类
		AppProperty.setProperty(mContext, AppConfig.TEMP_POST_CATALOG, position + "");
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) 
	{
	}
}
