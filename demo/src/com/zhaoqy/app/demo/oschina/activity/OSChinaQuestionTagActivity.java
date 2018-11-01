/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: OSChinaQuestionTagActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.activity
 * @Description: Tag相关帖子列表
 * @author: zhaoqy
 * @date: 2015-11-23 下午9:16:05
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.oschina.adapter.QuestionAdapter;
import com.zhaoqy.app.demo.oschina.helper.UIHelper;
import com.zhaoqy.app.demo.oschina.helper.UserHelper;
import com.zhaoqy.app.demo.oschina.item.Notice;
import com.zhaoqy.app.demo.oschina.item.Post;
import com.zhaoqy.app.demo.oschina.item.PostList;
import com.zhaoqy.app.demo.oschina.util.StringUtils;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView;
import com.zhaoqy.app.demo.oschina.view.PullToRefreshListView.OnRefreshListener;

@SuppressLint("HandlerLeak")
public class OSChinaQuestionTagActivity extends OSChinaBaseActivity implements OnClickListener, OnItemClickListener, OnScrollListener, OnRefreshListener
{
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private final static int DATA_LOAD_FAIL = 0x003;
	private PullToRefreshListView mQuestion;
	private QuestionAdapter       mQuestionAdapter;
	private List<Post>  mQuestionData = new ArrayList<Post>();
	private Context     mContext;
	private ImageView   mHome;
	private TextView    mHeadTitle;
	private ProgressBar mProgressbar;
	private View        mQuestionFooter;
	private TextView    mQuestionFootMore;
	private ProgressBar mQuestionFootProgress;
    private String      mCurTag;
    private int         mQuestionSumData;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oschina_question_tag);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView()
	{
		mHome = (ImageView)findViewById(R.id.question_tag_home);
		mHeadTitle = (TextView)findViewById(R.id.question_tag_head_title);
		mProgressbar = (ProgressBar)findViewById(R.id.question_tag_head_progress);
		
		mQuestionFooter = getLayoutInflater().inflate(R.layout.view_oschina_listview_footer, null);
		mQuestionFootMore = (TextView) mQuestionFooter.findViewById(R.id.listview_foot_more);
		mQuestionFootProgress = (ProgressBar)mQuestionFooter.findViewById(R.id.listview_foot_progress);
        mQuestion = (PullToRefreshListView)findViewById(R.id.question_tag_listview);
	}
	
	private void setListener() 
	{
		mHome.setOnClickListener(this);
		mQuestion.setOnItemClickListener(this);
	    mQuestion.setOnScrollListener(this);
	    mQuestion.setOnRefreshListener(this);	
	}
	
	private void initData()
	{
		mCurTag = getIntent().getStringExtra("post_tag");
		mHeadTitle.setText(mCurTag);
		
		mQuestionAdapter = new QuestionAdapter(mContext, mQuestionData, R.layout.item_oschina_question);        
        mQuestion.addFooterView(mQuestionFooter);
        mQuestion.setAdapter(mQuestionAdapter); 
		loadQuestionData(mCurTag, 0, mQuestionHandler, UIHelper.LISTVIEW_ACTION_INIT);
    }
	
    /**
     * 线程加载问答列表数据
     * @param tag 当前Tag
     * @param pageIndex 当前页数
     * @param handler 处理器
     * @param action 动作标识
     */
	private void loadQuestionData(final String tag, final int pageIndex, final Handler handler, final int action)
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
					PostList list = UserHelper.getPostListByTag(mContext, tag, pageIndex, isRefresh);				
					msg.what = list.getPageSize();
					msg.obj = list;
	            } 
				catch (Exception e) 
				{
	            	e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }
				msg.arg1 = action;//告知handler当前action
                handler.sendMessage(msg);
			}
		}.start();
	} 
	
    /**
     * 头部加载动画展示
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
		case DATA_LOAD_FAIL:
		{
			mProgressbar.setVisibility(View.GONE);
			break;
		}
		default:
			break;
		}
    }

	@Override
	public void onClick(View v) 
	{
		UIHelper.showHome(mContext);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		//点击头部、底部栏无效
		if(position == 0 || view == mQuestionFooter) return;
		Post post = null;		
		//判断是否是TextView
		if(view instanceof TextView)
		{
			post = (Post)view.getTag();
		}
		else
		{
			TextView tv = (TextView)view.findViewById(R.id.question_listitem_title);
			post = (Post)tv.getTag();
		}
		if(post == null) return;
		//跳转到问答详情
		UIHelper.showQuestionDetail(view.getContext(), post.getId());
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) 
	{
		mQuestion.onScrollStateChanged(view, scrollState);
		//数据为空--不用继续下面代码了
		if(mQuestionData.size() == 0) return;
		//判断是否滚动到底部
		boolean scrollEnd = false;
		try 
		{
			if(view.getPositionForView(mQuestionFooter) == view.getLastVisiblePosition())
				scrollEnd = true;
		} 
		catch (Exception e) 
		{
			scrollEnd = false;
		}
		
		int lvDataState = StringUtils.toInt(mQuestion.getTag());
		if(scrollEnd && lvDataState==UIHelper.LISTVIEW_DATA_MORE)
		{
			mQuestion.setTag(UIHelper.LISTVIEW_DATA_LOADING);
			mQuestionFootMore.setText(R.string.load_ing);
			mQuestionFootProgress.setVisibility(View.VISIBLE);
			//当前pageIndex
			int pageIndex = mQuestionSumData/UserHelper.PAGE_SIZE;
			loadQuestionData(mCurTag, pageIndex, mQuestionHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
	{
		mQuestion.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
	}

	@Override
	public void onRefresh() 
	{
		loadQuestionData(mCurTag, 0, mQuestionHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
	}
	
	private Handler mQuestionHandler = new Handler()
	{
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) 
		{				
			if(msg.what >= 0)
			{	
				headButtonSwitch(DATA_LOAD_COMPLETE);
				PostList list = (PostList)msg.obj;
				Notice notice = list.getNotice();
				//处理listview数据
				switch (msg.arg1) 
				{
				case UIHelper.LISTVIEW_ACTION_INIT:
				case UIHelper.LISTVIEW_ACTION_REFRESH:
				{
					mQuestionSumData = msg.what;
					mQuestionData.clear();//先清除原有数据
					mQuestionData.addAll(list.getPostlist());
					break;
				}
				case UIHelper.LISTVIEW_ACTION_SCROLL:
				{
					mQuestionSumData += msg.what;
					if(mQuestionData.size() > 0)
					{
						for(Post p1 : list.getPostlist())
						{
							boolean b = false;
							for(Post p2 : mQuestionData)
							{
								if(p1.getId() == p2.getId() && p1.getAuthorId() == p2.getAuthorId()){
									b = true;
									break;
								}
							}
							if(!b) mQuestionData.add(p1);
						}
					}
					else
					{
						mQuestionData.addAll(list.getPostlist());
					}
					break;
				}
				default:
					break;
				}						
				
				if(msg.what < 20)
				{
					mQuestion.setTag(UIHelper.LISTVIEW_DATA_FULL);
					mQuestionAdapter.notifyDataSetChanged();
					mQuestionFootMore.setText(R.string.load_full);
				}
				else if(msg.what == 20)
				{					
					mQuestion.setTag(UIHelper.LISTVIEW_DATA_MORE);
					mQuestionAdapter.notifyDataSetChanged();
					mQuestionFootMore.setText(R.string.load_more);
				}
				//发送通知广播
				if(notice != null)
				{
					UIHelper.sendBroadCast(mContext, notice);
				}
			}
			else if(msg.what == -1)
			{
				headButtonSwitch(DATA_LOAD_FAIL);
				//有异常--显示加载出错 & 弹出错误消息
				mQuestion.setTag(UIHelper.LISTVIEW_DATA_MORE);
				mQuestionFootMore.setText(R.string.load_error);
			}
			if(mQuestionData.size()==0)
			{
				mQuestion.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
				mQuestionFootMore.setText(R.string.load_empty);
			}
			mQuestionFootProgress.setVisibility(View.GONE);
			if(msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH)
			{
				mQuestion.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
				mQuestion.setSelection(0);
			}
		}
	};
}
