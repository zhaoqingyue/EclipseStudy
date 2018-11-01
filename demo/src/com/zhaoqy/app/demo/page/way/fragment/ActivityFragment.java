package com.zhaoqy.app.demo.page.way.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.zhaoqy.app.demo.page.way.adapter.ActivityAdapter;
import com.zhaoqy.app.demo.page.way.commom.ClientApi;
import com.zhaoqy.app.demo.page.way.commom.LoadingAinm;
import com.zhaoqy.app.demo.page.way.item.Activity;
import com.zhaoqy.app.demo.weixin.ui.activity.WebViewActivity;

public class ActivityFragment extends Fragment 
{
	private ListView        mListView;
	private ActivityAdapter mAdapter;
	private RelativeLayout  mLoader;
	private LinearLayout    mLinearLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view =inflater.inflate(R.layout.fragment_way_activity, container, false);
		initViews(view);
		initData();
		new DownData().execute();
		LoadingAinm.ininLodingView(view);
		return view;
	}
	
	private void initViews(View view) 
	{
		mLoader = (RelativeLayout) view.findViewById(R.id.id_view_way_loader);
		mLinearLayout = (LinearLayout) view.findViewById(R.id.id_way_activity_linearlayout);
		mListView = (ListView) view.findViewById(R.id.id_way_activity_listview);
	}
	
	private void initData() 
	{
		mAdapter = new ActivityAdapter(getActivity());
	}

	class DownData extends AsyncTask<Void, Void, ArrayList<Activity>>
	{
		@Override
		protected ArrayList<Activity> doInBackground(Void... arg0) 
		{
			return ClientApi.getActivityList();
		}
		
		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(final ArrayList<Activity> result) 
		{
			super.onPostExecute(result);
			if (result!=null) 
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
						Intent intent = new Intent(getActivity(), WebViewActivity.class);
						intent.putExtra("Title", result.get(position).getName());
						intent.putExtra("URL", result.get(position).getUrlS());
						startActivity(intent);
						getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					}
				});
			}
			else 
			{
				mLoader.setVisibility(View.GONE);
				Toast.makeText(getActivity(), "网络异常,请检查", 1).show();
			}
		}
	}
}
