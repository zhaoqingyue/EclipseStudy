package com.zhaoqy.app.demo.page.sinaweibo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.sinaweibo.adapter.WeiboAdapter;
import com.zhaoqy.app.demo.page.sinaweibo.item.WeiBoData;
import com.zhaoqy.app.demo.page.sinaweibo.view.PullToRefreshListView;
import com.zhaoqy.app.demo.page.sinaweibo.view.PullToRefreshListView.OnRefreshListener;

public class SinaWeiboHomeActivity extends Activity implements OnClickListener, OnItemClickListener, OnRefreshListener
{
	private PullToRefreshListView mListView;
	private WeiboAdapter mAdapter;
	private Context      mContext;
	private ImageView    mWrite;
	private ImageView    mRefresh;
	private TextView     mUserName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_sinaweibo_home);
		mContext = this;
		
		initviews();
		setListener();
		initData();
	}

	private void initviews() 
	{
		mListView = (PullToRefreshListView) findViewById(R.id.id_sinaweibo_home_listview);
		mUserName = (TextView) findViewById(R.id.id_sinaweibo_home_username);
		mRefresh = (ImageView) findViewById(R.id.id_sinaweibo_home_refresh);
		mWrite = (ImageView) findViewById(R.id.id_sinaweibo_home_write);
	}
	
	private void setListener() 
	{
		mWrite.setOnClickListener(this);
		mListView.setOnRefreshListener(this);
		mRefresh.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
	}
	
	private void initData() 
	{
		mUserName.setText(WeiBoData.getUser().getScreen_name());
		mAdapter = new WeiboAdapter(this, WeiBoData.getStatusesList(), mListView);
		mListView.setAdapter(mAdapter);
	}
	
	private void refreshLoad() 
	{
	}

	@Override
	public void onRefresh() 
	{
		refreshLoad();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		Intent intent = new Intent(mContext, SinaWeiboDetailActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_sinaweibo_home_write:
		{
			Intent intent = new Intent(mContext, SinaWeiboSendActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_sinaweibo_home_refresh:
		{
			refreshLoad();
			mListView.onRefresh();
			break;
		}
		default:
			break;
		}
	}
}
