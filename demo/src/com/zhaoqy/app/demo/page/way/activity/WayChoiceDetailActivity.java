package com.zhaoqy.app.demo.page.way.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.way.adapter.ChoiceDetailAdapter;
import com.zhaoqy.app.demo.page.way.commom.ClientApi;
import com.zhaoqy.app.demo.page.way.commom.LoadingAinm;
import com.zhaoqy.app.demo.page.way.item.Choice;
import com.zhaoqy.app.demo.page.way.item.ChoiceDetail;
import com.zhaoqy.app.demo.page.way.util.ImageCache;

public class WayChoiceDetailActivity extends Activity implements OnClickListener
{
	private LruCache<String, Bitmap> mLruCache;
	private ChoiceDetailAdapter      mAdapter;
	private Context        mContext;
	private View           mHeader;
	private ImageView      mHeaderImage;
	private TextView       mHeaderText;
	private TextView       mTitle;
	private ListView       mListView;
	private RelativeLayout mLoader;
	private LinearLayout   mLinearLayout;
	private Button         mLike;
	private Button         mComment;
	private Button         mShare;
	private String         mStartId;
	private Choice         mChoice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_way_choice_detail);
		mContext = this;
		
		initViews();
		setListener();
		initData();
		LoadingAinm.ininLoding(WayChoiceDetailActivity.this);
		new DownData().execute();
	}

	private void initViews() 
	{
		mHeader = LayoutInflater.from(mContext).inflate(R.layout.view_way_choice_detail_header, null);
		mHeaderImage = (ImageView) mHeader.findViewById(R.id.id_way_choice_detail_image);
		mHeaderText = (TextView) mHeader.findViewById(R.id.id_way_choice_detail_text);
		mLinearLayout = (LinearLayout) findViewById(R.id.id_way_choice_detail_linearlayout);
		mTitle = (TextView) findViewById(R.id.id_way_choice_detail_title);
		mListView = (ListView) findViewById(R.id.id_way_choice_detail_listview);
		mLike = (Button) findViewById(R.id.id_way_choice_detail_like);
		mComment = (Button) findViewById(R.id.id_way_choice_detail_comment);
		mShare = (Button) findViewById(R.id.id_way_choice_detail_share);
		mLoader = (RelativeLayout) findViewById(R.id.id_view_way_loader);
	}
	
	private void setListener() 
	{
		mLike.setOnClickListener(this);
		mComment.setOnClickListener(this);
		mShare.setOnClickListener(this);
	}

	private void initData() 
	{
		mAdapter = new ChoiceDetailAdapter(mContext);
		Intent intent = getIntent();
		mChoice = (Choice) intent.getSerializableExtra("Choice");
		mStartId = intent.getStringExtra("id");
		mLruCache = ImageCache.GetLruCache(mContext);
		mHeaderImage.setTag("http://img.117go.com/timg/p308/" + mChoice.getImage());
		new ImageCache(mContext, mLruCache, mHeaderImage, "http://img.117go.com/timg/p308/" + mChoice.getImage(), "demo", 800, 400);
		mListView.addHeaderView(mHeader);
		mHeaderText.setText(mChoice.getForeword());
		mTitle.setText(mChoice.getTitle());
		mLike.setText(mChoice.getViewCount());
		mComment.setText(mChoice.getCmtCount());
	}

	class DownData extends AsyncTask<Void, Void, ArrayList<ChoiceDetail>> 
	{
		@Override
		protected ArrayList<ChoiceDetail> doInBackground(Void... arg0) 
		{
			return ClientApi.getChoiceDetailList(mStartId);
		}

		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(ArrayList<ChoiceDetail> result) 
		{
			super.onPostExecute(result);
			if (result == null) 
			{
				mLoader.setVisibility(View.GONE);
				Toast.makeText(mContext, "网络异常,请检查！", 1) .show();
			}
			else 
			{
				mAdapter.BindData(result);
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				mLoader.setVisibility(View.GONE);
				mLinearLayout.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_way_choice_detail_like:
		{
			break;
		}
		case R.id.id_way_choice_detail_comment:
		{
			break;
		}
		case R.id.id_way_choice_detail_share:
		{
			Intent intent = new Intent(mContext, WayShareActivity.class);
			intent.putExtra("shareContent", mChoice.getForeword());
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
