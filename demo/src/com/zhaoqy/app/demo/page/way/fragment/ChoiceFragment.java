package com.zhaoqy.app.demo.page.way.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.way.activity.WayChoiceDetailActivity;
import com.zhaoqy.app.demo.page.way.adapter.ChoiceAdapter;
import com.zhaoqy.app.demo.page.way.commom.ClientApi;
import com.zhaoqy.app.demo.page.way.commom.LoadingAinm;
import com.zhaoqy.app.demo.page.way.commom.URL;
import com.zhaoqy.app.demo.page.way.item.Choice;
import com.zhaoqy.app.demo.page.way.view.PullToRefresh;
import com.zhaoqy.app.demo.page.way.view.PullToRefresh.OnRefreshListener;

public class ChoiceFragment extends Fragment implements OnRefreshListener, OnItemClickListener
{
	private static final int  INIT = 0;
	private static final int  REFRESH = 1;
	private static final int  LOAD = 2;
	private ListView          mListView;
	private ChoiceAdapter     mAdapter;
	private PullToRefresh     mPullToLoad;
	private PullToRefresh     mPullToRefresh;
	private RelativeLayout    mLoader;
	private LinearLayout      mLinearLayout;
	private ArrayList<Choice> mChoices = new ArrayList<Choice>();
	private int               mOffset = 20;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_way_choice, container, false);
		initViews(view);
		setListener(view);
		initData();
		init();
		LoadingAinm.ininLodingView(view);
		return view;
	}

	private void initViews(View view) 
	{
		mListView = (ListView) view.findViewById(R.id.id_way_choice_listview);
		mLinearLayout = (LinearLayout) view.findViewById(R.id.id_way_choice_linearlayout);
		mLoader = (RelativeLayout) view.findViewById(R.id.id_view_way_loader);
	}
	
	private void setListener(View view) 
	{
		((PullToRefresh) view.findViewById(R.id.id_way_choice_refresh)).setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
	}
	
	private void initData() 
	{
		mAdapter = new ChoiceAdapter(getActivity());
	}

	private void init() 
	{
		new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				Message msg = Message.obtain();
				msg.obj = ClientApi.getChoiceList(URL.REFRESH_URL);
				msg.what = INIT;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	@Override
	public void onRefresh(PullToRefresh pullToRefreshLayout) 
	{
		mPullToRefresh = pullToRefreshLayout;
		new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				Message msg = Message.obtain();
				msg.obj = ClientApi.getChoiceList(URL.REFRESH_URL);
				msg.what = REFRESH;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	@Override
	public void onLoadMore(PullToRefresh pullToRefreshLayout) 
	{
		mPullToLoad = pullToRefreshLayout;
		new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				Message msg = Message.obtain();
				msg.obj = ClientApi.getChoiceList(URL.LOAD_URL);
				msg.what = LOAD;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		Intent intent = new Intent(getActivity(), WayChoiceDetailActivity.class);
		intent.putExtra("Choice", mChoices.get(position));
		intent.putExtra("id", mChoices.get(position).getTourId());
		startActivity(intent);
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() 
	{
		@SuppressWarnings("unchecked")
		@SuppressLint("ShowToast")
		public void handleMessage(Message msg) 
		{
			if (msg.obj == null) 
			{
				mLoader.setVisibility(View.GONE);
				Toast.makeText(getActivity(), "网络异常,请检查！", 1).show();
			} 
			else 
			{
				switch (msg.what) 
				{
				case INIT:
				{
					mLoader.setVisibility(View.GONE);
					mLinearLayout.setVisibility(View.VISIBLE);
					mChoices.clear();
					mChoices = (ArrayList<Choice>) msg.obj;
					mAdapter.BindData(mChoices);
					mListView.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					break;
				}
				case REFRESH:
				{
					mChoices.clear();
					mChoices = (ArrayList<Choice>) msg.obj;
					mAdapter.BindData(mChoices);
					mListView.setAdapter(mAdapter);
					mPullToRefresh.refreshFinish(PullToRefresh.SUCCEED);
					mAdapter.notifyDataSetChanged();
					break;
				}
				case LOAD:
				{
					mChoices.addAll((ArrayList<Choice>) msg.obj);
					mAdapter.BindData(mChoices);
					mListView.setAdapter(mAdapter);
					mListView.setSelection(mOffset);
					mPullToLoad.loadmoreFinish(PullToRefresh.SUCCEED);
					mAdapter.notifyDataSetChanged();
					break;
				}
				default:
					break;
				}
			}
		}
	};
}
