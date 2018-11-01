/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaUserFriendActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: 用户关注、粉丝
 * @author: zhaoqy
 * @date: 2015-11-19 下午3:02:09
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.adapter.FriendAdapter;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.FriendList;
import com.zhaoqy.app.demo.oschina.item.FriendList.Friend;
import com.zhaoqy.app.demo.oschina.item.Notice;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView.OnRefreshListener;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class OSChinaUserFriendActivity extends OSChinaBaseActivity implements OnClickListener, OnItemClickListener, OnScrollListener, OnRefreshListener
{
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private PullToRefreshListView mFriend;
	private List<Friend>  mDatas = new ArrayList<Friend>();
	private FriendAdapter mAdapter;
	private Context       mContext;
	private ImageView     mBack;
	private ProgressBar   mProgressbar;
	private Button        mFans;
	private Button        mFollower;
	private View          mFooter;
	private TextView      mFootMore;
	private ProgressBar   mFootProgress;
    private int           mSumData;
	private int           mCurCatalog;
	private int           mCurState;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oschina_user_friend);
        mContext = this;
        
        initView();
        setListener();
        initData();
	}

	private void initView()
    {	
    	mBack = (ImageView)findViewById(R.id.friend_head_back);
    	mProgressbar = (ProgressBar)findViewById(R.id.friend_head_progress);
    	mFans = (Button)findViewById(R.id.friend_type_fans);
    	mFollower = (Button)findViewById(R.id.friend_type_follower);
    	mFooter = getLayoutInflater().inflate(R.layout.view_oschina_footer, null);
    	mFootMore = (TextView)mFooter.findViewById(R.id.listview_foot_more);
    	mFootProgress = (ProgressBar)mFooter.findViewById(R.id.listview_foot_progress);
    	mFriend = (PullToRefreshListView)findViewById(R.id.friend_listview);
    }
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mFriend.setOnItemClickListener(this);
    	mFriend.setOnScrollListener(this);
    	mFriend.setOnRefreshListener(this);
    	mFans.setOnClickListener(friendBtnClick(mFans, FriendList.TYPE_FANS));
    	mFollower.setOnClickListener(friendBtnClick(mFollower, FriendList.TYPE_FOLLOWER));
	}
    
  	@SuppressLint("HandlerLeak")
	private void initData()
  	{	
  		//设置当前分类
    	mCurCatalog = getIntent().getIntExtra("friend_type", FriendList.TYPE_FOLLOWER);
    	if(mCurCatalog == FriendList.TYPE_FANS) 
    	{
        	mFans.setEnabled(false);
    	} 
    	else 
    	{
    		mFollower.setEnabled(false);
    	}
  		
    	//设置粉丝与关注的数量
    	int followers = getIntent().getIntExtra("friend_followers", 0);
    	int fans = getIntent().getIntExtra("friend_fans", 0);
    	mFollower.setText(getString(R.string.user_friend_follower, followers));
    	mFans.setText(getString(R.string.user_friend_fans, fans));
    	
    	mAdapter = new FriendAdapter(mContext, mDatas, R.layout.item_oschina_friend); 
    	mFriend.addFooterView(mFooter);//添加底部视图  必须在setAdapter前
    	mFriend.setAdapter(mAdapter); 
		loadLvFriendData(mCurCatalog,0,mFriendHandler,UIHelper.LISTVIEW_ACTION_INIT);
  	}
  	
  	@Override
	public void onClick(View v) 
  	{
		switch (v.getId())
		{
		case R.id.friend_head_back:
		{
			finish();
			break;
		}
		default:
			break;
		}
	}
  	
    /**
     * 线程加载好友列表数据
     * @param type 0:显示自己的粉丝 1:显示自己的关注者
     * @param pageIndex 当前页数
     * @param handler 处理器
     * @param action 动作标识
     */
	private void loadLvFriendData(final int type, final int pageIndex, final Handler handler, final int action)
	{  
		headButtonSwitch(DATA_LOAD_ING);
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
					FriendList FriendList = UserHelper.getFriendList(mContext, type, pageIndex, isRefresh);
					msg.what = FriendList.getFriendlist().size();
					msg.obj = FriendList;
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
	
	private View.OnClickListener friendBtnClick(final Button btn, final int catalog)
	{
    	return new View.OnClickListener() 
    	{
			public void onClick(View v) 
			{
		    	if(btn == mFans)
		    	{
		    		mFans.setEnabled(false);
		    	}
		    	else
		    	{
		    		mFans.setEnabled(true);
		    	}
		    		
		    	if(btn == mFollower)
		    	{
		    		mFollower.setEnabled(false);
		    	}
		    	else
		    	{
		    		mFollower.setEnabled(true);		    	
		    	}
		    		
		    	mFootMore.setText("更多");
		    	mFootProgress.setVisibility(View.GONE);
				mCurCatalog = catalog;
				loadLvFriendData(mCurCatalog, 0, mFriendHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);		    	
			}
		};
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		//点击头部、底部栏无效
		if(position == 0 || view == mFooter) return;
		TextView name = (TextView)view.findViewById(R.id.friend_listitem_name);
		Friend friend = (Friend)name.getTag();
		if(friend == null) return;
		UIHelper.showUserCenter(mContext, friend.getUserid(), friend.getName());
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) 
	{
		mFriend.onScrollStateChanged(view, scrollState);
		//数据为空--不用继续下面代码了
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
			mFriend.setTag(UIHelper.LISTVIEW_DATA_LOADING);
			mFootMore.setText("加载中···");
			mFootProgress.setVisibility(View.VISIBLE);
			//当前pageIndex
			int pageIndex = mSumData/20;
			loadLvFriendData(mCurCatalog, pageIndex, mFriendHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
	{
		mFriend.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
	}

	@Override
	public void onRefresh() 
	{
		loadLvFriendData(mCurCatalog, 0, mFriendHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
	}

	@SuppressLint("HandlerLeak")
	private Handler mFriendHandler = new Handler()
	{
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) 
		{
			headButtonSwitch(DATA_LOAD_COMPLETE);
			if(msg.what >= 0)
			{						
				FriendList list = (FriendList)msg.obj;
				Notice notice = list.getNotice();
				switch (msg.arg1) 
				{
				case UIHelper.LISTVIEW_ACTION_INIT:
				case UIHelper.LISTVIEW_ACTION_REFRESH:
				case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
				{
					mSumData = msg.what;
					mDatas.clear();//先清除原有数据
					mDatas.addAll(list.getFriendlist());
					break;
				}
				case UIHelper.LISTVIEW_ACTION_SCROLL:
				{
					mSumData += msg.what;
					if(mDatas.size() > 0)
					{
						for(Friend friend1 : list.getFriendlist())
						{
							boolean b = false;
							for(Friend friend2 : mDatas)
							{
								if(friend1.getUserid() == friend2.getUserid())
								{
									b = true;
									break;
								}
							}
							if(!b) mDatas.add(friend1);
						}
					}
					else
					{
						mDatas.addAll(list.getFriendlist());
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
				//发送通知广播
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
				mFriend.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
				mFriend.setSelection(0);
			}
			else if(msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG)
			{
				mFriend.onRefreshComplete();
				mFriend.setSelection(0);
			}
		}
	};
}
