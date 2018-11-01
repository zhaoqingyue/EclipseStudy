/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaUserFavoriteActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 用户收藏
 * @author: zhaoqy
 * @date: 2015-11-19 下午5:45:36
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.adapter.FavoriteAdapter;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.FavoriteList;
import com.zhaoqy.app.demo.oschina.item.FavoriteList.Favorite;
import com.zhaoqy.app.demo.oschina.item.Notice;
import com.zhaoqy.app.demo.oschina.item.Result;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView.OnRefreshListener;

public class OSChinaUserFavoriteActivity extends OSChinaBaseActivity implements OnClickListener, OnItemClickListener, OnScrollListener, OnItemLongClickListener, OnRefreshListener
{
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private PullToRefreshListView mFavorite;
	private FavoriteAdapter mAdapter;
	private List<Favorite>  mDatas = new ArrayList<Favorite>();
	private ProgressBar     mProgressbar;
	private Context         mContext;
	private ImageView       mBack;
	private Button          mSoft;
	private Button          mPost;
	private Button          mCode;
	private Button          mBlog;
	private Button          mNews;
	private View            mFooter;
	private TextView        mFootMore;
	private ProgressBar     mFootProgress;
    private int             mSumData;
	private int             mCurCatalog;
	private int             mCurState;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oschina_user_favorite);
        mContext = this;
        
        initView();
        setListener();
        initData();
	}
	
    private void initView()
    {
    	mBack = (ImageView)findViewById(R.id.favorite_head_back);
    	mProgressbar = (ProgressBar)findViewById(R.id.favorite_head_progress);
    	mSoft = (Button)findViewById(R.id.favorite_catalog_software);
    	mPost = (Button)findViewById(R.id.favorite_catalog_post);
    	mCode = (Button)findViewById(R.id.favorite_catalog_code);
    	mBlog = (Button)findViewById(R.id.favorite_catalog_blog);
    	mNews = (Button)findViewById(R.id.favorite_catalog_news);
    	mFooter = getLayoutInflater().inflate(R.layout.view_oschina_footer, null);
    	mFootMore = (TextView)mFooter.findViewById(R.id.listview_foot_more);
    	mFootProgress = (ProgressBar)mFooter.findViewById(R.id.listview_foot_progress);
    	mFavorite = (PullToRefreshListView)findViewById(R.id.favorite_listview);
    }
    
    private void setListener() 
    {
    	mBack.setOnClickListener(this);
    	mFavorite.setOnItemClickListener(this);
    	mFavorite.setOnScrollListener(this);
    	mFavorite.setOnItemLongClickListener(this);
    	mFavorite.setOnRefreshListener(this);
    	mSoft.setOnClickListener(favoriteBtnClick(mSoft, FavoriteList.TYPE_SOFTWARE));
    	mPost.setOnClickListener(favoriteBtnClick(mPost, FavoriteList.TYPE_POST));
    	mCode.setOnClickListener(favoriteBtnClick(mCode, FavoriteList.TYPE_CODE));
    	mBlog.setOnClickListener(favoriteBtnClick(mBlog, FavoriteList.TYPE_BLOG));
    	mNews.setOnClickListener(favoriteBtnClick(mNews, FavoriteList.TYPE_NEWS));
	}
    
  	private void initData()
  	{			
  		mSoft.setEnabled(false);
    	mCurCatalog = FavoriteList.TYPE_SOFTWARE;
    	mAdapter = new FavoriteAdapter(mContext, mDatas, R.layout.item_oschina_favorite); 
    	mFavorite.addFooterView(mFooter);//添加底部视图  必须在setAdapter前
    	mFavorite.setAdapter(mAdapter); 
    	loadFavoriteData(mCurCatalog, 0, mHandler, UIHelper.LISTVIEW_ACTION_INIT);
  	}
  	
  	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.favorite_head_back:
		{
			finish();
			break;
		}
		default:
			break;
		}
	}
  	
  	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
  	{
  		//点击头部、底部栏无效
		if(position == 0 || view == mFooter) return;
		Favorite fav = null;
		//判断是否是TextView
		if(view instanceof TextView)
		{
			fav = (Favorite)view.getTag();
		}
		else
		{
			TextView title = (TextView)view.findViewById(R.id.favorite_listitem_title);
			fav = (Favorite)title.getTag();
		} 
		if(fav == null) return;
		UIHelper.showUrlRedirect(mContext, fav.url);
	}
  	
  	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
  	{
  		mFavorite.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
	}
  	
  	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) 
  	{
  		mFavorite.onScrollStateChanged(view, scrollState);
		//数据为空
		if(mDatas.size() == 0) return;
		//判断是否滚动到底部
		boolean scrollEnd = false;
		try 
		{
			if(view.getPositionForView(mFooter) == view.getLastVisiblePosition())
				scrollEnd = true;
		} 
		catch (Exception e) 
		{
			scrollEnd = false;
		}
		
		if(scrollEnd && mCurState==UIHelper.LISTVIEW_DATA_MORE)
		{
			mFavorite.setTag(UIHelper.LISTVIEW_DATA_LOADING);
			mFootMore.setText("加载中···");
			mFootProgress.setVisibility(View.VISIBLE);
			//当前pageIndex
			int pageIndex = mSumData/20;
			loadFavoriteData(mCurCatalog, pageIndex, mHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
		}
	}
  	
	@SuppressLint("HandlerLeak")
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
	{
		//点击头部、底部栏无效
		if(position == 0 || view == mFooter) return false;				
		Favorite _fav = null;
		//判断是否是TextView
		if(view instanceof TextView)
		{
			_fav = (Favorite)view.getTag();
		}
		else
		{
			TextView title = (TextView)view.findViewById(R.id.favorite_listitem_title);
    		_fav = (Favorite)title.getTag();
		} 
		if(_fav == null) return false;
		final Favorite fav = _fav;
		//操作--删除
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
						mDatas.remove(fav);
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
					Result res = UserHelper.delFavorite(mContext, uid, fav.objid, fav.type);
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
		UIHelper.showFavoriteOptionDialog(mContext, thread);
		return true;
	}

	@Override
	public void onRefresh() 
	{
		loadFavoriteData(mCurCatalog, 0, mHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
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
			break;
    	}
		case DATA_LOAD_COMPLETE:
		{
			mProgressbar.setVisibility(View.GONE);
			break;
		}
		default:
			break;
		}
    }
  	
    /**
     * 线程加载收藏数据
     * @param type 0:全部收藏 1:软件 2:话题 3:博客 4:新闻 5:代码
     * @param pageIndex 当前页数
     * @param handler 处理器
     * @param action 动作标识
     */
	private void loadFavoriteData(final int type, final int pageIndex, final Handler handler, final int action)
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
					FavoriteList favoriteList = UserHelper.getFavoriteList(mContext, type, pageIndex, isRefresh);
					msg.what = favoriteList.getPageSize();
					msg.obj = favoriteList;
	            } 
				catch (Exception e) 
				{
	            	e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }
				//告知handler当前action
				msg.arg1 = action;
				if(mCurCatalog == type)
				{
					handler.sendMessage(msg);
				}
			}
		}.start();
	} 
	
	private View.OnClickListener favoriteBtnClick(final Button btn, final int catalog)
	{
    	return new View.OnClickListener() 
    	{
			public void onClick(View v) 
			{
		    	if(btn == mBlog)
		    	{
		    		mBlog.setEnabled(false);
		    	}
		    	else
		    	{
		    		mBlog.setEnabled(true);
		    	}
		    	
		    	if(btn == mCode)
		    	{
		    		mCode.setEnabled(false);
		    	}
		    	else
		    	{
		    		mCode.setEnabled(true);	
		    	}
		    	
		    	if(btn == mNews)
		    	{
		    		mNews.setEnabled(false);
		    	}
		    	else
		    	{
		    		mNews.setEnabled(true);
		    	}
		    		
		    	if(btn == mPost)
		    	{
		    		mPost.setEnabled(false);
		    	}
		    	else
		    	{
		    		mPost.setEnabled(true);
		    	}
		    		
		    	if(btn == mSoft)
		    	{
		    		mSoft.setEnabled(false);
		    	}
		    	else
		    	{
		    		mSoft.setEnabled(true);		 
		    	}   	
		    	
				mFootMore.setText("更多");
				mFootProgress.setVisibility(View.GONE);
				mCurCatalog = catalog;
				loadFavoriteData(mCurCatalog, 0, mHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);		    	
			}
		};
    }

	 @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) 
		{
			headButtonSwitch(DATA_LOAD_COMPLETE);
			if(msg.what >= 0)
			{						
				FavoriteList list = (FavoriteList)msg.obj;
				Notice notice = list.getNotice();
				switch (msg.arg1) 
				{
				case UIHelper.LISTVIEW_ACTION_INIT:
				case UIHelper.LISTVIEW_ACTION_REFRESH:
				case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
				{
					mSumData = msg.what;
					mDatas.clear();
					mDatas.addAll(list.getFavoritelist());
					break;
				}	
				case UIHelper.LISTVIEW_ACTION_SCROLL:
				{
					mSumData += msg.what;
					if(mDatas.size() > 0)
					{
						for(Favorite fav1 : list.getFavoritelist())
						{
							boolean b = false;
							for(Favorite fav2 : mDatas)
							{
								if(fav1.objid == fav2.objid)
								{
									b = true;
									break;
								}
							}
							if(!b) mDatas.add(fav1);
						}
					}
					else
					{
						mDatas.addAll(list.getFavoritelist());
					}
					break;
				}
				default:
					break;
				}
				
				if(msg.what < 20)
				{
					mCurState = UIHelper.LISTVIEW_DATA_FULL;
					mAdapter.notifyDataSetChanged();
					mFootMore.setText("已加载全部");
				}
				else if(msg.what == 20)
				{					
					mCurState = UIHelper.LISTVIEW_DATA_MORE;
					mAdapter.notifyDataSetChanged();
					mFootMore.setText("更多");
				}
				if(notice != null)
				{
					UIHelper.sendBroadCast(mContext, notice);
				}
			}
			else if(msg.what == -1)
			{
				//有异常--显示加载出错 & 弹出错误消息
				mCurState = UIHelper.LISTVIEW_DATA_MORE;
				mFootMore.setText("加载数据出错");
				Toast.makeText(mContext, "加载数据出错", Toast.LENGTH_SHORT).show();
			}
			if(mDatas.size()==0)
			{
				mCurState = UIHelper.LISTVIEW_DATA_EMPTY;
				mFootMore.setText("暂无数据");
			}
			mFootProgress.setVisibility(View.GONE);
			if(msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH)
			{
				mFavorite.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
				mFavorite.setSelection(0);
			}
			else if(msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG)
			{
				mFavorite.onRefreshComplete();
				mFavorite.setSelection(0);
			}
		}
	};
}
