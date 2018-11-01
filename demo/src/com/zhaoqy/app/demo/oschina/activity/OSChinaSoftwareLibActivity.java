/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaSoftwareLibActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 软件库
 * @author: zhaoqy
 * @date: 2015-11-25 上午11:50:05
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.adapter.SoftwareAdapter;
import com.zhaoqy.app.demo.oschina.adapter.SoftwareCatelogAdapter;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.Notice;
import com.zhaoqy.app.demo.oschina.item.SoftwareCatalogList;
import com.zhaoqy.app.demo.oschina.item.SoftwareCatalogList.SoftwareType;
import com.zhaoqy.app.demo.oschina.item.SoftwareList;
import com.zhaoqy.app.demo.oschina.item.SoftwareList.Software;
import com.zhaoqy.app.demo.oschina.util.StringUtils;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView.OnRefreshListener;
import com.zhaoqy.app.demo.oschina.view.ScrollLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class OSChinaSoftwareLibActivity extends OSChinaBaseActivity implements OnClickListener, OnItemClickListener, OnScrollListener, OnRefreshListener
{
	private final static int HEAD_TAG_CATALOG = 0x001;
	private final static int HEAD_TAG_RECOMMEND = 0x002;
	private final static int HEAD_TAG_LASTEST = 0x003;
	private final static int HEAD_TAG_HOT = 0x004;
	private final static int HEAD_TAG_CHINA = 0x005;
	
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	
	private final static int SCREEN_CATALOG = 0;
	private final static int SCREEN_TAG = 1;
	private final static int SCREEN_SOFTWARE = 2;
	
	private Context                mContext;
	private ImageView              mBack;
	private TextView               mTitle;
	private ProgressBar            mProgressbar;
	private ScrollLayout           mScrollLayout;	
	private Button                 mButtonCatalog;
	private Button                 mButtonRecommend;
	private Button                 mButtonLastest;
	private Button                 mButtonHot;
	private Button                 mButtonChina;
	private PullToRefreshListView  mSoftware;
	private SoftwareAdapter        mSoftwareAdapter;
	private List<Software>         mSoftwareData = new ArrayList<Software>();
	private View                   mSoftwareFooter;
	private TextView               mSoftwareFootMore;
	private ProgressBar            mSoftwareFootProgress;
	private ListView               mSoftwareCatalog;
	private SoftwareCatelogAdapter mSoftwareCatalogAdapter;
	private List<SoftwareType>     mSoftwareCatalogData = new ArrayList<SoftwareType>();
	private ListView               mSoftwareTag;
	private SoftwareCatelogAdapter mSoftwareTagAdapter;
	private List<SoftwareType>     mSoftwareTagData = new ArrayList<SoftwareType>();
	private String                 mCurTitle;
	private int                    mSumData;
	private int                    mCurHeadTag = HEAD_TAG_CATALOG;
	private int                    mCurScreen = SCREEN_CATALOG;
	private int                    mCurSearchTag;
	private int                    mCurSoftwareDataState;
    
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oschina_software_lib);
        mContext = this;
        
        initView();
        setListener();
        initData();
	}
	
	private void initView()
    {
    	mBack = (ImageView)findViewById(R.id.frame_software_head_back);
    	mTitle = (TextView)findViewById(R.id.frame_software_head_title);
    	mProgressbar = (ProgressBar)findViewById(R.id.frame_software_head_progress);
    	mScrollLayout = (ScrollLayout) findViewById(R.id.frame_software_scrolllayout);
    	
    	mButtonCatalog = (Button)findViewById(R.id.frame_btn_software_catalog);
    	mButtonRecommend = (Button)findViewById(R.id.frame_btn_software_recommend);
    	mButtonLastest = (Button)findViewById(R.id.frame_btn_software_lastest);
    	mButtonHot = (Button)findViewById(R.id.frame_btn_software_hot);
    	mButtonChina = (Button)findViewById(R.id.frame_btn_software_china);
    	
    	mSoftwareCatalog = (ListView)findViewById(R.id.frame_software_listview_catalog);
    	mSoftwareTag = (ListView)findViewById(R.id.frame_software_listview_tag);
    	mSoftwareFooter = getLayoutInflater().inflate(R.layout.view_oschina_listview_footer, null);
    	mSoftwareFootMore = (TextView)mSoftwareFooter.findViewById(R.id.listview_foot_more);
    	mSoftwareFootProgress = (ProgressBar)mSoftwareFooter.findViewById(R.id.listview_foot_progress);
    	mSoftware = (PullToRefreshListView)findViewById(R.id.frame_software_listview);
    }
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mSoftwareCatalog.setOnItemClickListener(this);
		mSoftwareTag.setOnItemClickListener(this);
		mSoftware.setOnItemClickListener(this);
    	mSoftware.setOnScrollListener(this);
    	mSoftware.setOnRefreshListener(this);
		mButtonCatalog.setOnClickListener(softwareBtnClick(mButtonCatalog, HEAD_TAG_CATALOG, "开源软件库"));
    	mButtonRecommend.setOnClickListener(softwareBtnClick(mButtonRecommend, HEAD_TAG_RECOMMEND,"每周推荐软件"));
    	mButtonLastest.setOnClickListener(softwareBtnClick(mButtonLastest, HEAD_TAG_LASTEST,"最新软件列表"));
    	mButtonHot.setOnClickListener(softwareBtnClick(mButtonHot, HEAD_TAG_HOT,"热门软件列表"));
    	mButtonChina.setOnClickListener(softwareBtnClick(mButtonChina, HEAD_TAG_CHINA,"国产软件列表"));
	}
    
  	private void initData()
  	{
  		//禁用滑动
        mScrollLayout.setIsScroll(false);
    	mButtonCatalog.setEnabled(false);
    	
    	mSoftwareCatalogAdapter = new SoftwareCatelogAdapter(mContext, mSoftwareCatalogData, R.layout.item_oschina_software_catelog); 
    	mSoftwareCatalog.setAdapter(mSoftwareCatalogAdapter); 
    	
    	mSoftwareTagAdapter = new SoftwareCatelogAdapter(mContext, mSoftwareTagData, R.layout.item_oschina_software_catelog); 
    	mSoftwareTag.setAdapter(mSoftwareTagAdapter); 
    	
    	mSoftwareAdapter = new SoftwareAdapter(mContext, mSoftwareData, R.layout.item_oschina_software); 
    	mSoftware.addFooterView(mSoftwareFooter);
    	mSoftware.setAdapter(mSoftwareAdapter); 
    	
  		loadLvSoftwareCatalogData(0, mSoftwareCatalogHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
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
     * 线程加载软件分类列表数据
     * @param tag 第一级:0 第二级:tag
     * @param handler 处理器
     * @param action 动作标识
     */
	private void loadLvSoftwareCatalogData(final int tag,final Handler handler,final int action)
	{  
		headButtonSwitch(DATA_LOAD_ING);
		new Thread()
		{
			public void run() 
			{
				Message msg = new Message();
				try 
				{
					SoftwareCatalogList softwareCatalogList = UserHelper.getSoftwareCatalogList(mContext, tag);
					msg.what = softwareCatalogList.getSoftwareTypelist().size();
					msg.obj = softwareCatalogList;
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
     * 线程加载软件分类二级列表数据
     * @param tag 第一级:0 第二级:tag
     * @param handler 处理器
     * @param action 动作标识
     */
	private void loadLvSoftwareTagData(final int searchTag,final int pageIndex,final Handler handler,final int action)
	{  
		headButtonSwitch(DATA_LOAD_ING);
		new Thread()
		{
			public void run() 
			{
				Message msg = new Message();
				boolean isRefresh = false;
				if(action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL) isRefresh = true;
				try 
				{
					SoftwareList softwareList = UserHelper.getSoftwareTagList(mContext, searchTag, pageIndex, isRefresh);
					msg.what = softwareList.getSoftwarelist().size();
					msg.obj = softwareList;
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
     * 线程加载软件列表数据
     * @param searchTag 软件分类 推荐:recommend 最新:time 热门:view 国产:list_cn
     * @param pageIndex 当前页数
     * @param handler 处理器
     * @param action 动作标识
     */
	private void loadLvSoftwareData(final int tag,final int pageIndex,final Handler handler,final int action)
	{  
		String _searchTag = "";
		switch (tag) 
		{
		case HEAD_TAG_RECOMMEND: 
		{
			_searchTag = SoftwareList.TAG_RECOMMEND;
			break;
		}
		case HEAD_TAG_LASTEST: 
		{
			_searchTag = SoftwareList.TAG_LASTEST;
			break;
		}
		case HEAD_TAG_HOT: 
		{
			_searchTag = SoftwareList.TAG_HOT;
			break;
		}
		case HEAD_TAG_CHINA: 
		{
			_searchTag = SoftwareList.TAG_CHINA;
			break;
		}
		default:
			break;
		}
		
		if(StringUtils.isEmpty(_searchTag)) return;		
		final String searchTag = _searchTag;
		headButtonSwitch(DATA_LOAD_ING);
		
		new Thread()
		{
			public void run() 
			{
				Message msg = new Message();
				boolean isRefresh = false;
				if(action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL) isRefresh = true;
				try 
				{
					SoftwareList softwareList = UserHelper.getSoftwareList(mContext, searchTag, pageIndex, isRefresh);
					msg.what = softwareList.getPageSize();
					msg.obj = softwareList;
	            } 
				catch (Exception e) 
				{
	            	e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }
				msg.arg1 = action;
				if(mCurHeadTag == tag) handler.sendMessage(msg);
			}
		}.start();
	} 
	
	private View.OnClickListener softwareBtnClick(final Button btn, final int tag, final String title)
	{
    	return new View.OnClickListener() 
    	{
			public void onClick(View v) 
			{
		    	if(btn == mButtonCatalog)
		    	{
		    		mButtonCatalog.setEnabled(false);
		    	}
		    	else
		    	{
		    		mButtonCatalog.setEnabled(true);
		    	}
		    		
		    	if(btn == mButtonRecommend)
		    	{
		    		mButtonRecommend.setEnabled(false);
		    	}
		    	else
		    	{
		    		mButtonRecommend.setEnabled(true);
		    	}
		    		
		    	if(btn == mButtonLastest)
		    	{
		    		mButtonLastest.setEnabled(false);
		    	}
		    	else
		    	{
		    		mButtonLastest.setEnabled(true);	
		    	}
		    		
		    	if(btn == mButtonHot)
		    	{
		    		mButtonHot.setEnabled(false);
		    	}
		    	else
		    	{
		    		mButtonHot.setEnabled(true);
		    	}
		    		
		    	if(btn == mButtonChina)
		    	{
		    		mButtonChina.setEnabled(false);
		    	}
		    	else
		    	{
		    		mButtonChina.setEnabled(true);	    	
		    	}
		    	
				mCurHeadTag = tag;	
		    	if(btn == mButtonCatalog)
		    	{			    		
		    		mCurScreen = SCREEN_CATALOG;
		    		if(mSoftwareCatalogData.size() == 0) loadLvSoftwareCatalogData(0, mSoftwareCatalogHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);		
		    	}
		    	else
		    	{		    		
		    		mCurScreen = SCREEN_SOFTWARE;
		    		loadLvSoftwareData(tag, 0, mSoftwareHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);	
		    	}			
	    	
		    	mTitle.setText(title);
		    	mScrollLayout.setToScreen(mCurScreen);
			}
		};
    }
	
	@Override
	public void onClick(View v) 
	{
		back();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		switch (parent.getId())
		{
		case R.id.frame_software_listview_catalog:
		{
			TextView name = (TextView)view.findViewById(R.id.softwarecatalog_listitem_name);
    		SoftwareType type = (SoftwareType)name.getTag();
    		if(type == null) return;
    		
    		if(type.tag > 0)
    		{
    			mCurTitle = type.name;
    			mTitle.setText(mCurTitle);
    			//加载二级分类
    			mCurScreen = SCREEN_TAG;
    			mScrollLayout.scrollToScreen(mCurScreen);
    			loadLvSoftwareCatalogData(type.tag, mSoftwareTagHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
    		}
			break;
		}
		case R.id.frame_software_listview_tag:
		{
			TextView name = (TextView)view.findViewById(R.id.softwarecatalog_listitem_name);
    		SoftwareType type = (SoftwareType)name.getTag();
    		if(type == null) return;
    		
    		if(type.tag > 0)
    		{
    			mTitle.setText(type.name);
    			//加载软件列表
    			mCurScreen = SCREEN_SOFTWARE;
    			mScrollLayout.scrollToScreen(mCurScreen);
    			mCurSearchTag = type.tag;
    			loadLvSoftwareTagData(mCurSearchTag, 0, mSoftwareHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
    		}
			break;
		}	
		case R.id.frame_software_listview:
		{
			//点击头部、底部栏无效
    		if(position == 0 || view == mSoftwareFooter) return;
			TextView name = (TextView)view.findViewById(R.id.software_listitem_name);
			Software sw = (Software)name.getTag();

    		if(sw == null) return;
    		UIHelper.showUrlRedirect(view.getContext(), sw.url);
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) 
	{
		mSoftware.onScrollStateChanged(view, scrollState);
		//数据为空--不用继续下面代码了
		if(mSoftwareData.size() == 0) return;
		//判断是否滚动到底部
		boolean scrollEnd = false;
		try 
		{
			if(view.getPositionForView(mSoftwareFooter) == view.getLastVisiblePosition()) scrollEnd = true;
		} 
		catch (Exception e) 
		{
			scrollEnd = false;
		}
		
		if(scrollEnd && mCurSoftwareDataState==UIHelper.LISTVIEW_DATA_MORE)
		{
			mSoftware.setTag(UIHelper.LISTVIEW_DATA_LOADING);
			mSoftwareFootMore.setText(R.string.load_ing);
			mSoftwareFootProgress.setVisibility(View.VISIBLE);
			int pageIndex = mSumData/20;
			if(mCurHeadTag == HEAD_TAG_CATALOG)
			{
				loadLvSoftwareTagData(mCurSearchTag, pageIndex, mSoftwareHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
			}
			else
			{
				loadLvSoftwareData(mCurHeadTag, pageIndex, mSoftwareHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
	{
		mSoftware.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
	}
	
	@Override
	public void onRefresh() 
	{
		if(mCurHeadTag == HEAD_TAG_CATALOG)
		{
			loadLvSoftwareTagData(mCurSearchTag, 0, mSoftwareHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
		}
		else
		{
			loadLvSoftwareData(mCurHeadTag, 0, mSoftwareHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if(keyCode == KeyEvent.KEYCODE_BACK) 
		{
			back();
			return true;
		}
		return false;
	}

	/**
	 * 返回事件
	 */
	private void back() 
	{
		if(mCurHeadTag == HEAD_TAG_CATALOG) 
		{
			switch (mCurScreen) 
			{
			case SCREEN_SOFTWARE:
			{
				mTitle.setText(mCurTitle);
				mCurScreen = SCREEN_TAG;
				mScrollLayout.scrollToScreen(SCREEN_TAG);
				break;
			}
			case SCREEN_TAG:
			{
				mTitle.setText("开源软件库");
				mCurScreen = SCREEN_CATALOG;
				mScrollLayout.scrollToScreen(SCREEN_CATALOG);
				break;
			}
			case SCREEN_CATALOG:
			{
				finish();
				break;
			}
			default:
				break;
			}
		}
		else
		{
			finish();
		}
	}
	
	private Handler mSoftwareCatalogHandler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			headButtonSwitch(DATA_LOAD_COMPLETE);
			if(msg.what >= 0)
			{						
				SoftwareCatalogList list = (SoftwareCatalogList)msg.obj;
				Notice notice = list.getNotice();
				switch (msg.arg1) 
				{
				case UIHelper.LISTVIEW_ACTION_INIT:
				case UIHelper.LISTVIEW_ACTION_REFRESH:
				case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
				{
					mSoftwareCatalogData.clear();
					mSoftwareCatalogData.addAll(list.getSoftwareTypelist());
					break;
				}
				case UIHelper.LISTVIEW_ACTION_SCROLL:
					break;
				}	
				
				mSoftwareCatalogAdapter.notifyDataSetChanged();
				//发送通知广播
				if(notice != null)
				{
					UIHelper.sendBroadCast(mContext, notice);
				}
			}
		}
	};
	
	private Handler mSoftwareTagHandler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			headButtonSwitch(DATA_LOAD_COMPLETE);
			if(msg.what >= 0)
			{						
				SoftwareCatalogList list = (SoftwareCatalogList)msg.obj;
				Notice notice = list.getNotice();
				switch (msg.arg1) 
				{
				case UIHelper.LISTVIEW_ACTION_INIT:
				case UIHelper.LISTVIEW_ACTION_REFRESH:
				case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
				{
					mSoftwareTagData.clear();
					mSoftwareTagData.addAll(list.getSoftwareTypelist());
					break;
				}
				case UIHelper.LISTVIEW_ACTION_SCROLL:
					break;
				}	
				
				mSoftwareTagAdapter.notifyDataSetChanged();
				//发送通知广播
				if(notice != null)
				{
					UIHelper.sendBroadCast(mContext, notice);
				}
			}
		}
	};
	
	private Handler mSoftwareHandler = new Handler()
	{
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) 
		{
			headButtonSwitch(DATA_LOAD_COMPLETE);
			if(msg.what >= 0)
			{						
				SoftwareList list = (SoftwareList)msg.obj;
				Notice notice = list.getNotice();
				switch (msg.arg1) 
				{
				case UIHelper.LISTVIEW_ACTION_INIT:
				case UIHelper.LISTVIEW_ACTION_REFRESH:
				case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
				{
					mSumData = msg.what;
					mSoftwareData.clear();
					mSoftwareData.addAll(list.getSoftwarelist());
					break;
				}
				case UIHelper.LISTVIEW_ACTION_SCROLL:
				{
					mSumData += msg.what;
					if(mSoftwareData.size() > 0)
					{
						for(Software sw1 : list.getSoftwarelist())
						{
							boolean b = false;
							for(Software sw2 : mSoftwareData)
							{
								if(sw1.name.equals(sw2.name))
								{
									b = true;
									break;
								}
							}
							if(!b) mSoftwareData.add(sw1);
						}
					}
					else
					{
						mSoftwareData.addAll(list.getSoftwarelist());
					}
					break;
				}
				default:
					break;
				}	
				
				if(msg.what < 20)
				{
					mCurSoftwareDataState = UIHelper.LISTVIEW_DATA_FULL;
					mSoftwareAdapter.notifyDataSetChanged();
					mSoftwareFootMore.setText(R.string.load_full);
				}
				else if(msg.what == 20)
				{					
					mCurSoftwareDataState = UIHelper.LISTVIEW_DATA_MORE;
					mSoftwareAdapter.notifyDataSetChanged();
					mSoftwareFootMore.setText(R.string.load_more);
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
				mCurSoftwareDataState = UIHelper.LISTVIEW_DATA_MORE;
				mSoftwareFootMore.setText(R.string.load_error);
				
			}
			if(mSoftwareData.size()==0)
			{
				mCurSoftwareDataState = UIHelper.LISTVIEW_DATA_EMPTY;
				mSoftwareFootMore.setText(R.string.load_empty);
			}
			mSoftwareFootProgress.setVisibility(View.GONE);
			if(msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH)
			{
				mSoftware.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
				mSoftware.setSelection(0);
			}
			else if(msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG)
			{
				mSoftware.onRefreshComplete();
				mSoftware.setSelection(0);
			}
		}
	};
}
