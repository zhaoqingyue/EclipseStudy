package com.zhaoqy.app.demo.page.sinaweibo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.sinaweibo.adapter.WeiboAdapter;
import com.zhaoqy.app.demo.page.sinaweibo.item.User;
import com.zhaoqy.app.demo.page.sinaweibo.item.WeiBoData;

public class SinaWeiboUserInfoActivity extends Activity implements OnClickListener
{
	private WeiboAdapter mAdapter;
	private ImageView mBack;
	private ListView  mListView;
	private User      mUser;
	private View      mHead;
	private TextView  mName;
	private TextView  mDsec;
	private TextView  mGuanzhu;
	private TextView  mFensi;
	private TextView  mZan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_sinaweibo_userinfo);
		
		initView();
		setListener();
		initData();
		showuserinfo();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_sinaweibo_userinfo_back);
		mListView = (ListView) findViewById(R.id.id_sinaweibo_userinfo_listview);
		mHead = LayoutInflater.from(this).inflate(R.layout.view_page_slide_sinaweibo_userinfo_header, null);
		mName = (TextView) mHead.findViewById(R.id.id_sinaweibo_userinfo_header_name);
		mDsec = (TextView) mHead.findViewById(R.id.id_sinaweibo_userinfo_header_dsec);
		mZan = (TextView) mHead.findViewById(R.id.id_sinaweibo_userinfo_header_zan);
		mGuanzhu = (TextView) mHead.findViewById(R.id.id_sinaweibo_userinfo_header_guanzhu);
		mFensi = (TextView) mHead.findViewById(R.id.id_sinaweibo_userinfo_header_fensi);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}
	
	private void initData() 
	{
		String name = getIntent().getStringExtra("username");
		mUser = WeiBoData.getUser();
		mName.setText(name);
		mListView.addHeaderView(mHead);
		mAdapter = new WeiboAdapter(this, WeiBoData.getStatusesList(), mListView);
		mListView.setAdapter(mAdapter);
	}
	
	public void showuserinfo()
	{
		mName.setText(mUser.getScreen_name());
		mDsec.setText(mUser.getDescription());
		mGuanzhu.setText("关注\n" + mUser.getFollowers_count());
		mFensi.setText("粉丝\n" + mUser.getFollowers_count());
		mZan.setText("赞\n" + mUser.getBi_followers_count());
	}
	
	@Override
	public void onClick(View v) 
	{
		finish();
	}
}
