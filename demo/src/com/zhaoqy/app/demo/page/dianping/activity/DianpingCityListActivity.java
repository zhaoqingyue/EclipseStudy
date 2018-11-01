/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: DianpingCityListActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.dianping.activity
 * @Description: 大众点评城市列表页
 * @author: zhaoqy
 * @date: 2015-12-17 上午10:52:58
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.dianping.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.dianping.item.City;
import com.zhaoqy.app.demo.page.dianping.util.SaxXMLParser;
import com.zhaoqy.app.demo.page.dianping.view.SideBar;
import com.zhaoqy.app.demo.page.dianping.view.SideBar.OnTouchingLetterChangedListener;

public class DianpingCityListActivity extends Activity implements OnClickListener, OnItemClickListener, OnTouchingLetterChangedListener
{
	private Context      mContext;
	private TextView     mBack;
	private ListView     mListView;
	private SideBar      mSidebar;
	private TextView     mSelection;
	private RadioButton  mRadioButton;
	private List<City>   mCityList;
	private StringBuffer mBuffer = new StringBuffer();
	private List<String> mFirstList = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dianping_city);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}

	private void initView() 
	{
		mBack = (TextView) findViewById(R.id.activity_city_list_tv_back);
		mListView = (ListView) findViewById(R.id.city_list_lv);
		mSidebar = (SideBar) findViewById(R.id.sidebar);
		mSelection = (TextView) findViewById(R.id.dialog);
		mRadioButton = (RadioButton) findViewById(R.id.city_list_rb1);
	}
	
	private void initData() 
	{
		mRadioButton.setChecked(true);
		View view=LayoutInflater.from(mContext).inflate(R.layout.view_dianping_city_header, null);
		mListView.addHeaderView(view);
		initCityList();
		CityListAdapter adapter = new CityListAdapter(mContext, mCityList);
		mListView.setAdapter(adapter);
		mSidebar.setTextView(mSelection);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mSidebar.setOnTouchingLetterChangedListener(this);
	}
	
	private void initCityList() 
	{
		SaxXMLParser parser = new SaxXMLParser();
		try 
		{
			InputStream inputStream=getAssets().open("citys.xml");	
			mCityList = parser.getListBySaxXMLParser(inputStream);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.activity_city_list_tv_back:
		{
			finish();
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public void onTouchingLetterChanged(String sortKey) 
	{
		mListView.setSelection(findIndexBySortKey(mCityList, sortKey)+1); 
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		Intent intent = new Intent();
		if(position != 0)
		{  
			TextView textView = (TextView) view.findViewById(R.id.city_list_item_tv_cityName);
			intent.putExtra("cityName", textView.getText().toString());
			setResult(RESULT_OK, intent);
			finish();
		}
	}
	
	private int findIndexBySortKey(List<City> list, String sortKey)
	{
		if(list != null)
		{
			for(int i=0; i<list.size(); i++)
			{
				City c = list.get(i);
				if(sortKey.equals(c.getSortKey()))
				{
					return i;
				}
			}
		}
		return -2; 
	}
	
	class CityListAdapter extends BaseAdapter
	{
		private List<City>     mDataList;
		private LayoutInflater mInflater;

		public CityListAdapter(Context context, List<City> list) 
		{
			mDataList = list;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() 
		{
			return mDataList.size();
		}

		@Override
		public Object getItem(int arg0) 
		{
			return mDataList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) 
		{
			return arg0;
		}

		@Override
		public View getView(int arg0, View converView, ViewGroup arg2) 
		{
			ViewHolder vh = null;
			if(converView == null)
			{
				vh = new ViewHolder();
				converView = mInflater.inflate(R.layout.item_dianping_city, null);
				vh.sortKey=(TextView) converView.findViewById(R.id.city_list_item_tv_sortKey);
				vh.cityName=(TextView) converView.findViewById(R.id.city_list_item_tv_cityName);
				converView.setTag(vh);
			}
			else
			{
				vh=(ViewHolder) converView.getTag();
			}
		
			City c = mDataList.get(arg0);
			String sort = c.getSortKey();
			String name = c.getName();

			if(mBuffer.indexOf(sort) == -1)
			{   
				mBuffer.append(sort);
				mFirstList.add(name);
			}
			if(mFirstList.contains(name))
			{
				vh.sortKey.setText(sort);
				vh.sortKey.setVisibility(View.VISIBLE);
			}
			else 
			{
				vh.sortKey.setVisibility(View.GONE);
			}
			vh.cityName.setText(name);
			return converView;
		}

		class ViewHolder
		{
			TextView sortKey;
			TextView cityName;
		}
	}
}
