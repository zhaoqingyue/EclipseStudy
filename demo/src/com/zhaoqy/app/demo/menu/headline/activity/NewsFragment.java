package com.zhaoqy.app.demo.menu.headline.activity;

import java.util.ArrayList;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.headline.adapt.NewsAdapter;
import com.zhaoqy.app.demo.menu.headline.item.NewsEntity;
import com.zhaoqy.app.demo.menu.headline.util.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NewsFragment extends Fragment 
{
	public final static int SET_NEWSLIST = 0;
	private ArrayList<NewsEntity> mNewsList = new ArrayList<NewsEntity>();
	private Activity    mActivity;
	private ListView    mListView;
	private NewsAdapter mAdapter;
	private ImageView   mLoading;
	private TextView    mTitle;
	private String      mText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Bundle args = getArguments();
		mText = args != null ? args.getString("text") : "";
		initData();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) 
	{
		mActivity = activity;
		super.onAttach(activity);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) 
	{
		if (isVisibleToUser) 
		{
			if(mNewsList != null && mNewsList.size() !=0)
			{
				handler.obtainMessage(SET_NEWSLIST).sendToTarget();
			}
			else
			{
				new Thread(new Runnable() 
				{
					@Override
					public void run() 
					{
						try 
						{
							Thread.sleep(2);
						} 
						catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
						handler.obtainMessage(SET_NEWSLIST).sendToTarget();
					}
				}).start();
			}
		}
		else
		{
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_menu_headline_news, null);
		mListView = (ListView) view.findViewById(R.id.id_menu_headline_fragment_news_listview);
		mTitle = (TextView)view.findViewById(R.id.id_menu_headline_fragment_news_title);
		mLoading = (ImageView)view.findViewById(R.id.id_menu_headline_fragment_news_loading);
		mTitle.setText(mText);
		return view;
	}

	private void initData() 
	{
		mNewsList = Constants.getNewsList();
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case SET_NEWSLIST:
			{
				mLoading.setVisibility(View.GONE);
				mAdapter = new NewsAdapter(mActivity, mNewsList);
				mListView.setAdapter(mAdapter);
				break;
			}
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
}
