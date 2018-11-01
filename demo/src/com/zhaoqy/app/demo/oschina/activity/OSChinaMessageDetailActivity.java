/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaMessageDetailActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 留言详情
 * @author: zhaoqy
 * @date: 2015-11-24 下午3:19:44
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.adapter.MessageDetailAdapter;
import com.zhaoqy.app.demo.oschina.common.AppConfig;
import com.zhaoqy.app.demo.oschina.common.AppProperty;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.Comment;
import com.zhaoqy.app.demo.oschina.item.CommentList;
import com.zhaoqy.app.demo.oschina.item.Notice;
import com.zhaoqy.app.demo.oschina.item.Result;
import com.zhaoqy.app.demo.oschina.util.StringUtils;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

@SuppressLint("HandlerLeak")
public class OSChinaMessageDetailActivity extends OSChinaBaseActivity implements OnClickListener, OnFocusChangeListener, OnKeyListener, OnScrollListener, OnItemLongClickListener, OnRefreshListener
{
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	
	private InputMethodManager    mInputMethodManager;
	private PullToRefreshListView mComment;
	private MessageDetailAdapter  mCommentAdapter;
	private List<Comment>  mCommentData = new ArrayList<Comment>();
	private Context        mContext;
	private ImageView      mBack;
	private ImageView      mRefresh;
	private TextView       mHeadTitle;
	private ProgressBar    mProgressbar;
	private View           mCommentFooter;
	private TextView       mCommentFootMore;
	private ProgressBar    mCommentFootProgress;
	private ProgressDialog mProgress;
	private ViewSwitcher   mFootViewSwitcher;
	private ImageView      mFootEditebox;
	private EditText       mFootEditer;
	private Button         mFootPubcomment;
	private String         mMessageKey = AppConfig.TEMP_MESSAGE;
	private String         mCurFriendName;
	private String         mContentText;
	private int            mSumData;
	private int            mCurFriendId;
	private int            mCurCatalog;
	private int            mCurDataState;
	private int            mUId;
	private int            mFriendid;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oschina_message_detail);
        mContext = this;
        
        initView();        
        setListener();
        initData();        
    }
    
    private void initView()
    {
    	mBack = (ImageView)findViewById(R.id.message_detail_back);
    	mRefresh = (ImageView)findViewById(R.id.message_detail_refresh);
    	mHeadTitle = (TextView)findViewById(R.id.message_detail_head_title);
    	mProgressbar = (ProgressBar)findViewById(R.id.message_detail_head_progress);
    	
    	mFootViewSwitcher = (ViewSwitcher)findViewById(R.id.message_detail_foot_viewswitcher);
    	mFootPubcomment = (Button)findViewById(R.id.message_detail_foot_pubcomment);
    	mFootEditebox = (ImageView)findViewById(R.id.message_detail_footbar_editebox);
    	mFootEditer = (EditText)findViewById(R.id.message_detail_foot_editer);
    	mCommentFooter = getLayoutInflater().inflate(R.layout.view_oschina_listview_footer, null);
    	mCommentFootMore = (TextView)mCommentFooter.findViewById(R.id.listview_foot_more);
        mCommentFootProgress = (ProgressBar)mCommentFooter.findViewById(R.id.listview_foot_progress);
    	mComment = (PullToRefreshListView)findViewById(R.id.message_list_listview);
    }
    
    private void setListener() 
    {
    	mBack.setOnClickListener(this);
    	mRefresh.setOnClickListener(this);
    	mFootEditebox.setOnClickListener(this);
    	mFootEditer.setOnFocusChangeListener(this); 
    	mFootEditer.setOnKeyListener(this);
        mComment.setOnScrollListener(this);
        mComment.setOnItemLongClickListener(this);
        mComment.setOnRefreshListener(this);
    	mFootPubcomment.setOnClickListener(this);
	}

	private void initData()
	{		
		mInputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE); 
		mCurFriendId = getIntent().getIntExtra("friend_id", 0);
		mCurFriendName = getIntent().getStringExtra("friend_name");
		mCurCatalog = CommentList.CATALOG_MESSAGE;
		if(mCurFriendId > 0) mMessageKey = AppConfig.TEMP_MESSAGE + "_" + mCurFriendId;
		
		//编辑器添加文本监听
    	mFootEditer.addTextChangedListener(UIHelper.getTextWatcher(this, mMessageKey));
    	//显示临时编辑内容
    	UIHelper.showTempEditContent(mContext, mFootEditer, mMessageKey);
    	mHeadTitle.setText(getString(R.string.message_detail_head_title, mCurFriendName));
    	mCommentAdapter = new MessageDetailAdapter(mContext, mCommentData, R.layout.item_oschina_message_detail); 
        mComment.addFooterView(mCommentFooter);
        mComment.setAdapter(mCommentAdapter); 
        loadCommentData(mCurFriendId,mCurCatalog,0,mHandler,UIHelper.LISTVIEW_ACTION_INIT);
    }
	
    /**
     * 线程加载评论数据
     * @param id 当前文章id
     * @param catalog 分类
     * @param pageIndex 当前页数
     * @param handler 处理器
     * @param action 动作标识
     */
	private void loadCommentData(final int id,final int catalog,final int pageIndex,final Handler handler,final int action)
	{  
		headButtonSwitch(DATA_LOAD_ING);
		new Thread()
		{
			public void run() 
			{
				Message msg = new Message();
				boolean isRefresh = false;
				if(action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL)
				{
					isRefresh = true;
				}
					
				try 
				{
					CommentList commentlist = UserHelper.getCommentList(mContext, catalog, id, pageIndex, isRefresh);				
					msg.what = commentlist.getPageSize();
					msg.obj = commentlist;
	            } 
				catch (Exception e) 
				{
	            	e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }
				msg.arg1 = action;
                handler.sendMessage(msg);
			}
		}.start();
	} 

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.message_detail_back:
		{
			finish();
			break;
		}
		case R.id.message_detail_refresh:
		{
			loadCommentData(mCurFriendId,mCurCatalog,0,mHandler,UIHelper.LISTVIEW_ACTION_REFRESH);
			break;
		}
		case R.id.message_detail_footbar_editebox:
		{
			mFootViewSwitcher.showNext();
			mFootEditer.setVisibility(View.VISIBLE);
			mFootEditer.requestFocus();
			mFootEditer.requestFocusFromTouch();
			break;
		}
		case R.id.message_detail_foot_pubcomment:
		{
			if(!UserHelper.isLogin())
			{
				UIHelper.showLoginDialog(mContext);
				return;
			}
			
			mUId = UserHelper.getLoginUid();
			mFriendid = mCurFriendId;
			if(mUId==0 || mFriendid==0) return;
			mContentText = mFootEditer.getText().toString();
			if(StringUtils.isEmpty(mContentText))
			{
				UIHelper.ToastMessage(v.getContext(), "请输入留言内容");
				return;
			}
			
			mProgress = ProgressDialog.show(v.getContext(), null, "发送中···", true, true); 
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
							//恢复初始底部栏
							mFootViewSwitcher.setDisplayedChild(0);
							mFootEditer.clearFocus();
							mFootEditer.setText("");
							mFootEditer.setVisibility(View.GONE);
							//显示刚刚发送的留言
				        	mCommentData.add(0, res.getComment());
				        	mCommentAdapter.notifyDataSetChanged();
				        	mComment.setSelection(0);
				        	//清除之前保存的编辑内容
				        	AppProperty.removeProperty(mContext, mMessageKey);
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

	@Override
	public void onFocusChange(View v, boolean hasFocus) 
	{
		if(hasFocus)
		{  
			mInputMethodManager.showSoftInput(v, 0);  
        }  
        else
        {  
            mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);  
        }  
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if(mFootViewSwitcher.getDisplayedChild() == 1)
			{
				mFootViewSwitcher.setDisplayedChild(0);
				mFootEditer.clearFocus();
				mFootEditer.setVisibility(View.GONE);
			}
			return true;
		}
		return false;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) 
	{
		mComment.onScrollStateChanged(view, scrollState);
		//数据为空--不用继续下面代码了
		if(mCommentData.size() == 0) return;
		//判断是否滚动到底部
		boolean scrollEnd = false;
		try 
		{
			if(view.getPositionForView(mCommentFooter) == view.getLastVisiblePosition())
			{
				scrollEnd = true;
			}
		} 
		catch (Exception e) 
		{
			scrollEnd = false;
		}
		
		if(scrollEnd && mCurDataState==UIHelper.LISTVIEW_DATA_MORE)
		{
			mComment.setTag(UIHelper.LISTVIEW_DATA_LOADING);
			mCommentFootMore.setText(R.string.load_ing);
			mCommentFootProgress.setVisibility(View.VISIBLE);
			//当前pageIndex
			int pageIndex = mSumData/20;
			loadCommentData(mCurFriendId, mCurCatalog, pageIndex, mHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
	{
		mComment.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
	{
		//点击头部、底部栏无效
		if(position == 0 || view == mCommentFooter) return false;
		TextView username = (TextView)view.findViewById(R.id.messagedetail_listitem_username);
		final Comment com = (Comment)username.getTag();
		
		//操作--删除         		
		final Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) 
			{
				if(msg.what == 1)
				{
					Result res = (Result)msg.obj;
					if(res.OK())
					{
						mSumData--;
						mCommentData.remove(com);
						mCommentAdapter.notifyDataSetChanged();
					}
					UIHelper.ToastMessage(mContext, res.getErrorMessage());
				}
			}        			
		};
		//留言--当成评论来删除
		final Thread thread = new Thread()
		{
			public void run() 
			{
				Message msg = new Message();
				try 
				{
					Result res = UserHelper.delComment(mContext, mCurFriendId, mCurCatalog, com.getId(), com.getAuthorId());
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
		};
		UIHelper.showMessageDetailOptionDialog(OSChinaMessageDetailActivity.this, com, thread);
		return true;
	}

	@Override
	public void onRefresh() 
	{
		loadCommentData(mCurFriendId, mCurCatalog, 0, mHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
	}
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{ 
		if (resultCode != RESULT_OK) return;   
    	if (data == null) return;
        if (requestCode == UIHelper.REQUEST_CODE_FOR_RESULT) 
        { 
        	Comment comm = (Comment)data.getSerializableExtra("COMMENT_SERIALIZABLE");
        	mCommentData.add(0, comm);
        	mCommentAdapter.notifyDataSetChanged();
        	mComment.setSelection(0);
        }
	}
	
	private void headButtonSwitch(int type) 
    {
    	switch (type) 
    	{
		case DATA_LOAD_ING:
		{
			mProgressbar.setVisibility(View.VISIBLE);
			mRefresh.setVisibility(View.GONE);
			break;
		}
		case DATA_LOAD_COMPLETE:
		{
			mProgressbar.setVisibility(View.GONE);
			mRefresh.setVisibility(View.VISIBLE);
			break;
		}
		default:
			break;
		}
    }
	
	private Handler mHandler = new Handler()
	{
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) 
		{
			headButtonSwitch(DATA_LOAD_COMPLETE);
			if(msg.what >= 0)
			{						
				CommentList list = (CommentList)msg.obj;
				Notice notice = list.getNotice();
				//处理listview数据
				switch (msg.arg1) 
				{
				case UIHelper.LISTVIEW_ACTION_INIT:
				case UIHelper.LISTVIEW_ACTION_REFRESH:
				{
					mSumData = msg.what;
					//先清除原有数据
					mCommentData.clear();
					mCommentData.addAll(list.getCommentlist());
					break;
				}	
				case UIHelper.LISTVIEW_ACTION_SCROLL:
				{
					mSumData += msg.what;
					if(mCommentData.size() > 0)
					{
						for(Comment com1 : list.getCommentlist())
						{
							boolean b = false;
							for(Comment com2 : mCommentData)
							{
								if(com1.getId() == com2.getId() && com1.getAuthorId() == com2.getAuthorId())
								{
									b = true;
									break;
								}
							}
							if(!b) mCommentData.add(com1);
						}
					}
					else
					{
						mCommentData.addAll(list.getCommentlist());
					}
					break;
				}
				default:
					break;
				}	
				
				if(msg.what < 20)
				{
					mCurDataState = UIHelper.LISTVIEW_DATA_FULL;
					mCommentAdapter.notifyDataSetChanged();
					mCommentFootMore.setText(R.string.load_full);
				}
				else if(msg.what == 20)
				{					
					mCurDataState = UIHelper.LISTVIEW_DATA_MORE;
					mCommentAdapter.notifyDataSetChanged();
					mCommentFootMore.setText(R.string.load_more);
				}
				//发送通知广播
				if(notice != null)
				{
					UIHelper.sendBroadCast(mContext, notice);
				}
			}
			else if(msg.what == -1)
			{
				//有异常--也显示更多 & 弹出错误消息
				mCurDataState = UIHelper.LISTVIEW_DATA_MORE;
				mCommentFootMore.setText(R.string.load_more);
			}
			
			if(mCommentData.size()==0)
			{
				mCurDataState = UIHelper.LISTVIEW_DATA_EMPTY;
				mCommentFootMore.setText(R.string.load_empty);
			}
			mCommentFootProgress.setVisibility(View.GONE);
			if(msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH)
			{
				mComment.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
			}
		}
	};
}
