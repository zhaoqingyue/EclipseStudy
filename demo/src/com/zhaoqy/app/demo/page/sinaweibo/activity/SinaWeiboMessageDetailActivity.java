package com.zhaoqy.app.demo.page.sinaweibo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.sinaweibo.adapter.ComentAdapter;
import com.zhaoqy.app.demo.page.sinaweibo.item.WeiBoData;
import com.zhaoqy.app.demo.page.sinaweibo.view.PullToRefreshListView;

public class SinaWeiboMessageDetailActivity extends Activity implements OnClickListener
{
	public static final int AT_ME = 0;
	public static final int COMMENTS_ME = 1;
	private PullToRefreshListView mListView;
	private Context       mContext;
	private ComentAdapter mAdapter;
	private ImageView     mBack;
	private TextView      mName;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_sinaweibo_message_detail);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_sinaweibo_message_detail_back);
		mName = (TextView) findViewById(R.id.id_sinaweibo_message_detail_name);
		mListView = (PullToRefreshListView) findViewById(R.id.id_sinaweibo_message_detail_listview);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		int what = getIntent().getIntExtra("what", -1);
		if (what == AT_ME) 
		{
			mName.setText("@我的信息");
		} 
		else 
		{
			mName.setText("我收到的评论");
		}
		mAdapter = new ComentAdapter(WeiBoData.getCommentList(), mContext);
		mListView.setAdapter(mAdapter);
		mListView.onRefresh();
	}

	@Override
	public void onClick(View v) 
	{
		finish();
	}
}
