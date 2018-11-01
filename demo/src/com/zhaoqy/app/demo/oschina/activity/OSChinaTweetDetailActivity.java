/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaTweetDetailActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 动弹详情
 * @author: zhaoqy
 * @mDate: 2015-11-24 上午10:27:11
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.adapter.CommentAdapter;
import com.zhaoqy.app.demo.oschina.adapter.FaceAdapter;
import com.zhaoqy.app.demo.oschina.common.AppConfig;
import com.zhaoqy.app.demo.oschina.common.AppProperty;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.Comment;
import com.zhaoqy.app.demo.oschina.item.CommentList;
import com.zhaoqy.app.demo.oschina.item.Notice;
import com.zhaoqy.app.demo.oschina.item.Result;
import com.zhaoqy.app.demo.oschina.item.Tweet;
import com.zhaoqy.app.demo.oschina.util.StringUtils;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView.OnRefreshListener;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

@SuppressLint("HandlerLeak")
public class OSChinaTweetDetailActivity extends OSChinaBaseActivity implements OnClickListener, OnItemClickListener, OnScrollListener, OnItemLongClickListener, OnRefreshListener, OnKeyListener
{
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private InputMethodManager    imm;
	private PullToRefreshListView mLvComment;
	private ProgressDialog mProgress;
	private CommentAdapter mCommentAdapter;
	private List<Comment>  mCommentData = new ArrayList<Comment>();
	private Context      mContext;
	private ImageView    mBack;
	private ImageView    mRefresh;
	private LinearLayout mLinearlayout;
	private ProgressBar  mProgressbar;
	private View         mCommentFooter;
	private TextView     mCommentFootMore;
	private ProgressBar  mCommentFootProgress;
    private View         mHeader;
    private ImageView    mUserFace;
    private TextView     mUserName;
    private TextView     mDate;
    private TextView     mCommentCount;
    private WebView      mContent;
    private ImageView    mImage;
    private Tweet        mTweetDetail;
	private ViewSwitcher mFootViewSwitcher;
	private ImageView    mFootEditebox;
	private EditText     mFootEditer;
	private Button       mFootPubcomment;
	private ImageView    mFace;
	private GridView     mGridView;
	private FaceAdapter  mGVFaceAdapter;
	private String       mCommentKey = AppConfig.TEMP_COMMENT;
	private String       mContentText;
	private int          mSumData;
	private int          mCurId;
	private int          mCurCatalog;	
	private int          mDataState;
	private int          mCatalog;
	private int          mId;
	private int          mUId;
	private int          mIsPostToMyZone; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oschina_tweet_detail);
        mContext = this;
        
        initView();
        initData();   
      	initGridView();
      	setListener();
    }
    
	private void initView()
    {   
    	mBack = (ImageView)findViewById(R.id.tweet_detail_back);
    	mRefresh = (ImageView)findViewById(R.id.tweet_detail_refresh);
    	mLinearlayout = (LinearLayout)findViewById(R.id.tweet_detail_linearlayout);
    	mProgressbar = (ProgressBar)findViewById(R.id.tweet_detail_head_progress);
    	mFace = (ImageView)findViewById(R.id.tweet_detail_foot_face);
    	
    	mFootViewSwitcher = (ViewSwitcher)findViewById(R.id.tweet_detail_foot_viewswitcher);
    	mFootPubcomment = (Button)findViewById(R.id.tweet_detail_foot_pubcomment);
    	mFootEditebox = (ImageView)findViewById(R.id.tweet_detail_footbar_editebox);
    	mFootEditer = (EditText)findViewById(R.id.tweet_detail_foot_editer);
    	
    	mHeader = View.inflate(mContext, R.layout.view_oschina_tweet_detail_content, null);
    	mUserFace = (ImageView)mHeader.findViewById(R.id.tweet_listitem_userface);
    	mUserName = (TextView)mHeader.findViewById(R.id.tweet_listitem_username);
    	mDate = (TextView)mHeader.findViewById(R.id.tweet_listitem_date);
    	mCommentCount = (TextView)mHeader.findViewById(R.id.tweet_listitem_commentCount);
    	mImage = (ImageView)mHeader.findViewById(R.id.tweet_listitem_image);
    	
    	mCommentFooter = getLayoutInflater().inflate(R.layout.view_oschina_listview_footer, null);
    	mCommentFootMore = (TextView)mCommentFooter.findViewById(R.id.listview_foot_more);
        mCommentFootProgress = (ProgressBar)mCommentFooter.findViewById(R.id.listview_foot_progress);
    	mLvComment = (PullToRefreshListView)findViewById(R.id.tweet_detail_commentlist);
    }
    
	private void initData()
	{		
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE); 
		mCurId = getIntent().getIntExtra("tweet_id", 0);
		mCurCatalog = CommentList.CATALOG_TWEET;
    	if(mCurId > 0) mCommentKey = AppConfig.TEMP_COMMENT + "_" + mCurCatalog + "_" + mCurId;
    	
    	//编辑器添加文本监听
    	mFootEditer.addTextChangedListener(UIHelper.getTextWatcher(mContext, mCommentKey));
    	//显示临时编辑内容
    	UIHelper.showTempEditContent(mContext, mFootEditer, mCommentKey);
    	
    	mContent = (WebView)mHeader.findViewById(R.id.tweet_listitem_content);
    	mContent.getSettings().setJavaScriptEnabled(false);
    	mContent.getSettings().setSupportZoom(true);
    	mContent.getSettings().setBuiltInZoomControls(true);
    	mContent.getSettings().setDefaultFontSize(12);
    	
    	mCommentAdapter = new CommentAdapter(mContext, mCommentData, R.layout.item_oschina_comment); 
    	mLvComment.addHeaderView(mHeader);
        mLvComment.addFooterView(mCommentFooter);
        mLvComment.setAdapter(mCommentAdapter); 
		
		loadTweetDetail(mCurId, mHandler, false);
		loadCommentData(mCurId,mCurCatalog, 0, mCommentHandler, UIHelper.LISTVIEW_ACTION_INIT);
    }
	
    private void initGridView() 
    {
    	mGVFaceAdapter = new FaceAdapter(mContext);
    	mGridView = (GridView)findViewById(R.id.tweet_detail_foot_faces);
    	mGridView.setAdapter(mGVFaceAdapter);
    }
    
    private void setListener() 
    {
    	mBack.setOnClickListener(this);
    	mLvComment.setOnItemClickListener(this);
        mLvComment.setOnScrollListener(this);
        mLvComment.setOnItemLongClickListener(this);
        mLvComment.setOnRefreshListener(this);
        mFootEditer.setOnKeyListener(this);
        mRefresh.setOnClickListener(this);
    	mFace.setOnClickListener(this);
    	mFootPubcomment.setOnClickListener(this);
    	mFootEditebox.setOnClickListener(this);
    	mFootEditer.setOnClickListener(this);
    	mUserName.setOnClickListener(this);
		mUserFace.setOnClickListener(this);
		mImage.setOnClickListener(this);
    	mGridView.setOnItemClickListener(this);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.tweet_detail_back:
		{
			finish();
			break;
		}	
		case R.id.tweet_detail_refresh:
		{
			loadTweetDetail(mCurId, mHandler, true);
			loadCommentData(mCurId, mCurCatalog, 0, mCommentHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
			break;
		}	
		case R.id.tweet_detail_foot_face:
		{
			showOrHideIMM();
			break;
		}	
		case R.id.tweet_detail_foot_pubcomment:
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
			mProgress = ProgressDialog.show(v.getContext(), null, "发布中···",true,true); 
			
			final Handler handler = new Handler()
			{
				public void handleMessage(Message msg) 
				{
					if(mProgress!=null)mProgress.dismiss();
					if(msg.what == 1 && msg.obj != null)
					{
						Result res = (Result)msg.obj;
						UIHelper.ToastMessage(mContext, res.getErrorMessage());
						if(res.OK())
						{
							//发送通知广播
							if(res.getNotice() != null) UIHelper.sendBroadCast(mContext, res.getNotice());
							//恢复初始底部栏
							mFootViewSwitcher.setDisplayedChild(0);
							mFootEditer.clearFocus();
							mFootEditer.setText("");
							mFootEditer.setVisibility(View.GONE);
							//隐藏软键盘
							imm.hideSoftInputFromWindow(mFootEditer.getWindowToken(), 0);
							//隐藏表情
							hideFace();
							//更新评论列表
							mCommentData.add(0, res.getComment());
							mCommentAdapter.notifyDataSetChanged();
							mLvComment.setSelection(0);
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
		case R.id.tweet_detail_footbar_editebox:
		{
			mFootViewSwitcher.showNext();
			mFootEditer.setVisibility(View.VISIBLE);
			mFootEditer.requestFocus();
			mFootEditer.requestFocusFromTouch();
			imm.showSoftInput(mFootEditer, 0);
			break;
		}
		case R.id.tweet_detail_foot_editer:
		{
			//显示软键盘&隐藏表情
			showIMM();
			break;
		}
		case R.id.tweet_listitem_username:
		case R.id.tweet_listitem_userface:
		{
			if(mTweetDetail != null)
			{
				UIHelper.showUserCenter(v.getContext(), mTweetDetail.getAuthorId(), mTweetDetail.getAuthor());
			}
			break;
		}
		case R.id.tweet_listitem_image:
		{
			if(mTweetDetail!=null)
			{
				UIHelper.showImageZoomDialog(v.getContext(), mTweetDetail.getImgBig());
			}
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		switch (parent.getId()) 
		{
		case R.id.tweet_detail_commentlist:
		{
			//点击头部、底部栏无效
    		if(position == 0 || view == mCommentFooter || position == 1 || view == mHeader) return;
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
    		UIHelper.showCommentReply(mContext,mCurId, mCurCatalog, com.getId(), com.getAuthorId(), com.getAuthor(), com.getContent());
			break;
		}
		case R.id.tweet_detail_foot_faces:
		{
			//插入的表情
			SpannableString ss = new SpannableString(view.getTag().toString());
			Drawable d = getResources().getDrawable((int)mGVFaceAdapter.getItemId(position));
			//设置表情图片的显示大小
			d.setBounds(0, 0, 35, 35);
			ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
			ss.setSpan(span, 0, view.getTag().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);				 
			//在光标所在处插入表情
			mFootEditer.getText().insert(mFootEditer.getSelectionStart(), ss);	
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) 
	{
		mLvComment.onScrollStateChanged(view, scrollState);
		if(mCommentData.size() == 0) return;
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
		
		if(scrollEnd && mDataState==UIHelper.LISTVIEW_DATA_MORE)
		{
			mLvComment.setTag(UIHelper.LISTVIEW_DATA_LOADING);
			mCommentFootMore.setText(R.string.load_ing);
			mCommentFootProgress.setVisibility(View.VISIBLE);
			int pageIndex = mSumData/20;
			loadCommentData(mCurId, mCurCatalog, pageIndex, mCommentHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
	{
		mLvComment.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
	{
		//点击头部、底部栏无效
		if(position == 0 || view == mCommentFooter || position == 1 || view == mHeader) return false;
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
	public boolean onKey(View v, int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if(mFootViewSwitcher.getDisplayedChild() == 1)
			{
				mFootViewSwitcher.setDisplayedChild(0);
				mFootEditer.clearFocus();
				mFootEditer.setVisibility(View.GONE);
				hideFace();
			}
			return true;
		}
		return false;
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
        	mLvComment.setSelection(0);
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
		headButtonSwitch(DATA_LOAD_ING,2);
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
	 * 线程加载动弹详情
	 * @param tweetId
	 * @param handler
	 */
	private void loadTweetDetail(final int tweetId, final Handler handler, final boolean isRefresh)
	{
		headButtonSwitch(DATA_LOAD_ING,1);
		new Thread()
		{
			public void run() 
			{
				Message msg = new Message();
				try 
				{
					mTweetDetail = UserHelper.getTweet(mContext, tweetId, isRefresh);
					msg.what = (mTweetDetail!=null && mTweetDetail.getId()>0) ? 1 : 0;
					msg.obj = (mTweetDetail!=null) ? mTweetDetail.getNotice() : null;
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
	}
	
	private void headButtonSwitch(int type,int action) 
	{
    	switch (type) 
    	{
		case DATA_LOAD_ING:
		{
			if(action==1)mLinearlayout.setVisibility(View.GONE);
			mProgressbar.setVisibility(View.VISIBLE);
			mRefresh.setVisibility(View.GONE);
			break;
		}
		case DATA_LOAD_COMPLETE:
		{
			mLinearlayout.setVisibility(View.VISIBLE);
			mProgressbar.setVisibility(View.GONE);
			mRefresh.setVisibility(View.VISIBLE);
			break;
		}
		default:
			break;
		}
    }
	
	private void showIMM() 
	{
    	mFace.setTag(1);
    	showOrHideIMM();
    }
	
    private void showFace() 
    {
		mFace.setImageResource(R.drawable.oschina_bar_keyboard);
		mFace.setTag(1);
		mGridView.setVisibility(View.VISIBLE);
    }
    
    private void hideFace() 
    {
    	mFace.setImageResource(R.drawable.oschina_bar_face);
		mFace.setTag(null);
		mGridView.setVisibility(View.GONE);
    }
    
    private void showOrHideIMM() 
    {
    	if(mFace.getTag() == null)
    	{
			//隐藏软键盘
			imm.hideSoftInputFromWindow(mFootEditer.getWindowToken(), 0);
			//显示表情
			showFace();
		}
    	else
    	{
			//显示软键盘
			imm.showSoftInput(mFootEditer, 0);
			//隐藏表情
			hideFace();
		}
    }
    
    //加载动弹
    private Handler mHandler = new Handler()
    {
		public void handleMessage(Message msg) 
		{
			headButtonSwitch(DATA_LOAD_COMPLETE,1);
			if(msg.what == 1)
			{
				mUserName.setText(mTweetDetail.getAuthor());
				mDate.setText(StringUtils.friendly_time(mTweetDetail.getPubDate()));
				mCommentCount.setText(mTweetDetail.getCommentCount()+"");
				
				String body = UIHelper.WEB_STYLE + mTweetDetail.getBody();
				body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+","$1");
				body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+","$1");
				mContent.loadDataWithBaseURL(null, body, "text/html", "utf-8",null);
				mContent.setWebViewClient(UIHelper.getWebViewClient());
				
				//加载用户头像
				String faceURL = mTweetDetail.getFace();
				if(faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL)) 
				{
					mUserFace.setImageResource(R.drawable.oschina_dface);
				}
				else
				{
					UIHelper.showUserFace(mUserFace, faceURL);
				}
				
				//加载图片
				String imgSmall = mTweetDetail.getImgSmall();
				if(!StringUtils.isEmpty(imgSmall)) 
				{
					UIHelper.showLoadImage(mImage, imgSmall, null);
					mImage.setVisibility(View.VISIBLE);
				}
				
				//发送通知广播
				if(msg.obj != null)
				{
					UIHelper.sendBroadCast(mContext, (Notice)msg.obj);
				}
			}
			else if(msg.what == 0)
			{
				UIHelper.ToastMessage(mContext, "读取失败，可能已被删除");	
			}
		}
	};
	
	//加载评论
    private Handler mCommentHandler = new Handler()
	{
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) 
		{
			headButtonSwitch(DATA_LOAD_COMPLETE,2);
			if(msg.what >= 0)
			{						
				CommentList list = (CommentList)msg.obj;
				Notice notice = list.getNotice();
				switch (msg.arg1) 
				{
				case UIHelper.LISTVIEW_ACTION_INIT:
				case UIHelper.LISTVIEW_ACTION_REFRESH:
				{
					mSumData = msg.what;
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
					mDataState = UIHelper.LISTVIEW_DATA_FULL;
					mCommentAdapter.notifyDataSetChanged();
					mCommentFootMore.setText(R.string.load_full);
				}
				else if(msg.what == 20)
				{					
					mDataState = UIHelper.LISTVIEW_DATA_MORE;
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
				mDataState = UIHelper.LISTVIEW_DATA_MORE;
				mCommentFootMore.setText(R.string.load_more);
			}
			if(mCommentData.size()==0)
			{
				mDataState = UIHelper.LISTVIEW_DATA_EMPTY;
				mCommentFootMore.setText(R.string.load_empty);
			}
			mCommentFootProgress.setVisibility(View.GONE);
			if(msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH)
				mLvComment.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
		}
	};
}
