package com.zhaoqy.app.demo.listview.custom.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.listview.custom.view.OnRefreshListener;
import com.zhaoqy.app.demo.listview.custom.view.RefreshListView;

public class ListView0Activity extends Activity implements OnClickListener, OnRefreshListener
{
	private Context         mContext;
	private ImageView       mBack;
	private TextView        mTitle;
	private List<String>    mListData;
	private RefreshListView mRefreshView;
	private MyAdapter       mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview0);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mRefreshView = (RefreshListView) findViewById(R.id.refresh_listview);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mRefreshView.setOnRefreshListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.listview_item0));
		
		mListData = new ArrayList<String>();
		for (int i = 0; i < 20; i++) 
		{
			mListData.add("这是一条ListView的数据" + i);
		}
	    mAdapter = new MyAdapter();
	    mRefreshView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public void onRefresh() 
	{
		//异步查询数据
		new AsyncTask<Void, Void, Void>() 
		{
			@Override
			protected Void doInBackground(Void... params) 
			{
				SystemClock.sleep(2000);
				mListData.add(0, "这是下拉刷新出来BaseAnimation");
				return null;
			}

			protected void onPostExecute(Void result) 
			{
				mAdapter.notifyDataSetChanged();
				//隐藏头布局
				mRefreshView.onRefreshFinish();
			}
		}.execute(new Void[] {});
	}

	@Override
	public void onLoadMoring() 
	{
		new AsyncTask<Void, Void, Void>() 
		{
			@Override
			protected Void doInBackground(Void... params) 
			{
				SystemClock.sleep(5000);
				mListData.add("加载更多刷新出来BaseAnimation1");
				mListData.add("加载更多刷新出来BaseAnimation2");
				return null;
			}

			@Override
			protected void onPostExecute(Void result) 
			{
				super.onPostExecute(result);
				mAdapter.notifyDataSetChanged();
				mRefreshView.onRefreshFinish();
			}

		}.execute(new Void[] {});
	}
	
	class MyAdapter extends BaseAdapter 
	{
		@Override
		public int getCount() 
		{
			return mListData.size();
		}

		@Override
		public Object getItem(int arg0) 
		{
			return mListData.get(arg0);
		}

		@Override
		public long getItemId(int position) 
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			TextView textView = null;
			if(convertView != null) 
			{
				 textView = (TextView) convertView;
			}
			else 
			{
				textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_listview_paging, null);
			}
			textView.setText(mListData.get(position));
			textView.setTextSize(20);
			textView.setTextColor(getResources().getColor(R.color.black));
			return textView;
		}
    }
}
