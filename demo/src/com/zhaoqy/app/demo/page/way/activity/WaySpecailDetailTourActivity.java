package com.zhaoqy.app.demo.page.way.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.way.adapter.ChoiceAdapter;
import com.zhaoqy.app.demo.page.way.commom.ClientApi;
import com.zhaoqy.app.demo.page.way.commom.LoadingAinm;
import com.zhaoqy.app.demo.page.way.item.Choice;

public class WaySpecailDetailTourActivity extends Activity
{
	private ListView       mListView;
	private ChoiceAdapter  mAdapter;
	private String         mSearchId;
	private TextView       mTitlet;
	private RelativeLayout mLoader;
	private LinearLayout   mLinearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_way_special_detail_tour);
		
		initViews();
		initData();
		LoadingAinm.ininLoding(WaySpecailDetailTourActivity.this);
		new DownLoad().execute();
	}
	
	private void initViews()
	{
		mListView = (ListView) findViewById(R.id.id_way_special_detail_tour_listview);
		mTitlet = (TextView) findViewById(R.id.id_way_special_detail_tour_title);
		mLoader = (RelativeLayout) findViewById(R.id.id_view_way_loader);
		mLinearLayout = (LinearLayout) findViewById(R.id.id_way_special_detail_tour_linearlayout);
	}
	
	private void initData()
	{
		mAdapter = new ChoiceAdapter(getApplicationContext());
		mSearchId = getIntent().getStringExtra("SearchId");
		String title = getIntent().getStringExtra("name");
		mTitlet.setText(title);
	}

	class DownLoad extends AsyncTask<Void, Void, ArrayList<Choice>> 
	{
		protected ArrayList<Choice> doInBackground(Void... arg0) 
		{
			return ClientApi.getSpecialDetailTour(mSearchId);
		}

		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(final ArrayList<Choice> result) 
		{
			super.onPostExecute(result);
			if (result != null) 
			{
				mAdapter.BindData(result);
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				mLoader.setVisibility(View.GONE);
				mLinearLayout.setVisibility(View.VISIBLE);
				mListView.setOnItemClickListener(new OnItemClickListener() 
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
					{
						Intent intent = new Intent(WaySpecailDetailTourActivity.this, WayChoiceDetailActivity.class);
						intent.putExtra("Choice", result.get(position));
						intent.putExtra("id", result.get(position).getTourId());
						startActivity(intent);
					}
				});
			} 
			else 
			{
				mLoader.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(), "网络异常，请检查", 0).show();
			}
		}
	}
}
