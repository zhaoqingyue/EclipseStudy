/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaSearchActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 搜索
 * @author: zhaoqy
 * @date: 2015-11-25 上午11:10:12
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.adapter.SearchAdapter;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.Notice;
import com.zhaoqy.app.demo.oschina.item.SearchList;
import com.zhaoqy.app.demo.oschina.item.SearchList.Result;
import com.zhaoqy.app.demo.oschina.util.StringUtils;

@SuppressLint("HandlerLeak")
public class OSChinaSearchActivity extends OSChinaBaseActivity implements OnClickListener, OnFocusChangeListener, OnKeyListener, OnItemClickListener, OnScrollListener
{
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private InputMethodManager mInputMethodManager;
	private Context       mContext;
	private Button        mSearchBtn;
	private EditText      mSearchEditer;
	private ProgressBar   mProgressbar;
	private Button        mSearchSoft;
	private Button        mSearchPost;
	private Button        mSearchCode;
	private Button        mSearchBlog;
	private Button        mSearchNews;
	private ListView      mSearch;
	private SearchAdapter mSearchAdapter;
	private List<Result>  mSearchData = new ArrayList<Result>();
	private View          mSearchFooter;
	private TextView      mSearchFootMore;
	private ProgressBar   mSearchFootProgress;
	private String        mSearchCatalog = SearchList.CATALOG_SOFTWARE;
	private String        mSearchContent = "";
    private int           mSumData;
	private int           mDataState;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oschina_search);
        mContext = this;

        initView();
        setListener();
        initData();
	}

	private void initView()
    {
    	mSearchBtn = (Button)findViewById(R.id.search_btn);
    	mSearchEditer = (EditText)findViewById(R.id.search_editer);
    	mProgressbar = (ProgressBar)findViewById(R.id.search_progress);
    	
    	mSearchSoft = (Button)findViewById(R.id.search_catalog_software);
    	mSearchPost = (Button)findViewById(R.id.search_catalog_post);
    	mSearchCode = (Button)findViewById(R.id.search_catalog_code);
    	mSearchBlog = (Button)findViewById(R.id.search_catalog_blog);
    	mSearchNews = (Button)findViewById(R.id.search_catalog_news);
    	
    	mSearchFooter = getLayoutInflater().inflate(R.layout.view_oschina_listview_footer, null);
    	mSearchFootMore = (TextView)mSearchFooter.findViewById(R.id.listview_foot_more);
    	mSearchFootProgress = (ProgressBar)mSearchFooter.findViewById(R.id.listview_foot_progress);
    	mSearch = (ListView)findViewById(R.id.search_listview);
    }
	
	private void setListener() 
    {
    	mSearchBtn.setOnClickListener(this);
    	mSearchEditer.setOnFocusChangeListener(this); 
    	mSearchEditer.setOnKeyListener(this);
    	mSearch.setOnItemClickListener(this);
    	mSearch.setOnScrollListener(this);
    	mSearchSoft.setOnClickListener(searchBtnClick(mSearchSoft, SearchList.CATALOG_SOFTWARE));
    	mSearchPost.setOnClickListener(searchBtnClick(mSearchPost, SearchList.CATALOG_POST));
    	mSearchCode.setOnClickListener(searchBtnClick(mSearchCode, SearchList.CATALOG_CODE));
    	mSearchBlog.setOnClickListener(searchBtnClick(mSearchBlog, SearchList.CATALOG_BLOG));
    	mSearchNews.setOnClickListener(searchBtnClick(mSearchNews, SearchList.CATALOG_NEWS));
	}
    
  	private void initData()
  	{			
  		mInputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
  		mSearchSoft.setEnabled(false);
  		mSearchAdapter = new SearchAdapter(mContext, mSearchData, R.layout.item_oschina_search); 
  		
  		mSearch.setVisibility(ListView.GONE);
    	mSearch.addFooterView(mSearchFooter);
    	mSearch.setAdapter(mSearchAdapter); 
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
    		mSearchBtn.setClickable(false);
			mProgressbar.setVisibility(View.VISIBLE);
			break;
    	}
		case DATA_LOAD_COMPLETE:
		{
			mSearchBtn.setClickable(true);
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
	private void loadSearchData(final String catalog,final int pageIndex,final Handler handler,final int action)
	{  
		if(StringUtils.isEmpty(mSearchContent))
		{
			UIHelper.ToastMessage(mContext, "请输入搜索内容");
			return;
		}
		headButtonSwitch(DATA_LOAD_ING);
		mSearch.setVisibility(ListView.VISIBLE);
		
		new Thread()
		{
			public void run() 
			{
				Message msg = new Message();
				try 
				{
					SearchList searchList = UserHelper.getSearchList(mContext, catalog, mSearchContent, pageIndex, 20);
					msg.what = searchList.getPageSize();
					msg.obj = searchList;
	            } 
				catch (Exception e) 
				{
	            	e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }
				msg.arg1 = action;
				if(mSearchCatalog.equals(catalog)) handler.sendMessage(msg);
			}
		}.start();
	} 
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.search_btn:
		{
			mSearchEditer.clearFocus();
			mSearchContent = mSearchEditer.getText().toString();
			loadSearchData(mSearchCatalog, 0, mSearchHandler, UIHelper.LISTVIEW_ACTION_INIT);
			break;
		}
		default:
			break;
		}
	}
	
	private View.OnClickListener searchBtnClick(final Button btn, final String catalog)
	{
    	return new View.OnClickListener() 
    	{
			public void onClick(View v) 
			{
		    	if(btn == mSearchBlog)
		    	{
		    		mSearchBlog.setEnabled(false);
		    	}
		    	else
		    	{
		    		mSearchBlog.setEnabled(true);
		    	}
		    		
		    	if(btn == mSearchCode)
		    	{
		    		mSearchCode.setEnabled(false);
		    	}
		    	else
		    	{
		    		mSearchCode.setEnabled(true);	
		    	}
		    		
		    	if(btn == mSearchNews)
		    	{
		    		mSearchNews.setEnabled(false);
		    	}
		    	else
		    	{
		    		mSearchNews.setEnabled(true);
		    	}
		    		
		    	if(btn == mSearchPost)
		    	{
		    		mSearchPost.setEnabled(false);
		    	}
		    	else
		    	{
		    		mSearchPost.setEnabled(true);
		    	}
		    		
		    	if(btn == mSearchSoft)
		    	{
		    		mSearchSoft.setEnabled(false);
		    	}
		    	else
		    	{
		    		mSearchSoft.setEnabled(true);
		    	}
		    		
				//开始搜索
				mSearchEditer.clearFocus();
				mSearchContent = mSearchEditer.getText().toString();
				mSearchCatalog = catalog;
				loadSearchData(catalog, 0, mSearchHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);		    	
			}
		};
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
		if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH) 
		{
			if(v.getTag() == null) 
			{
				v.setTag(1);
				mSearchEditer.clearFocus();
				mSearchContent = mSearchEditer.getText().toString();
				loadSearchData(mSearchCatalog, 0, mSearchHandler, UIHelper.LISTVIEW_ACTION_INIT);						
			}
			else
			{
				v.setTag(null);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		//点击底部栏无效
		if(view == mSearchFooter) return;
		Result res = null;
		//判断是否是TextView
		if(view instanceof TextView)
		{
			res = (Result)view.getTag();
		}
		else
		{
			TextView title = (TextView)view.findViewById(R.id.search_listitem_title);
			res = (Result)title.getTag();
		} 
		if(res == null) return;
		UIHelper.showUrlRedirect(view.getContext(), res.getUrl());
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) 
	{
		//数据为空--不用继续下面代码了
		if(mSearchData.size() == 0) return;
		//判断是否滚动到底部
		boolean scrollEnd = false;
		try 
		{
			if(view.getPositionForView(mSearchFooter) == view.getLastVisiblePosition()) scrollEnd = true;
		} catch (Exception e) 
		{
			scrollEnd = false;
		}
		
		if(scrollEnd && mDataState==UIHelper.LISTVIEW_DATA_MORE)
		{
			mSearch.setTag(UIHelper.LISTVIEW_DATA_LOADING);
			mSearchFootMore.setText(R.string.load_ing);
			mSearchFootProgress.setVisibility(View.VISIBLE);
			//当前pageIndex
			int pageIndex = mSumData/20;
			loadSearchData(mSearchCatalog, pageIndex, mSearchHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
	{
	}
	
	private Handler mSearchHandler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			headButtonSwitch(DATA_LOAD_COMPLETE);
			if(msg.what >= 0)
			{						
				SearchList list = (SearchList)msg.obj;
				Notice notice = list.getNotice();
				//处理listview数据
				switch (msg.arg1) 
				{
				case UIHelper.LISTVIEW_ACTION_INIT:
				case UIHelper.LISTVIEW_ACTION_REFRESH:
				case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
				{
					mSumData = msg.what;
					mSearchData.clear();
					mSearchData.addAll(list.getResultlist());
					break;
				}
				case UIHelper.LISTVIEW_ACTION_SCROLL:
				{
					mSumData += msg.what;
					if(mSearchData.size() > 0)
					{
						for(Result res1 : list.getResultlist())
						{
							boolean b = false;
							for(Result res2 : mSearchData)
							{
								if(res1.getObjid() == res2.getObjid())
								{
									b = true;
									break;
								}
							}
							if(!b) mSearchData.add(res1);
						}
					}
					else
					{
						mSearchData.addAll(list.getResultlist());
					}
					break;
				}
				default:
					break;
				}	
				
				if(msg.what < 20)
				{
					mDataState = UIHelper.LISTVIEW_DATA_FULL;
					mSearchAdapter.notifyDataSetChanged();
					mSearchFootMore.setText(R.string.load_full);
				}
				else if(msg.what == 20)
				{					
					mDataState = UIHelper.LISTVIEW_DATA_MORE;
					mSearchAdapter.notifyDataSetChanged();
					mSearchFootMore.setText(R.string.load_more);
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
				mDataState = UIHelper.LISTVIEW_DATA_MORE;
				mSearchFootMore.setText(R.string.load_error);
			}
			if(mSearchData.size()==0)
			{
				mDataState = UIHelper.LISTVIEW_DATA_EMPTY;
				mSearchFootMore.setText(R.string.load_empty);
			}
			mSearchFootProgress.setVisibility(View.GONE);
			if(msg.arg1 != UIHelper.LISTVIEW_ACTION_SCROLL)
			{
				mSearch.setSelection(0);//返回头部
			}
		}
	};
}
