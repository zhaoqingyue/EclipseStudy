/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaUserCenterActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 用户专页
 * @author: zhaoqy
 * @date: 2015-11-24 下午3:25:54
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.adapter.ActiveAdapter;
import com.zhaoqy.app.demo.oschina.adapter.BlogAdapter;
import com.zhaoqy.app.demo.oschina.dialog.UserInfoDialog;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.Active;
import com.zhaoqy.app.demo.oschina.item.Blog;
import com.zhaoqy.app.demo.oschina.item.BlogList;
import com.zhaoqy.app.demo.oschina.item.Notice;
import com.zhaoqy.app.demo.oschina.item.Result;
import com.zhaoqy.app.demo.oschina.item.User;
import com.zhaoqy.app.demo.oschina.item.UserInformation;
import com.zhaoqy.app.demo.oschina.util.StringUtils;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView;

@SuppressLint({ "HandlerLeak", "DefaultLocale" })
public class OSChinaUserCenterActivity extends OSChinaBaseActivity implements OnClickListener, OnCancelListener
{
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private PullToRefreshListView mActive;
	private PullToRefreshListView mBlog;
	private UserInfoDialog mUserinfoDialog;
	private ActiveAdapter  mActiveAdapter;
	private BlogAdapter    mBlogAdapter;
	private List<Active>   mActiveData = new ArrayList<Active>();
	private List<Blog>  mBlogData = new ArrayList<Blog>();
	private Context     mContext;
	private ImageView   mBack;
	private ImageView   mRefresh;
	private TextView    mHeadTitle;
	private ProgressBar mActiveFootProgress;
	private ProgressBar mBlogFootProgress;
	private ProgressBar mProgressbar;
	private RadioButton mRelation;
	private RadioButton mMessage;
	private RadioButton mAtme;
	private Button      mTabActive;
	private Button      mTabBlog;
	private ImageView   mUserface;
	private TextView    mUsername;
	private TextView    mFrom;
	private TextView    mGender;
	private TextView    mJointime;
	private TextView    mDevplatform;
	private TextView    mExpertise;
	private TextView    mLatestonline;
	private View        mActiveFooter;
	private TextView    mActiveFooterMore;
	private View        mBlogFooter;
	private TextView    mBlogFooterMore;
	private User        mUser;
    private String      mHisName;
	private String      mUserName;
	private int         mActiveSumData;
	private int         mBlogSumData;
	private int         mRelationAction;	
	private int         mActiveDataState;
	private int         mBlogDataState;	
	private int         mUid;
	private int         mHisUid;
	private int         mPageSize = 20;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oschina_user_center);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView()
    {
    	mBack = (ImageView)findViewById(R.id.user_center_back);
    	mRefresh = (ImageView)findViewById(R.id.user_center_refresh);
    	mHeadTitle = (TextView)findViewById(R.id.user_center_head_title);
    	mProgressbar = (ProgressBar)findViewById(R.id.user_center_head_progress);
    	mRelation = (RadioButton)findViewById(R.id.user_center_footbar_relation);
    	mMessage = (RadioButton)findViewById(R.id.user_center_footbar_message);
    	mAtme = (RadioButton)findViewById(R.id.user_center_footbar_atme);
    	
    	mTabActive = (Button)findViewById(R.id.user_center_btn_active);
    	mTabBlog = (Button)findViewById(R.id.user_center_btn_blog);
    	
    	mUserface = (ImageView)mUserinfoDialog.findViewById(R.id.user_center_userface);
    	mUsername = (TextView)mUserinfoDialog.findViewById(R.id.user_center_username);
    	mFrom = (TextView)mUserinfoDialog.findViewById(R.id.user_center_from);
    	mGender = (TextView)mUserinfoDialog.findViewById(R.id.user_center_gender);
    	mJointime = (TextView)mUserinfoDialog.findViewById(R.id.user_center_jointime);
    	mDevplatform = (TextView)mUserinfoDialog.findViewById(R.id.user_center_devplatform);
    	mExpertise = (TextView)mUserinfoDialog.findViewById(R.id.user_center_expertise);
    	mLatestonline = (TextView)mUserinfoDialog.findViewById(R.id.user_center_latestonline);
    }  
	
	private void setListener() 
    {
    	mBack.setOnClickListener(this);
    	mRefresh.setOnClickListener(this);
    	mHeadTitle.setOnClickListener(this);
    	mMessage.setOnClickListener(this);
    	mAtme.setOnClickListener(this);
    	mUserinfoDialog.setOnCancelListener(this);
	}
    
	private void initData()
	{    	
		mHisUid = getIntent().getIntExtra("his_id", 0);
    	mHisName = getIntent().getStringExtra("his_name");
    	mUserName = getIntent().getStringExtra("his_name");
    	mUid = UserHelper.getLoginUid();
		
    	mUserinfoDialog = new UserInfoDialog(mContext);
    	mHeadTitle.setText(mUserName + " ▼");
    	mTabActive.setEnabled(false);
    	mTabActive.setOnClickListener(tabBtnClick(mTabActive));
    	mTabBlog.setOnClickListener(tabBtnClick(mTabBlog));
		
		initLvActive();
    	initLvBlog();
		
		loadActiveData(mUserHandler, 0 ,UIHelper.LISTVIEW_ACTION_INIT);
		loadBlogData(mBlogHandler, 0, UIHelper.LISTVIEW_ACTION_INIT);
	}
	
	//初始化动态列表控件
    private void initLvActive() 
    {
    	mActiveFooter = getLayoutInflater().inflate(R.layout.view_oschina_listview_footer, null);
    	mActiveFooterMore = (TextView)mActiveFooter.findViewById(R.id.listview_foot_more);
        mActiveFootProgress = (ProgressBar)mActiveFooter.findViewById(R.id.listview_foot_progress);

    	mActiveAdapter = new ActiveAdapter(this, mActiveData, R.layout.item_oschina_active); 
    	mActive = (PullToRefreshListView)findViewById(R.id.user_center_activelist);
        mActive.addFooterView(mActiveFooter);
        mActive.setAdapter(mActiveAdapter); 
        mActive.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        	{
        		//点击头部、底部栏无效
        		if(position == 0 || view == mActiveFooter) return;
        		ImageView img = (ImageView)view.findViewById(R.id.active_listitem_userface);
        		Active active = (Active)img.getTag();
        		UIHelper.showActiveRedirect(view.getContext(), active);
        	}
		});
        mActive.setOnScrollListener(new AbsListView.OnScrollListener() 
        {
			public void onScrollStateChanged(AbsListView view, int scrollState) 
			{
				mActive.onScrollStateChanged(view, scrollState);
				//数据为空--不用继续下面代码了
				if(mActiveData.size() == 0) return;
				//判断是否滚动到底部
				boolean scrollEnd = false;
				try 
				{
					if(view.getPositionForView(mActiveFooter) == view.getLastVisiblePosition()) scrollEnd = true;
				} 
				catch (Exception e) 
				{
					scrollEnd = false;
				}
				
				if(scrollEnd && mActiveDataState==UIHelper.LISTVIEW_DATA_MORE)
				{
					mActive.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					mActiveFooterMore.setText(R.string.load_ing);
					mActiveFootProgress.setVisibility(View.VISIBLE);
					int pageIndex = mActiveSumData/mPageSize;
					loadActiveData(mActiveHandler, pageIndex, UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}
			
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) 
			{
				mActive.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		});
        mActive.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() 
        {
            public void onRefresh() 
            {
				loadActiveData(mActiveHandler, 0, UIHelper.LISTVIEW_ACTION_REFRESH);
            }
        });
    }
    
    //初始化博客列表控件
    private void initLvBlog() 
    {
    	mBlogFooter = getLayoutInflater().inflate(R.layout.view_oschina_listview_footer, null);
    	mBlogFooterMore = (TextView)mBlogFooter.findViewById(R.id.listview_foot_more);
        mBlogFootProgress = (ProgressBar)mBlogFooter.findViewById(R.id.listview_foot_progress);

    	mBlogAdapter = new BlogAdapter(this, BlogList.CATALOG_USER, mBlogData, R.layout.item_oschina_blog); 
    	mBlog = (PullToRefreshListView)findViewById(R.id.user_center_bloglist);
        mBlog.addFooterView(mBlogFooter);
        mBlog.setAdapter(mBlogAdapter); 
        mBlog.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        	{
        		//点击头部、底部栏无效
        		if(position == 0 || view == mBlogFooter) return;
        		TextView txt = (TextView)view.findViewById(R.id.blog_listitem_title);
        		Blog blog = (Blog)txt.getTag();
        		UIHelper.showUrlRedirect(view.getContext(), blog.getUrl());
        	}
		});
        mBlog.setOnScrollListener(new AbsListView.OnScrollListener() 
        {
			public void onScrollStateChanged(AbsListView view, int scrollState) 
			{
				mBlog.onScrollStateChanged(view, scrollState);
				//数据为空--不用继续下面代码了
				if(mBlogData.size() == 0) return;
				//判断是否滚动到底部
				boolean scrollEnd = false;
				try 
				{
					if(view.getPositionForView(mBlogFooter) == view.getLastVisiblePosition()) scrollEnd = true;
				} 
				catch (Exception e) 
				{
					scrollEnd = false;
				}
				
				if(scrollEnd && mBlogDataState==UIHelper.LISTVIEW_DATA_MORE)
				{
					mBlog.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					mBlogFooterMore.setText(R.string.load_ing);
					mBlogFootProgress.setVisibility(View.VISIBLE);
					int pageIndex = mBlogSumData/mPageSize;
					loadBlogData(mBlogHandler, pageIndex, UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}
			
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) 
			{
				mBlog.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		});
        mBlog.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() 
        {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
			{
				//点击头部、底部栏无效
        		if(position == 0 || view == mBlogFooter) return false;
        		Blog _blog = null;
        		//判断是否是TextView
        		if(view instanceof TextView)
        		{
        			_blog = (Blog)view.getTag();
        		}
        		else
        		{
        			TextView txt = (TextView)view.findViewById(R.id.blog_listitem_title);
            		_blog = (Blog)txt.getTag();
        		} 
        		if(_blog == null) return false;
        		final Blog blog = _blog;
				//操作--删除
        		final int uid = UserHelper.getLoginUid();
        		//判断该博客是否是当前登录用户发表的
        		if(uid == blog.getAuthorId())
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
									mBlogData.remove(blog);
									mBlogAdapter.notifyDataSetChanged();
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
								Result res = UserHelper.delBlog(mContext, uid, blog.getAuthorId(), blog.getId());
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
	        		UIHelper.showBlogOptionDialog(mContext, thread);
        		}
        		else
        		{
        			UIHelper.showBlogOptionDialog(mContext, null);
        		}
				return true;
			}        	
		});
        mBlog.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() 
        {
            public void onRefresh() 
            {
				loadBlogData(mBlogHandler, 0, UIHelper.LISTVIEW_ACTION_REFRESH);
            }
        });
    }

	//加载动态列表
	private void loadActiveData(final Handler handler, final int pageIndex, final int action) 
	{
		headButtonSwitch(DATA_LOAD_ING);
		new Thread() 
		{
			public void run() 
			{
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL) isRefresh = true;
				try 
				{
					UserInformation uinfo = UserHelper.getInformation(mContext, mUid, mHisUid, mHisName, pageIndex, isRefresh);
					mUser = uinfo.getUser();
					msg.what = uinfo.getPageSize();
					msg.obj = uinfo;
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
	
	//加载博客列表
	private void loadBlogData(final Handler handler, final int pageIndex, final int action) 
	{
		headButtonSwitch(DATA_LOAD_ING);
		new Thread() 
		{
			public void run() 
			{
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL) isRefresh = true;
				try 
				{
					BlogList bloglist = UserHelper.getUserBlogList(mContext, mHisUid, mHisName, pageIndex, isRefresh);
					msg.what = bloglist.getPageSize();
					msg.obj = bloglist;
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
	
	private void loadUserRelation(int relation)
	{
		switch (relation) 
		{
		case User.RELATION_TYPE_BOTH: 
		{
			mRelation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.oschina_bar_relation_del, 0, 0, 0);
			mRelation.setText("取消互粉");
			break;
		}
		case User.RELATION_TYPE_FANS_HIM: 
		{
			mRelation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.oschina_bar_relation_del, 0, 0, 0);
			mRelation.setText("取消关注");
			break;
		}
		case User.RELATION_TYPE_FANS_ME: 
		{
			mRelation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.oschina_bar_relation_add, 0, 0, 0);
			mRelation.setText("加关注");
			break;
		}
		case User.RELATION_TYPE_NULL: 
		{
			mRelation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.oschina_bar_relation_add, 0, 0, 0);
			mRelation.setText("加关注");
			break;
		}
		default:
			break;
		}
		if(relation > 0) mRelation.setOnClickListener(this);
	}
	
	@SuppressWarnings("deprecation")
	private void activeHandleMessage(Message msg)
	{
		if(msg.what >= 0)
		{
			UserInformation uinfo = (UserInformation)msg.obj;
			Notice notice = uinfo.getNotice();	
			switch (msg.arg1) 
			{
			case UIHelper.LISTVIEW_ACTION_INIT:
			case UIHelper.LISTVIEW_ACTION_REFRESH:
			case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
			{
				mActiveSumData = msg.what;
				mActiveData.clear();
				mActiveData.addAll(uinfo.getActivelist());
				break;
			}
			case UIHelper.LISTVIEW_ACTION_SCROLL:
			{
				mActiveSumData += msg.what;
				if(mActiveData.size() > 0)
				{
					for(Active active1 : uinfo.getActivelist())
					{
						boolean b = false;
						for(Active active2 : mActiveData)
						{
							if(active1.getId() == active2.getId())
							{
								b = true;
								break;
							}
						}
						if(!b) mActiveData.add(active1);
					}
				}
				else
				{
					mActiveData.addAll(uinfo.getActivelist());
				}
				break;
			}
			default:
				break;
			}
			if(msg.what<mPageSize)
			{
				mActiveDataState = UIHelper.LISTVIEW_DATA_FULL;
				mActiveAdapter.notifyDataSetChanged();
				mActiveFooterMore.setText(R.string.load_full);
			}
			else if(msg.what == mPageSize)
			{	
				mActiveDataState = UIHelper.LISTVIEW_DATA_MORE;
				mActiveAdapter.notifyDataSetChanged();
				mActiveFooterMore.setText(R.string.load_more);
			}
			//发送通知广播
			if(msg.obj != null)
			{
				UIHelper.sendBroadCast(mContext, notice);
			}
		}
		else if(msg.what == -1)
		{
			//有异常--显示加载出错 & 弹出错误消息
			mActiveDataState = UIHelper.LISTVIEW_DATA_MORE;
			mActiveFooterMore.setText(R.string.load_error);
		}
		if(mActiveData.size()==0)
		{
			mActiveDataState = UIHelper.LISTVIEW_DATA_EMPTY;
			mActiveFooterMore.setText(R.string.load_empty);
		}
		mActiveFootProgress.setVisibility(View.GONE);
		if(msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH)
		{
			mActive.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
			mActive.setSelection(0);
		}
		else if(msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG)
		{
			mActive.setSelection(0);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void blogHandleMessage(Message msg)
	{
		if(msg.what >= 0)
		{
			BlogList bloglist = (BlogList)msg.obj;
			Notice notice = bloglist.getNotice();
			//显示用户博客数量
			String tabBlogText = String.format(mContext.getResources().getString(R.string.user_blog), bloglist.getBlogsCount());
			mTabBlog.setText(tabBlogText);		
			switch (msg.arg1) 
			{
			case UIHelper.LISTVIEW_ACTION_INIT:
			case UIHelper.LISTVIEW_ACTION_REFRESH:
			case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
			{
				mBlogSumData = msg.what;
				mBlogData.clear();
				mBlogData.addAll(bloglist.getBloglist());
				break;
			}
			case UIHelper.LISTVIEW_ACTION_SCROLL:
			{
				mBlogSumData += msg.what;
				if(mBlogData.size() > 0)
				{
					for(Blog blog1 : bloglist.getBloglist())
					{
						boolean b = false;
						for(Blog blog2 : mBlogData)
						{
							if(blog1.getId() == blog2.getId())
							{
								b = true;
								break;
							}
						}
						if(!b) mBlogData.add(blog1);
					}
				}
				else
				{
					mBlogData.addAll(bloglist.getBloglist());
				}
				break;
			}
			default:
				break;
			}
			
			if(msg.what<mPageSize)
			{
				mBlogDataState = UIHelper.LISTVIEW_DATA_FULL;
				mBlogAdapter.notifyDataSetChanged();
				mBlogFooterMore.setText(R.string.load_full);
			}
			else if(msg.what == mPageSize)
			{
				mBlogDataState = UIHelper.LISTVIEW_DATA_MORE;
				mBlogAdapter.notifyDataSetChanged();
				mBlogFooterMore.setText(R.string.load_more);
			}
			//发送通知广播
			if(msg.obj != null)
			{
				UIHelper.sendBroadCast(mContext, notice);
			}
		}
		else if(msg.what == -1)
		{
			//有异常--显示加载出错 & 弹出错误消息
			mBlogDataState = UIHelper.LISTVIEW_DATA_MORE;
			mBlogFooterMore.setText(R.string.load_error);
		}
		if(mBlogData.size()==0)
		{
			mBlogDataState = UIHelper.LISTVIEW_DATA_EMPTY;
			mBlogFooterMore.setText(R.string.load_empty);
		}
		mBlogFootProgress.setVisibility(View.GONE);
		if(msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH)
		{
			mBlog.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
			mBlog.setSelection(0);
		}
		else if(msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG)
		{
			mBlog.setSelection(0);
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

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.user_center_back:
		{
			finish();
			break;
		}
		case R.id.user_center_refresh:
		{
			loadActiveData(mUserHandler, 0 ,UIHelper.LISTVIEW_ACTION_REFRESH);
			loadBlogData(mBlogHandler, 0, UIHelper.LISTVIEW_ACTION_REFRESH);
			break;
		}
		case R.id.user_center_head_title:
		{
			if(mUserinfoDialog != null && mUserinfoDialog.isShowing())
			{
				mHeadTitle.setText(mUserName + " ▼");
				mUserinfoDialog.hide();
			}
			else
			{
				mHeadTitle.setText(mUserName + " ▲");				
				mUserinfoDialog.show();
			}
			break;
		}
		case R.id.user_center_footbar_message:
		{
			UIHelper.showMessagePub(OSChinaUserCenterActivity.this, mUser.getUid(), mUser.getName());
			break;
		}
		case R.id.user_center_footbar_atme:
		{
			UIHelper.showTweetPub(OSChinaUserCenterActivity.this, "@"+mUser.getName()+" ", mUser.getUid());
			break;
		}
		case R.id.user_center_footbar_relation:
		{
			//判断登录
			if(!UserHelper.isLogin())
			{
				UIHelper.showLoginDialog(mContext);
				return;
			}
			
			final Handler handler = new Handler()
			{
				public void handleMessage(Message msg) 
				{
					if(msg.what == 1)
					{
						Result res = (Result)msg.obj;
						if(res.OK())
						{
							switch (mUser.getRelation()) 
							{
							case User.RELATION_TYPE_BOTH:
							{
								mRelation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.oschina_bar_relation_add, 0, 0, 0);
								mRelation.setText("加关注");
								mUser.setRelation(User.RELATION_TYPE_FANS_ME);
								break;
							}
							case User.RELATION_TYPE_FANS_HIM:
							{
								mRelation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.oschina_bar_relation_add, 0, 0, 0);
								mRelation.setText("加关注");
								mUser.setRelation(User.RELATION_TYPE_NULL);
								break;
							}
							case User.RELATION_TYPE_FANS_ME:
							{
								mRelation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.oschina_bar_relation_del, 0, 0, 0);
								mRelation.setText("取消互粉");
								mUser.setRelation(User.RELATION_TYPE_BOTH);
								break;
							}
							case User.RELATION_TYPE_NULL:
							{
								mRelation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.oschina_bar_relation_del, 0, 0, 0);
								mRelation.setText("取消关注");
								mUser.setRelation(User.RELATION_TYPE_FANS_HIM);
								break;
							}
							default:
								break;
							}
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
						Result res = UserHelper.updateRelation(mContext, mUid, mHisUid, mRelationAction);
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
			String dialogTitle = "";
			switch (mUser.getRelation()) 
			{
			case User.RELATION_TYPE_BOTH:
			{
				dialogTitle = "确定取消互粉吗？";
				mRelationAction = User.RELATION_ACTION_DELETE;
				break;
			}
			case User.RELATION_TYPE_FANS_HIM:
			{
				dialogTitle = "确定取消关注吗？";
				mRelationAction = User.RELATION_ACTION_DELETE;
				break;
			}
			case User.RELATION_TYPE_FANS_ME:
			{
				dialogTitle = "确定关注他吗？";
				mRelationAction = User.RELATION_ACTION_ADD;
				break;
			}
			case User.RELATION_TYPE_NULL:
			{
				dialogTitle = "确定关注他吗？";
				mRelationAction = User.RELATION_ACTION_ADD;
				break;
			}
			default:
				break;
			}
			new AlertDialog.Builder(v.getContext())
			.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle(dialogTitle)
			.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					thread.start();
					dialog.dismiss();
				}
			})
			.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.dismiss();
				}
			})
			.create().show();
			break;
		}
		default:
			break;
		}
	}
	
	private View.OnClickListener tabBtnClick(final Button btn)
	{
    	return new View.OnClickListener() 
    	{
			public void onClick(View v) 
			{
		    	if(btn == mTabActive)
		    	{
		    		mTabActive.setEnabled(false);
		    	}
		    	else
		    	{
		    		mTabActive.setEnabled(true);
		    	}
		    	
		    	if(btn == mTabBlog)
		    	{
		    		mTabBlog.setEnabled(false);
		    	}
		    	else
		    	{
		    		mTabBlog.setEnabled(true);
		    	}	    	
				
				if(btn == mTabActive)
				{
					mActive.setVisibility(View.VISIBLE);
					mBlog.setVisibility(View.GONE);
					if(mActiveData.size() == 0) loadActiveData(mActiveHandler, 0, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
				}
				else
				{
					mActive.setVisibility(View.GONE);
					mBlog.setVisibility(View.VISIBLE);
					if(mBlogData.size() == 0) loadBlogData(mBlogHandler, 0, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
				}
			}
		};
    }
	
	@Override
	public void onCancel(DialogInterface dialog) 
	{
		mHeadTitle.setText(mUserName + " ▼");
	}
	
	private Handler mActiveHandler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			headButtonSwitch(DATA_LOAD_COMPLETE);
			activeHandleMessage(msg);
		}
	};

	private Handler mBlogHandler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			headButtonSwitch(DATA_LOAD_COMPLETE);
			blogHandleMessage(msg);
		}
	};

	private Handler mUserHandler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			headButtonSwitch(DATA_LOAD_COMPLETE);
			if (mUser != null) 
			{
				mUserName = mUser.getName();
				mHeadTitle.setText(mUserName + " ▼");
				mUsername.setText(mUser.getName());
				mFrom.setText(mUser.getLocation());
				mGender.setText(mUser.getGender());
				mJointime.setText(StringUtils.friendly_time(mUser.getJointime()));
				mDevplatform.setText(mUser.getDevplatform());
				mExpertise.setText(mUser.getExpertise());
				mLatestonline.setText(StringUtils.friendly_time(mUser.getLatestonline()));
				//初始化用户关系 & 点击事件
				loadUserRelation(mUser.getRelation());
				//加载用户头像
				UIHelper.showUserFace(mUserface, mUser.getFace());
			}
			activeHandleMessage(msg);
		}
	};
}
