/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaQuestionDetailActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 问答详情
 * @author: zhaoqy
 * @date: 2015-11-23 下午4:27:26
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.animation.BadgeView;
import com.zhaoqy.app.demo.oschina.adapter.CommentAdapter;
import com.zhaoqy.app.demo.oschina.common.AppConfig;
import com.zhaoqy.app.demo.oschina.common.AppProperty;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.Comment;
import com.zhaoqy.app.demo.oschina.item.CommentList;
import com.zhaoqy.app.demo.oschina.item.FavoriteList;
import com.zhaoqy.app.demo.oschina.item.Notice;
import com.zhaoqy.app.demo.oschina.item.Post;
import com.zhaoqy.app.demo.oschina.item.Result;
import com.zhaoqy.app.demo.oschina.util.CacheUtils;
import com.zhaoqy.app.demo.oschina.util.NetUtils;
import com.zhaoqy.app.demo.oschina.util.StringUtils;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

@SuppressLint("HandlerLeak")
public class OSChinaQuestionDetailActivity extends OSChinaBaseActivity implements OnClickListener, OnFocusChangeListener, OnKeyListener, OnItemClickListener, OnScrollListener, OnItemLongClickListener, OnRefreshListener
{
	private final static int VIEWSWITCH_TYPE_DETAIL = 0x001;
	private final static int VIEWSWITCH_TYPE_COMMENTS = 0x002;
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private final static int DATA_LOAD_FAIL = 0x003;
	
	private InputMethodManager    mInputMethodManager;
	private PullToRefreshListView mComment;
	private CommentAdapter  mCommentAdapter;
	private List<Comment>   mCommentData = new ArrayList<Comment>();
	private ProgressDialog  mProgress;
	private GestureDetector mGestureDetector;
	private Context      mContext;
	private FrameLayout  mHeader;
	private LinearLayout mFooter;
	private ImageView    mBack;
	private ImageView    mFavorite;
	private ImageView    mRefresh;
	private TextView     mHeadTitle;
	private ProgressBar  mProgressbar;
	private ScrollView   mScrollView;
    private ViewSwitcher mViewSwitcher;
	private BadgeView    mBvComment;
	private ImageView    mDetail;
	private ImageView    mCommentList;
	private ImageView    mShare;
	private TextView     mTitle;
	private TextView     mAuthor;
	private TextView     mPubDate;
	private TextView     mCommentCount;
	private WebView      mWebView;
    private Post         mPostDetail;
    private ViewSwitcher mFootViewSwitcher;
	private ImageView    mFootEditebox;
	private EditText     mFootEditer;
	private Button       mFootPubcomment;	
	private View         mCommentFooter;
	private TextView     mCommentFootMore;
	private ProgressBar  mCommentFootProgress;
	private String       mCommentKey = AppConfig.TEMP_COMMENT;
	private String       mContentText;
	private boolean      mIsFullScreen;
	private int          mPostId;
    private int		     mSumData;
    private int 	     mCurId;
	private int 		 mCurCatalog;	
	private int 		 mCurDataState;
	private int 		 mCurPosition;
	private int 		 mCatalog;
	private int 		 mId;
	private int 		 mUId;
	private int 		 mIsPostToMyZone;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oschina_question_detail);
        mContext = this;
        
        initView();  
        setListener();
        initData();
    	initCommentView();
    	initCommentData();
    	//注册双击全屏事件
    	regOnDoubleEvent();
    }

    private void initView()
    {
    	mHeader = (FrameLayout)findViewById(R.id.question_detail_header);
    	mFooter = (LinearLayout)findViewById(R.id.question_detail_footer);
    	mBack = (ImageView)findViewById(R.id.question_detail_back);
    	mRefresh = (ImageView)findViewById(R.id.question_detail_refresh);
    	mHeadTitle = (TextView)findViewById(R.id.question_detail_head_title);
    	mProgressbar = (ProgressBar)findViewById(R.id.question_detail_head_progress);
    	mViewSwitcher = (ViewSwitcher)findViewById(R.id.question_detail_viewswitcher);
    	mScrollView = (ScrollView)findViewById(R.id.question_detail_scrollview);
    	
    	mDetail = (ImageView)findViewById(R.id.question_detail_footbar_detail);
    	mCommentList = (ImageView)findViewById(R.id.question_detail_footbar_commentlist);
    	mFavorite = (ImageView)findViewById(R.id.question_detail_footbar_favorite);
    	mShare = (ImageView)findViewById(R.id.question_detail_footbar_share);
    	
    	mTitle = (TextView)findViewById(R.id.question_detail_title);
    	mAuthor = (TextView)findViewById(R.id.question_detail_author);
    	mPubDate = (TextView)findViewById(R.id.question_detail_date);
    	mCommentCount = (TextView)findViewById(R.id.question_detail_commentcount);
    	mWebView = (WebView)findViewById(R.id.question_detail_webview);
    	
    	mFootViewSwitcher = (ViewSwitcher)findViewById(R.id.question_detail_foot_viewswitcher);
    	mFootPubcomment = (Button)findViewById(R.id.question_detail_foot_pubcomment);
    	mFootEditebox = (ImageView)findViewById(R.id.question_detail_footbar_editebox);
    	mFootEditer = (EditText)findViewById(R.id.question_detail_foot_editer);
    	
    	mCommentFooter = getLayoutInflater().inflate(R.layout.view_oschina_listview_footer, null);
    	mCommentFootMore = (TextView)mCommentFooter.findViewById(R.id.listview_foot_more);
        mCommentFootProgress = (ProgressBar)mCommentFooter.findViewById(R.id.listview_foot_progress);
    	mComment = (PullToRefreshListView)findViewById(R.id.comment_list_listview);
    }
    
    private void setListener() 
    {
    	mBack.setOnClickListener(this);
    	mFavorite.setOnClickListener(this);
    	mRefresh.setOnClickListener(this);
    	mShare.setOnClickListener(this);
    	mDetail.setOnClickListener(this);
    	mCommentList.setOnClickListener(this);
    	mAuthor.setOnClickListener(this);
    	mFootEditebox.setOnClickListener(this);
    	mFootEditer.setOnFocusChangeListener(this); 
    	mFootEditer.setOnKeyListener(this);
    	mFootPubcomment.setOnClickListener(this);
    	mComment.setOnItemClickListener(this);
        mComment.setOnScrollListener(this);
        mComment.setOnItemLongClickListener(this);
        mComment.setOnRefreshListener(this);
	}
    
	private void initData()
	{		
		mInputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		mPostId = getIntent().getIntExtra("post_id", 0); 
    	if(mPostId > 0) mCommentKey = AppConfig.TEMP_COMMENT + "_" + CommentList.CATALOG_POST + "_" + mPostId;
    	
    	mDetail.setEnabled(false);
    	mWebView.getSettings().setJavaScriptEnabled(false);
    	mWebView.getSettings().setSupportZoom(true);
    	mWebView.getSettings().setBuiltInZoomControls(true);
    	mWebView.getSettings().setDefaultFontSize(15);    	
    	mBvComment = new BadgeView(this, mCommentList);
    	mBvComment.setBackgroundResource(R.drawable.oschina_count_bg2);
    	mBvComment.setIncludeFontPadding(false);
    	mBvComment.setGravity(Gravity.CENTER);
    	mBvComment.setTextSize(8f);
    	mBvComment.setTextColor(Color.WHITE);
    	
    	//编辑器添加文本监听
    	mFootEditer.addTextChangedListener(UIHelper.getTextWatcher(mContext, mCommentKey));
    	//显示临时编辑内容
    	UIHelper.showTempEditContent(mContext, mFootEditer, mCommentKey);
		initData(mPostId, false);
	}
	
    private void initData(final int post_id, final boolean isRefresh)
    {
    	headButtonSwitch(DATA_LOAD_ING);
		new Thread()
		{
			public void run() 
			{
                Message msg = new Message();
				try 
				{
					mPostDetail = UserHelper.getPost(mContext, post_id, isRefresh);
	                msg.what = (mPostDetail!=null && mPostDetail.getId()>0) ? 1 : 0;
	                msg.obj = (mPostDetail!=null) ? mPostDetail.getNotice() : null;
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
    }

    private void initCommentView()
    {    
    	mCommentAdapter = new CommentAdapter(mContext, mCommentData, R.layout.item_oschina_comment); 
        mComment.addFooterView(mCommentFooter);
        mComment.setAdapter(mCommentAdapter); 
    }
    
	private void initCommentData()
	{
		mCurId = mPostId;
		mCurCatalog = CommentList.CATALOG_POST;
		loadCommentData(mCurId,mCurCatalog,0,mCommentHandler,UIHelper.LISTVIEW_ACTION_INIT);
    }
	
	/**
     * 底部栏切换
     * @param type
     */
    private void viewSwitch(int type) 
    {
    	switch (type) 
    	{
		case VIEWSWITCH_TYPE_DETAIL:
		{
			mDetail.setEnabled(false);
			mCommentList.setEnabled(true);
			mHeadTitle.setText("帖子正文");
			mViewSwitcher.setDisplayedChild(0);			
			break;
		}
		case VIEWSWITCH_TYPE_COMMENTS:
		{
			mDetail.setEnabled(true);
			mCommentList.setEnabled(false);
			mHeadTitle.setText("网友评论");
			mViewSwitcher.setDisplayedChild(1);
			break;
		}
		default:
			break;
    	}
    }
    
    /**
     * 头部按钮展示
     * @param type
     */
    private void headButtonSwitch(int type) 
    {
    	switch (type) 
    	{
		case DATA_LOAD_ING:
		{
			mScrollView.setVisibility(View.GONE);
			mProgressbar.setVisibility(View.VISIBLE);
			mRefresh.setVisibility(View.GONE);
			break;
		}
		case DATA_LOAD_COMPLETE:
		{
			mScrollView.setVisibility(View.VISIBLE);
			mProgressbar.setVisibility(View.GONE);
			mRefresh.setVisibility(View.VISIBLE);
			break;
		}
		case DATA_LOAD_FAIL:
		{
			mScrollView.setVisibility(View.GONE);
			mProgressbar.setVisibility(View.GONE);
			mRefresh.setVisibility(View.VISIBLE);
			break;
		}
		default:
			break;
		}
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
		new Thread()
		{
			public void run() 
			{
				Message msg = new Message();
				boolean isRefresh = false;
				if(action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL) isRefresh = true;
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
	
	/**
	 * 注册双击全屏事件
	 */
	private void regOnDoubleEvent()
	{
		mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener()
		{
			@Override
			public boolean onDoubleTap(MotionEvent e) 
			{
				mIsFullScreen = !mIsFullScreen;
				if (!mIsFullScreen) 
				{   
                    WindowManager.LayoutParams params = getWindow().getAttributes();   
                    params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);   
                    getWindow().setAttributes(params);   
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);  
                    mHeader.setVisibility(View.VISIBLE);
                    mFooter.setVisibility(View.VISIBLE);
                }
				else 
				{    
                    WindowManager.LayoutParams params = getWindow().getAttributes();   
                    params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;   
                    getWindow().setAttributes(params);   
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);   
                    mHeader.setVisibility(View.GONE);
                    mFooter.setVisibility(View.GONE);
                }
				return true;
			}
		});
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.question_detail_back:
		{
			finish();
			break;
		}
		case R.id.question_detail_refresh:
		{
			initData(mPostId, true);
			loadCommentData(mCurId,mCurCatalog,0,mCommentHandler,UIHelper.LISTVIEW_ACTION_REFRESH);
			break;
		}
		case R.id.question_detail_author:
		{
			UIHelper.showUserCenter(v.getContext(), mPostDetail.getAuthorId(), mPostDetail.getAuthor());
			break;
		}
		case R.id.question_detail_footbar_detail:
		{
			if(mPostId == 0) return;
			//切换显示详情
			viewSwitch(VIEWSWITCH_TYPE_DETAIL);
			break;
		}
		case R.id.question_detail_footbar_commentlist:
		{
			if(mPostId == 0) return;
			//切换显示评论
			viewSwitch(VIEWSWITCH_TYPE_COMMENTS);
			break;
		}
		case R.id.question_detail_footbar_favorite:
		{
			if(mPostId == 0 || mPostDetail == null) return;
			if(!UserHelper.isLogin())
			{
				UIHelper.showLoginDialog(mContext);
				return;
			}
			final int uid = UserHelper.getLoginUid();
			final Handler handler = new Handler()
			{
				public void handleMessage(Message msg) 
				{
					if(msg.what == 1)
					{
						Result res = (Result)msg.obj;
						if(res.OK())
						{
							if(mPostDetail.getFavorite() == 1)
							{
								mPostDetail.setFavorite(0);
								mFavorite.setImageResource(R.drawable.oschina_bar_favorite);
							}
							else
							{
								mPostDetail.setFavorite(1);
								mFavorite.setImageResource(R.drawable.oschina_bar_favorite2);
							}
							//重新保存缓存
							CacheUtils.saveObject(mContext, mPostDetail, mPostDetail.getCacheKey());
						}
						UIHelper.ToastMessage(mContext, res.getErrorMessage());
					}
				}        			
    		};
    		new Thread()
    		{
				public void run() 
				{
					Message msg = new Message();
					Result res = null;
					try 
					{
						if(mPostDetail.getFavorite() == 1)
						{
							res = UserHelper.delFavorite(mContext, uid, mPostId, FavoriteList.TYPE_POST);
						}
						else
						{
							res = UserHelper.addFavorite(mContext, uid, mPostId, FavoriteList.TYPE_POST);
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
		case R.id.question_detail_footbar_share:
		{
			if(mPostDetail == null)
			{
				UIHelper.ToastMessage(mContext, "无法获取文章信息");
				return;
			}
			UIHelper.showShareDialog(OSChinaQuestionDetailActivity.this, mPostDetail.getTitle(), mPostDetail.getUrl());
			break;
		}
		case R.id.question_detail_footbar_editebox:
		{
			mFootViewSwitcher.showNext();
			mFootEditer.setVisibility(View.VISIBLE);
			mFootEditer.requestFocus();
			mFootEditer.requestFocusFromTouch();
			break;
		}
		case R.id.question_detail_foot_pubcomment:
		{
			mId = mCurId;
			if(mCurId == 0) return;
			mCatalog = mCurCatalog;
			mContentText = mFootEditer.getText().toString();
			if(StringUtils.isEmpty(mContentText))
			{
				UIHelper.ToastMessage(v.getContext(), "请输入回帖内容");
				return;
			}
			
			if(!UserHelper.isLogin())
			{
				UIHelper.showLoginDialog(mContext);
				return;
			}
				
			mUId = UserHelper.getLoginUid();
			mProgress = ProgressDialog.show(v.getContext(), null, "发表中···", true, true); 	
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
							//恢复初始底部栏
							mFootViewSwitcher.setDisplayedChild(0);
							mFootEditer.clearFocus();
							mFootEditer.setText("");
							mFootEditer.setVisibility(View.GONE);
							//跳到评论列表
					    	viewSwitch(VIEWSWITCH_TYPE_COMMENTS);
					    	//更新评论列表
					    	mCommentData.add(0,res.getComment());
					    	mCommentAdapter.notifyDataSetChanged();
					    	mComment.setSelection(0);        	
							//显示评论数
					        int count = mPostDetail.getAnswerCount() + 1;
					        mPostDetail.setAnswerCount(count);
							mBvComment.setText(count+"");
							mBvComment.show();
							//清除之前保存的编辑内容
							AppProperty.removeProperty(mContext, mCommentKey);
						}
					}
				}
			};
			new Thread()
			{
				public void run() 
				{
					Message msg = new Message();
					Result res = new Result();
					try 
					{
						//发表评论
						res = UserHelper.pubComment(mContext, mCatalog, mId, mUId, mContentText, mIsPostToMyZone);
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		//点击头部、底部栏无效
		if(position == 0 || view == mCommentFooter) return;
		Comment com = null;
		//判断是否是TextView
		if(view instanceof TextView)
		{
			com = (Comment)view.getTag();
		}
		else
		{
    		ImageView img = (ImageView)view.findViewById(R.id.comment_listitem_userface);
    		com = (Comment)img.getTag();
		} 
		if(com == null) return;
		//跳转--回复评论界面
		UIHelper.showCommentReply(mContext, mCurId, mCurCatalog, com.getId(), com.getAuthorId(), com.getAuthor(), com.getContent());
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
				scrollEnd = true;
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
			loadCommentData(mCurId, mCurCatalog, pageIndex, mCommentHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
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
		Comment _com = null;
		//判断是否是TextView
		if(view instanceof TextView)
		{
			_com = (Comment)view.getTag();
		}
		else
		{
    		ImageView img = (ImageView)view.findViewById(R.id.comment_listitem_userface);
    		_com = (Comment)img.getTag();
		}
		if(_com == null) return false;
		final Comment com = _com;
		mCurPosition = mCommentData.indexOf(com);
		
		//操作--回复 & 删除
		int uid = UserHelper.getLoginUid();
		//判断该评论是否是当前登录用户发表的：true--有删除操作  false--没有删除操作
		if(uid == com.getAuthorId())
		{
    		final Handler handler = new Handler()
    		{
				public void handleMessage(Message msg) 
				{
					if(msg.what == 1)
					{
						Result res = (Result)msg.obj;
						if(res.OK())
						{
							mSumData--;
							mBvComment.setText(mSumData+"");
				    		mBvComment.show();
							mCommentData.remove(com);
							mCommentAdapter.notifyDataSetChanged();
						}
						UIHelper.ToastMessage(mContext, res.getErrorMessage());
					}
				}        			
    		};
    		final Thread thread = new Thread()
    		{
				public void run() 
				{
					Message msg = new Message();
					try 
					{
						Result res = UserHelper.delComment(mContext, mCurId, mCurCatalog, com.getId(), com.getAuthorId());
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
    		UIHelper.showCommentOptionDialog(mContext, mCurId, mCurCatalog, com, thread);
		}
		else
		{
			UIHelper.showCommentOptionDialog(mContext, mCurId, mCurCatalog, com, null);
		}
		return true;
	}

	@Override
	public void onRefresh() 
	{
		loadCommentData(mCurId, mCurCatalog, 0, mCommentHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) 
	{
		mGestureDetector.onTouchEvent(event);
		return super.dispatchTouchEvent(event);
	}
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{ 	
		if (resultCode != RESULT_OK) return;   
    	if (data == null) return;
    	//跳到评论列表
    	viewSwitch(VIEWSWITCH_TYPE_COMMENTS);
    	
        if (requestCode == UIHelper.REQUEST_CODE_FOR_RESULT) 
        { 
        	Comment comm = (Comment)data.getSerializableExtra("COMMENT_SERIALIZABLE");
        	mCommentData.add(0,comm);
        	mCommentAdapter.notifyDataSetChanged();
        	mComment.setSelection(0);
    		//显示评论数
            int count = mPostDetail.getAnswerCount() + 1;
            mPostDetail.setAnswerCount(count);
    		mBvComment.setText(count+"");
    		mBvComment.show();
        }
        else if (requestCode == UIHelper.REQUEST_CODE_FOR_REPLY)
        {
        	Comment comm = (Comment)data.getSerializableExtra("COMMENT_SERIALIZABLE");
        	mCommentData.set(mCurPosition, comm);
        	mCommentAdapter.notifyDataSetChanged();
        }
	}
	
	@SuppressWarnings("deprecation")
	private String getPostTags(List<String> taglist) 
    {
    	if(taglist == null) return "";
    	String tags = "";
    	for(String tag : taglist) 
    	{
    		tags += String.format("<a class='tag' href='http://www.oschina.net/question/tag/%s' >&nbsp;%s&nbsp;</a>&nbsp;&nbsp;", URLEncoder.encode(tag), tag);
    	}
    	return String.format("<div style='margin-top:10px;'>%s</div>", tags);
    }
	
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			headButtonSwitch(DATA_LOAD_COMPLETE);
			if(msg.what == 1)
			{					
				mTitle.setText(mPostDetail.getTitle());
				mAuthor.setText(mPostDetail.getAuthor());
				mPubDate.setText(StringUtils.friendly_time(mPostDetail.getPubDate()));
				mCommentCount.setText(mPostDetail.getAnswerCount() + "回/" + mPostDetail.getViewCount() + "阅");
				
				//是否收藏
				if(mPostDetail.getFavorite() == 1)
				{
					mFavorite.setImageResource(R.drawable.oschina_bar_favorite2);
				}
				else
				{
					mFavorite.setImageResource(R.drawable.oschina_bar_favorite);
				}
				//显示评论数
				if(mPostDetail.getAnswerCount() > 0)
				{
					mBvComment.setText(mPostDetail.getAnswerCount()+"");
					mBvComment.show();
				}
				else
				{
					mBvComment.setText("");
					mBvComment.hide();
				}
				
				//显示标签
				String tags = getPostTags(mPostDetail.getTags());
				String body = UIHelper.WEB_STYLE + mPostDetail.getBody() + tags + "<div style=\"margin-bottom: 80px\" />";
				//读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
				boolean isLoadImage;
				
				if(NetUtils.NETTYPE_WIFI == NetUtils.getNetworkType(mContext))
				{
					isLoadImage = true;
				}
				else
				{
					isLoadImage = UIHelper.isLoadImage(mContext);
				}
				if(isLoadImage)
				{
					body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+","$1");
					body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+","$1");
				}
				else
				{
					body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>","");
				}

				mWebView.loadDataWithBaseURL(null, body, "text/html", "utf-8",null);
				mWebView.setWebViewClient(UIHelper.getWebViewClient());	
				
				//发送通知广播
				if(msg.obj != null)
				{
					UIHelper.sendBroadCast(mContext, (Notice)msg.obj);
				}
			}
			else if(msg.what == 0)
			{
				headButtonSwitch(DATA_LOAD_FAIL);
				
				UIHelper.ToastMessage(mContext, "读取失败，可能已被删除");
			}
			else if(msg.what == -1 && msg.obj != null)
			{
				headButtonSwitch(DATA_LOAD_FAIL);
			}
		}
	};
	
	private Handler mCommentHandler = new Handler()
	{
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) 
		{				
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
					mCommentData.clear();//先清除原有数据
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
				
				//评论数更新
				if(mPostDetail != null && mCommentData.size() > mPostDetail.getAnswerCount())
				{
					mPostDetail.setAnswerCount(mCommentData.size());
					mBvComment.setText(mCommentData.size()+"");
					mBvComment.show();
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
				if(notice != null){
					UIHelper.sendBroadCast(mContext, notice);
				}
			}
			else if(msg.what == -1)
			{
				//有异常--显示加载出错 & 弹出错误消息
				mCurDataState = UIHelper.LISTVIEW_DATA_MORE;
				mCommentFootMore.setText(R.string.load_error);
				
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
				mComment.setSelection(0);
			}
		}
	};
}
