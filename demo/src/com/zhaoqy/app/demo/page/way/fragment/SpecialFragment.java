package com.zhaoqy.app.demo.page.way.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.way.activity.WaySpecailDetailTourActivity;
import com.zhaoqy.app.demo.page.way.adapter.SpecialAdapter;
import com.zhaoqy.app.demo.page.way.commom.ClientApi;
import com.zhaoqy.app.demo.page.way.commom.LoadingAinm;
import com.zhaoqy.app.demo.page.way.item.Special;

public class SpecialFragment extends Fragment 
{
	private ListView       mImageListview;
	private ListView       mTourListview;
	private TextView       mImageMore;
	private TextView       mTourMore;
	private SpecialAdapter mTourAdapter;
	private SpecialAdapter mImageAdapter;
	private RelativeLayout mLoader;
	private ScrollView     mScrollView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_way_special, container, false);
		initViews(view);
		setListener();
		initData();
		new Downdata().execute();
		LoadingAinm.ininLodingView(view);
		return view;
	}

	private void setListener() 
	{
		mImageMore.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
			}
		});

		mTourMore.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
			}
		});
	}
	
	private void initData() 
	{
		mImageAdapter = new SpecialAdapter(getActivity());
		mTourAdapter = new SpecialAdapter(getActivity());
	}

	private void initViews(View view) 
	{
		mImageListview = (ListView) view.findViewById(R.id.id_way_special_image_listView);
		mTourListview = (ListView) view.findViewById(R.id.id_way_special_tour_listView);
		mImageMore = (TextView) view.findViewById(R.id.id_way_special_image_more);
		mTourMore = (TextView) view.findViewById(R.id.id_way_special_tour_more);
		mLoader = (RelativeLayout) view.findViewById(R.id.id_view_way_loader);
		mScrollView = (ScrollView) view.findViewById(R.id.id_way_special_srcollow);
	}

	class Downdata extends AsyncTask<Void, Void, ArrayList<ArrayList<Special>>> 
	{
		@Override
		protected ArrayList<ArrayList<Special>> doInBackground(Void... arg0) 
		{
			return ClientApi.getSpecialList();
		}

		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(final ArrayList<ArrayList<Special>> result) 
		{
			super.onPostExecute(result);
			if (result != null) 
			{
				mImageAdapter.BindData(result.get(1));
				mImageListview.setAdapter(mImageAdapter);
				mImageAdapter.notifyDataSetChanged();
				mImageListview.setOnItemClickListener(new OnItemClickListener() 
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
					{
					}
				});
				mTourAdapter.BindData(result.get(0));
				mTourListview.setAdapter(mTourAdapter);
				mTourListview.setOnItemClickListener(new OnItemClickListener() 
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
					{
						Intent intent = new Intent(getActivity(), WaySpecailDetailTourActivity.class);
						intent.putExtra("SearchId", result.get(0).get(position).getId());
						intent.putExtra("name", result.get(0).get(position).getName());
						startActivity(intent);
					}
				});
				mTourAdapter.notifyDataSetChanged();
				mLoader.setVisibility(View.GONE);
				mScrollView.setVisibility(View.VISIBLE);
			} 
			else 
			{
				mLoader.setVisibility(View.GONE);
				Toast.makeText(getActivity(), "网络异常", 0).show();
			}
		}
	}
}
