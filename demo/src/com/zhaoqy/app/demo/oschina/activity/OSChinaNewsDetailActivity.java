/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaNewsDetailActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 新闻详情
 * @author: zhaoqy
 * @date: 2015-11-20 上午10:46:43
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

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
import com.zhaoqy.app.demo.oschina.item.News;
import com.zhaoqy.app.demo.oschina.item.News.Relative;
import com.zhaoqy.app.demo.oschina.item.Notice;
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
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

@SuppressLint("HandlerLeak")
public class OSChinaNewsDetailActivity extends OSChinaBaseActivity implements OnClickListener, OnFocusChangeListener, OnKeyListener, OnItemClickListener, OnScrollListener, OnItemLongClickListener, OnRefreshListener
{
	private final static int VIEWSWITCH_TYPE_DETAIL = 0x001;
	private final static int VIEWSWITCH_TYPE_COMMENTS = 0x002;
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private final static int DATA_LOAD_FAIL = 0x003;
	private InputMethodManager    mImm;
	private PullToRefreshListView mComment;
	private CommentAdapter  mAdapter;
	private List<Comment>   mData = new ArrayList<Comment>();
	private ProgressDialog  mProgress;
	private GestureDetector mGestureDetector;
	private Context      mContext;
	private FrameLayout  mHeader;
	private LinearLayout mFooter;
	private ImageView    mHome;
	private ImageView    mFavorite;
	private ImageView    mRefresh;
	private TextView     mHeadTitle;
	private ProgressBar  mProgressbar;
	private ScrollView   mScrollView;
    private ViewSwitcher mViewSwitcher;
	private BadgeView    mBadgeView;
	private ImageView    mDetail;
	private ImageView    mCommentList;
	private ImageView    mShare;
	private TextView     mTitle;
	private TextView     mAuthor;
	private TextView     mPubDate;
	private TextView     mCommentCount;
	private WebView      mWebView;
    private News         mNewsDetail;
	private View         mCommentFooter;
	private TextView     mCommentFootMore;
	private ProgressBar  mCommentFootProgress;
	private ViewSwitcher mFootViewSwitcher;
	private ImageView    mFootEditebox;
	private EditText     mFootEditer;
	private Button       mFootPubcomment;
	private String       mCommentKey = AppConfig.TEMP_COMMENT;
	private String       mContentText;
	private boolean      mIsFullScreen;
	private int          mNewsId;
    private int          mSumData;
    private int          mCurId;
	private int          mCurCatalog;	
	private int          mCurDataState;
	private int          mCurPosition;
	private int          mCatalog;
	private int          mId;
	private int          mUId;
	private int          mIsPostToMyZone; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oschina_news_detail);
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
    	mHeader = (FrameLayout)findViewById(R.id.news_detail_header);
    	mFooter = (LinearLayout)findViewById(R.id.news_detail_footer);
    	mHome = (ImageView)findViewById(R.id.news_detail_home);
    	mRefresh = (ImageView)findViewById(R.id.news_detail_refresh);
    	mHeadTitle = (TextView)findViewById(R.id.news_detail_head_title);
    	mProgressbar = (ProgressBar)findViewById(R.id.news_detail_head_progress);
    	mViewSwitcher = (ViewSwitcher)findViewById(R.id.news_detail_viewswitcher);
    	mScrollView = (ScrollView)findViewById(R.id.news_detail_scrollview);
    	
    	mDetail = (ImageView)findViewById(R.id.news_detail_footbar_detail);
    	mCommentList = (ImageView)findViewById(R.id.news_detail_footbar_commentlist);
    	mShare = (ImageView)findViewById(R.id.news_detail_footbar_share);
    	mFavorite = (ImageView)findViewById(R.id.news_detail_footbar_favorite);
    	
    	mTitle = (TextView)findViewById(R.id.news_detail_title);
    	mAuthor = (TextView)findViewById(R.id.news_detail_author);
    	mPubDate = (TextView)findViewById(R.id.news_detail_date);
    	mCommentCount = (TextView)findViewById(R.id.news_detail_commentcount);
    	mWebView = (WebView)findViewById(R.id.news_detail_webview);
    	
    	mFootViewSwitcher = (ViewSwitcher)findViewById(R.id.news_detail_foot_viewswitcher);
    	mFootPubcomment = (Button)findViewById(R.id.news_detail_foot_pubcomment);
    	mFootEditebox = (ImageView)findViewById(R.id.news_detail_footbar_editebox);
    	mFootEditer = (EditText)findViewById(R.id.news_detail_foot_editer);
    	
    	mCommentFooter = getLayoutInflater().inflate(R.layout.view_oschina_listview_footer, null);
    	mCommentFootMore = (TextView)mCommentFooter.findViewById(R.id.listview_foot_more);
        mCommentFootProgress = (ProgressBar)mCommentFooter.findViewById(R.id.listview_foot_progress);
        mComment = (PullToRefreshListView)findViewById(R.id.comment_list_listview);
    }
    
    private void setListener() 
    {
    	mHome.setOnClickListener(this);
    	mFavorite.setOnClickListener(this);
    	mRefresh.setOnClickListener(this);
    	mAuthor.setOnClickListener(this);
    	mShare.setOnClickListener(this);
    	mDetail.setOnClickListener(this);
    	mCommentList.setOnClickListener(this);
    	mFootPubcomment.setOnClickListener(this);
    	mFootEditebox.setOnClickListener(this);
    	mFootEditer.setOnFocusChangeListener(this); 
    	mFootEditer.setOnKeyListener(this);
    	mComment.setOnItemClickListener(this);
        mComment.setOnScrollListener(this);
        mComment.setOnItemLongClickListener(this);
        mComment.setOnRefreshListener(this);
	}
    
	private void initData()
	{	
		mImm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		mNewsId = getIntent().getIntExtra("news_id", 0);
		if(mNewsId > 0) mCommentKey = AppConfig.TEMP_COMMENT + "_" + CommentList.CATALOG_NEWS + "_" + mNewsId;
		
		mDetail.setEnabled(false);
    	mWebView.getSettings().setJavaScriptEnabled(false);
    	mWebView.getSettings().setSupportZoom(true);
    	mWebView.getSettings().setBuiltInZoomControls(true);
    	mWebView.getSettings().setDefaultFontSize(15);
    	
    	mBadgeView = new BadgeView(this, mCommentList);
    	mBadgeView.setBackgroundResource(R.drawable.oschina_count_bg2);
    	mBadgeView.setIncludeFontPadding(false);
    	mBadgeView.setGravity(Gravity.CENTER);
    	mBadgeView.setTextSize(8f);
    	mBadgeView.setTextColor(Color.WHITE);
    	
    	//编辑器添加文本监听
    	mFootEditer.addTextChangedListener(UIHelper.getTextWatcher(mContext, mCommentKey));
    	//显示临时编辑内容
    	UIHelper.showTempEditContent(mContext, mFootEditer, mCommentKey);
		initData(mNewsId, false);
	}
	
    private void initData(final int news_id, final boolean isRefresh)
    {		
    	headButtonSwitch(DATA_LOAD_ING);
		new Thread()
		{
			public void run() 
			{
                Message msg = new Message();
				try 
				{
					mNewsDetail = UserHelper.getNews(mContext, news_id, isRefresh);
	                msg.what = (mNewsDetail!=null && mNewsDetail.getId()>0) ? 1 : 0;
	                msg.obj = (mNewsDetail!=null) ? mNewsDetail.getNotice() : null;
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
			mHeadTitle.setText("新闻正文");
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

    private void initCommentView()
    {    	
    	mAdapter = new CommentAdapter(mContext, mData, R.layout.item_oschina_comment); 
        mComment.addFooterView(mCommentFooter);
        mComment.setAdapter(mAdapter); 
    }
    
	private void initCommentData()
	{
		mCurId = mNewsId;
		mCurCatalog = CommentList.CATALOG_NEWS;
		loadCommentData(mCurId,mCurCatalog,0, mCommentHandler,UIHelper.LISTVIEW_ACTION_INIT);
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
				if(action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
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
		case R.id.news_detail_home:
		{
			UIHelper.showHome(mContext);
	    	break;
		}
		case R.id.news_detail_refresh:
		{
			initData(mNewsId, true);
			loadCommentData(mCurId, mCurCatalog, 0, mCommentHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
	    	break;
		}	
		case R.id.news_detail_author:
		{
			UIHelper.showUserCenter(mContext, mNewsDetail.getAuthorId(), mNewsDetail.getAuthor());
	    	break;
		}
		case R.id.news_detail_footbar_detail:
		{
			if(mNewsId == 0) return;
			//切换到详情
			viewSwitch(VIEWSWITCH_TYPE_DETAIL);
	    	break;
		}
		case R.id.news_detail_footbar_commentlist:
		{
			if(mNewsId == 0) return;
			//切换到评论
			viewSwitch(VIEWSWITCH_TYPE_COMMENTS);
	    	break;
		}
		case R.id.news_detail_footbar_favorite:
		{
			if(mNewsId == 0 || mNewsDetail == null) return;
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
							if(mNewsDetail.getFavorite() == 1)
							{
								mNewsDetail.setFavorite(0);
								mFavorite.setImageResource(R.drawable.oschina_bar_favorite);
							}
							else
							{
								mNewsDetail.setFavorite(1);
								mFavorite.setImageResource(R.drawable.oschina_bar_favorite2);
							}
							//重新保存缓存
							CacheUtils.saveObject(mContext, mNewsDetail, mNewsDetail.getCacheKey());
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
						if(mNewsDetail.getFavorite() == 1)
						{
							res = UserHelper.delFavorite(mContext, uid, mNewsId, FavoriteList.TYPE_NEWS);
						}
						else
						{
							res = UserHelper.addFavorite(mContext, uid, mNewsId, FavoriteList.TYPE_NEWS);
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
		case R.id.news_detail_footbar_share:
		{
			if(mNewsDetail == null)
			{
				UIHelper.ToastMessage(v.getContext(), "无法获取文章信息");
				return;
			}
			UIHelper.showShareDialog(OSChinaNewsDetailActivity.this, mNewsDetail.getTitle(), mNewsDetail.getUrl());
	    	break;
		}
		case R.id.news_detail_foot_pubcomment:
		{
			mId = mCurId;
			if(mCurId == 0) return;
			mCatalog = mCurCatalog;
			mContentText = mFootEditer.getText().toString();
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
				
			mUId = UserHelper.getLoginUid();
			mProgress = ProgressDialog.show(v.getContext(), null, "发表中···",true,true); 			
			
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
							//跳到评论列表
					    	viewSwitch(VIEWSWITCH_TYPE_COMMENTS);
					    	//更新评论列表
					    	mData.add(0,res.getComment());
					    	mAdapter.notifyDataSetChanged();
					    	mComment.setSelection(0);        	
							//显示评论数
					        int count = mNewsDetail.getCommentCount() + 1;
					        mNewsDetail.setCommentCount(count);
							mBadgeView.setText(count+"");
							mBadgeView.show();
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
		case R.id.news_detail_footbar_editebox:
		{
			mFootViewSwitcher.showNext();
			mFootEditer.setVisibility(View.VISIBLE);
			mFootEditer.requestFocus();
			mFootEditer.requestFocusFromTouch();
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
			mImm.showSoftInput(v, 0);  
        }  
        else
        {  
        	mImm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
        }  
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) 
	{
		mComment.onScrollStateChanged(view, scrollState);
		//数据为空--不用继续下面代码了
		if(mData.size() == 0) return;
		//判断是否滚动到底部
		boolean scrollEnd = false;
		try 
		{
			if(view.getPositionForView(mCommentFooter) == view.getLastVisiblePosition()) scrollEnd = true;
		} 
		catch (Exception e) 
		{
			scrollEnd = false;
		}
		
		if(scrollEnd && mCurDataState==UIHelper.LISTVIEW_DATA_MORE)
		{
			mComment.setTag(UIHelper.LISTVIEW_DATA_LOADING);
			mCommentFootMore.setText("加载中···");
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
	public boolean onKey(View v, int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if(mFootViewSwitcher.getDisplayedChild()==1)
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
		mCurPosition = mData.indexOf(com);

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
							mBadgeView.setText(mSumData+"");
				    		mBadgeView.show();
							mData.remove(com);
							mAdapter.notifyDataSetChanged();
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
    	viewSwitch(VIEWSWITCH_TYPE_COMMENTS);//跳到评论列表
    	
        if (requestCode == UIHelper.REQUEST_CODE_FOR_RESULT) 
        { 
        	Comment comm = (Comment)data.getSerializableExtra("COMMENT_SERIALIZABLE");
        	mData.add(0,comm);
        	mAdapter.notifyDataSetChanged();
        	mComment.setSelection(0);        	
    		//显示评论数
            int count = mNewsDetail.getCommentCount() + 1;
            mNewsDetail.setCommentCount(count);
    		mBadgeView.setText(count+"");
    		mBadgeView.show();
        }
        else if (requestCode == UIHelper.REQUEST_CODE_FOR_REPLY)
        {
        	Comment comm = (Comment)data.getSerializableExtra("COMMENT_SERIALIZABLE");
        	mData.set(mCurPosition, comm);
        	mAdapter.notifyDataSetChanged();
        }
	}
	
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) 
		{				
			if(msg.what == 1)
			{	
				headButtonSwitch(DATA_LOAD_COMPLETE);					
				mTitle.setText(mNewsDetail.getTitle());
				mAuthor.setText(mNewsDetail.getAuthor());
				mPubDate.setText(StringUtils.friendly_time(mNewsDetail.getPubDate()));
				mCommentCount.setText(String.valueOf(mNewsDetail.getCommentCount()));
				
				//是否收藏
				if(mNewsDetail.getFavorite() == 1)
				{
					mFavorite.setImageResource(R.drawable.oschina_bar_favorite2);
				}
				else
				{
					mFavorite.setImageResource(R.drawable.oschina_bar_favorite);
				}
				
				//显示评论数
				if(mNewsDetail.getCommentCount() > 0)
				{
					mBadgeView.setText(mNewsDetail.getCommentCount()+"");
					mBadgeView.show();
				}
				else
				{
					mBadgeView.setText("");
					mBadgeView.hide();
				}
				
				String body = UIHelper.WEB_STYLE + mNewsDetail.getBody();					
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
					//过滤掉 img标签的width,height属性
					body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+","$1");
					body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+","$1");
				}
				else
				{
					//过滤掉 img标签
					body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>","");
				}
				
				//更多关于***软件的信息
				String softwareName = mNewsDetail.getSoftwareName(); 
				String softwareLink = mNewsDetail.getSoftwareLink(); 
				if(!StringUtils.isEmpty(softwareName) && !StringUtils.isEmpty(softwareLink))
					body += String.format("<div id='oschina_software' style='margin-top:8px;color:#FF0000;font-weight:bold'>更多关于:&nbsp;<a href='%s'>%s</a>&nbsp;的详细信息</div>", softwareLink, softwareName);
				
				//相关新闻
				if(mNewsDetail.getRelatives().size() > 0)
				{
					String strRelative = "";
					for(Relative relative : mNewsDetail.getRelatives())
					{
						strRelative += String.format("<a href='%s' style='text-decoration:none'>%s</a><p/>", relative.url, relative.title);
					}
					body += String.format("<p/><hr/><b>相关资讯</b><div><p/>%s</div>", strRelative);
				}
				body += "<div style='margin-bottom: 80px'/>";					
				
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
					//先清除原有数据
					mData.clear();
					mData.addAll(list.getCommentlist());
					break;
				}
				case UIHelper.LISTVIEW_ACTION_SCROLL:
				{
					mSumData += msg.what;
					if(mData.size() > 0)
					{
						for(Comment com1 : list.getCommentlist())
						{
							boolean b = false;
							for(Comment com2 : mData)
							{
								if(com1.getId() == com2.getId() && com1.getAuthorId() == com2.getAuthorId())
								{
									b = true;
									break;
								}
							}
							if(!b) mData.add(com1);
						}
					}
					else
					{
						mData.addAll(list.getCommentlist());
					}
					break;
				}
				default:
					break;
				}	
				
				//评论数更新
				if(mNewsDetail != null && mData.size() > mNewsDetail.getCommentCount())
				{
					mNewsDetail.setCommentCount(mData.size());
					mBadgeView.setText(mData.size()+"");
					mBadgeView.show();
				}
				
				if(msg.what < 20)
				{
					mCurDataState = UIHelper.LISTVIEW_DATA_FULL;
					mAdapter.notifyDataSetChanged();
					mCommentFootMore.setText(R.string.load_full);
				}
				else if(msg.what == 20)
				{					
					mCurDataState = UIHelper.LISTVIEW_DATA_MORE;
					mAdapter.notifyDataSetChanged();
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
				//有异常--显示加载出错 & 弹出错误消息
				mCurDataState = UIHelper.LISTVIEW_DATA_MORE;
				mCommentFootMore.setText(R.string.load_error);
			}
			if(mData.size()==0)
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
