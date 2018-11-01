package com.zhaoqy.app.demo.page.sinaweibo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.sinaweibo.adapter.ComentAdapter;
import com.zhaoqy.app.demo.page.sinaweibo.item.Statuses;
import com.zhaoqy.app.demo.page.sinaweibo.item.WeiBoData;
import com.zhaoqy.app.demo.page.sinaweibo.util.WeiboAutolinkUtil;

public class SinaWeiboDetailActivity extends Activity implements OnClickListener
{
	private ComentAdapter mAdapter;
	private ListView  mListView;
	private ImageView mBack;
	private View      mHead;
	private Statuses  mStatuses;
	private TextView  mUserName;
	private TextView  mContent;
	private TextView  mWeiboContent;
	private TextView  mLikeCount;
	private TextView  mCommentCount;
	private TextView  mRewrewCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_slide_sinaweibo_detail);
        
        initview();
        setListener();
        initData();
    }

	private void initview() 
    {
    	mListView = (ListView) findViewById(R.id.id_sinaweibo_listview);
    	mBack = (ImageView) findViewById(R.id.id_sinaweibo_back);
    	mHead = getLayoutInflater().inflate(R.layout.view_page_slide_sinaweibo_detail_headview, null);
        mListView.addHeaderView(mHead);
        mUserName = (TextView) mHead.findViewById(R.id.id_sinaweibo_user_name);
        mContent = (TextView) mHead.findViewById(R.id.id_sinaweibo_content);
        mWeiboContent = (TextView) mHead.findViewById(R.id.id_sinaweibo_weibo_content);
        mLikeCount = (TextView) mHead.findViewById(R.id.id_sinaweibo_weibo_like_count);
        mCommentCount = (TextView) mHead.findViewById(R.id.id_sinaweibo_weibo_comment_count);
        mRewrewCount = (TextView) mHead.findViewById(R.id.id_sinaweibo_weibo_rewrew_count);
    }
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}
	
	private void initData() 
	{
		mAdapter = new ComentAdapter(WeiBoData.getCommentList(), this);
        mListView.setAdapter(mAdapter);
        mStatuses = WeiBoData.getCurStatuses();
    	mUserName.setText(mStatuses.getUser().getScreen_name());
    	mContent.setText(WeiboAutolinkUtil.Autolink(mStatuses.getText(), SinaWeiboDetailActivity.this));
        if (mStatuses.getRetweeted_status() != null) 
        {
            Statuses rewStatuses = mStatuses.getRetweeted_status();
            mWeiboContent.setText(rewStatuses.getText());
            mCommentCount.setText(rewStatuses.getComments_count() + "");
            mRewrewCount.setText(rewStatuses.getReposts_count() + "");
            mLikeCount.setText(rewStatuses.getAttitudes_count() + "");
        } 
	}

	@Override
	public void onClick(View v) 
	{
		finish();
	}
}
