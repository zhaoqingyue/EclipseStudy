/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaCommentPubActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 发表评论
 * @author: zhaoqy
 * @date: 2015-11-24 上午11:46:04
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.CommentList;
import com.zhaoqy.app.demo.oschina.item.Result;
import com.zhaoqy.app.demo.oschina.util.StringUtils;
import com.zhaoqy.app.demo.oschina.view.LinkView;

@SuppressLint("HandlerLeak")
public class OSChinaCommentPubActivity extends OSChinaBaseActivity implements OnClickListener
{
	public final static int CATALOG_NEWS = 1;
	public final static int CATALOG_POST = 2;
	public final static int CATALOG_TWEET = 3;
	public final static int CATALOG_ACTIVE = 4;
	public final static int CATALOG_MESSAGE = 4;
	public final static int CATALOG_BLOG = 5;
	
	private ProgressDialog mProgress;
	private Context   mContext;
	private ImageView mBack;
	private EditText  mContent;
	private CheckBox  mZone;
	private Button    mPublish;
	private LinkView  mQuote;
	private String    mContentText;
	private int       mCatalog;
	private int       mId;
	private int       mUid;
	private int       mIsPostToMyZone;
	private int       mReplyId;
	private int       mAuthorId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oschina_comment_pub);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView()
    {
    	mBack = (ImageView)findViewById(R.id.comment_list_back);
    	mPublish = (Button)findViewById(R.id.comment_pub_publish);
    	mContent = (EditText)findViewById(R.id.comment_pub_content);
    	mZone = (CheckBox)findViewById(R.id.comment_pub_zone);
    	mQuote = (LinkView)findViewById(R.id.comment_pub_quote);
    }
	
	private void setListener() 
    {
    	mBack.setOnClickListener(this);
    	mPublish.setOnClickListener(this);    	
	}
	
	private void initData() 
	{
		mId = getIntent().getIntExtra("id", 0);
		mCatalog = getIntent().getIntExtra("catalog", 0);
		mReplyId = getIntent().getIntExtra("reply_id", 0);
		mAuthorId = getIntent().getIntExtra("author_id", 0);
		
		if(mCatalog == CommentList.CATALOG_TWEET)
		{
    		mZone.setVisibility(View.VISIBLE);
    	}
		mQuote.setText(UIHelper.parseQuoteSpan(getIntent().getStringExtra("author"),getIntent().getStringExtra("content")));
    	mQuote.parseLinkText();
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.comment_list_back:
		{
			finish();
			break;
		}
		case R.id.comment_pub_publish:
		{
			mContentText = mContent.getText().toString();
			if(StringUtils.isEmpty(mContentText))
			{
				UIHelper.ToastMessage(v.getContext(), "请输入评论内容");
				return;
			}
			
			if(!UserHelper.isLogin())
			{
				UIHelper.showLoginDialog(mContext);
				return;
			}
			
			if(mZone.isChecked())
			{
				mIsPostToMyZone = 1;
			}
				
			mUid = UserHelper.getLoginUid();
	    	mProgress = ProgressDialog.show(v.getContext(), null, "发表中···", true, true); 			
			
			new Thread()
			{
				public void run() 
				{
					Message msg = new Message();
					Result res = new Result();
					try 
					{
						//发表评论
						if(mReplyId == 0)
						{
							res = UserHelper.pubComment(mContext, mCatalog, mId, mUid, mContentText, mIsPostToMyZone);
						}
						//对评论进行回复
						else if(mReplyId > 0)
						{
							if(mCatalog == CATALOG_BLOG)
							{
								res = UserHelper.replyBlogComment(mContext, mId, mUid, mContentText, mReplyId, mAuthorId);
							}
							else
							{
								res = UserHelper.replyComment(mContext, mId, mCatalog, mReplyId, mAuthorId, mUid, mContentText);
							}
						}
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
					//返回刚刚发表的评论
					Intent intent = new Intent();
					intent.putExtra("COMMENT_SERIALIZABLE", res.getComment());
					setResult(RESULT_OK, intent);
					//跳转到文章详情
					finish();
				}
			}
		}
	};
}
