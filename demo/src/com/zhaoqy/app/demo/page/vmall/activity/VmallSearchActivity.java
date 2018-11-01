/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: VmallSearchActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.vmall.activity
 * @Description: 华为商城搜索页
 * @author: zhaoqy
 * @date: 2015-12-17 下午9:08:27
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.vmall.activity;

import java.util.ArrayList;
import java.util.List;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.vmall.adapter.SearchAdapter;
import com.zhaoqy.app.demo.page.vmall.item.SGoods;
import com.zhaoqy.app.demo.page.vmall.task.SearchAsync;
import com.zhaoqy.app.demo.page.vmall.task.SearchInitAsync;
import com.zhaoqy.app.demo.page.vmall.util.UrlUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class VmallSearchActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ViewSearch vs;
	private SearchAdapter sa, sagv;
	private List<SGoods> list = new ArrayList<SGoods>();
	private boolean flag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vmall_saerch);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}

	private void initView() 
	{
		vs = new ViewSearch();
		vs.search_etword = (EditText) findViewById(R.id.search_etword);
		vs.search_ivdelete = (ImageView) findViewById(R.id.search_ivdelete);
		vs.search_change = (ImageView) findViewById(R.id.search_ivchangeview);
		vs.search_ivsearch = (ImageView) findViewById(R.id.search_ivsearch);
		vs.search_ll_tv1 = (TextView) findViewById(R.id.search_ll_tv1);
		vs.search_ll_tv2 = (TextView) findViewById(R.id.search_ll_tv2);
		vs.search_ll_tv3 = (TextView) findViewById(R.id.search_ll_tv3);
		vs.search_ll_tv4 = (TextView) findViewById(R.id.search_ll_tv4);
		vs.search_gvword = (GridView) findViewById(R.id.search_gvword);
		vs.search_gv = (GridView) findViewById(R.id.search_gv);
		vs.search_lv = (ListView) findViewById(R.id.search_lv);
		init();
		fillAdapter();
		new SearchInitAsync(mContext, vs, sa, sagv, list).execute(UrlUtil.getHotWords);
		vs.search_ivdelete.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				init();
				list.clear();
			}
		});
		vs.search_ivsearch.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				initClick();
				String str = vs.search_etword.getText().toString().replace(" ", "%20");
				if (str == null || "".equals(str)) 
				{
					UrlUtil.search_keyword = vs.search_etword.getHint().toString().replace(" ", "%20");
				} 
				else 
				{
					UrlUtil.search_keyword = str;
				}
				String path = UrlUtil.search_0 + UrlUtil.search_page + UrlUtil.search_1 + UrlUtil.search_item + UrlUtil.search_2 + UrlUtil.search_keyword;
				new SearchAsync(mContext, vs, sa, sagv, list, true).execute(path);
			}
		});
		vs.search_gv.setOnScrollListener(new OnScrollListener() 
		{
			public void onScrollStateChanged(AbsListView view, int scrollState) 
			{
				if (flag && scrollState == OnScrollListener.SCROLL_STATE_IDLE) 
				{
					UrlUtil.search_page++;
					String path = UrlUtil.search_0 + UrlUtil.search_page + UrlUtil.search_1 + UrlUtil.search_item + UrlUtil.search_2 + UrlUtil.search_keyword;
					new SearchAsync(mContext, vs, sa, sagv, list,false).execute(path);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
			{
				flag = (totalItemCount == firstVisibleItem + visibleItemCount);
			}
		});
		vs.search_lv.setOnScrollListener(new OnScrollListener() 
		{
			public void onScrollStateChanged(AbsListView view, int scrollState) 
			{
				if (flag && scrollState == OnScrollListener.SCROLL_STATE_IDLE) 
				{
					UrlUtil.search_page++;
					String path = UrlUtil.search_0 + UrlUtil.search_page + UrlUtil.search_1 + UrlUtil.search_item + UrlUtil.search_2 + UrlUtil.search_keyword;
					new SearchAsync(mContext, vs, sa, sagv, list, true).execute(path);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
			{
				flag = (totalItemCount == firstVisibleItem + visibleItemCount);
			}
		});

		vs.search_gv.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				ImageView iv = (ImageView) view.findViewById(R.id.grid_search_ivpicUrl);
				Intent intent = new Intent(mContext, VmallDetailActivity.class);
				intent.putExtra("url", iv.getContentDescription().toString());
				startActivity(intent);
			}
		});
		vs.search_lv.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				ImageView iv = (ImageView) view.findViewById(R.id.list_search_ivpicUrl);
				Intent intent = new Intent(mContext, VmallDetailActivity.class);
				intent.putExtra("url", iv.getContentDescription().toString());
				startActivity(intent);
			}
		});
	}

	private void initData() 
	{
	}

	private void setListener() 
	{
	}
	
	private void initTv() 
	{
		vs.search_ll_tv1.setTextColor(Color.parseColor("#666666"));
		vs.search_ll_tv2.setTextColor(Color.parseColor("#666666"));
		vs.search_ll_tv3.setTextColor(Color.parseColor("#666666"));
		vs.search_ll_tv4.setTextColor(Color.parseColor("#666666"));
	}

	private void init() 
	{
		vs.search_etword.setText(null);
		vs.search_change.setVisibility(View.GONE);
		vs.search_ll_tv1.setText("热门搜索");
		vs.search_ll_tv1.setTextColor(Color.parseColor("#666666"));
		vs.search_ll_tv2.setVisibility(View.GONE);
		vs.search_ll_tv3.setVisibility(View.GONE);
		vs.search_ll_tv4.setVisibility(View.GONE);
		vs.search_lv.setVisibility(View.GONE);
		vs.search_gv.setVisibility(View.GONE);
		vs.search_gvword.setVisibility(View.VISIBLE);
		vs.search_ll_tv1.setClickable(false);
		initClick();
	}

	private void initClick() 
	{
		UrlUtil.search_page = 1;
		UrlUtil.search_item = 1;
		list.clear();
	}

	private void fillAdapter() 
	{
		sa = new SearchAdapter(mContext, list, vs.search_lv);
		vs.search_lv.setAdapter(sa);

		sagv = new SearchAdapter(mContext, list, vs.search_gv);
		vs.search_gv.setAdapter(sagv);
	}

	public static class ViewSearch 
	{
		public EditText search_etword;
		public ImageView search_change, search_ivsearch, search_ivdelete;
		public TextView search_ll_tv1, search_ll_tv2, search_ll_tv3, search_ll_tv4;
		public ListView search_lv;
		public GridView search_gvword, search_gv;
	}

	@Override
	public void onClick(View v) 
	{
		initTv();
		switch (v.getId()) 
		{
		case R.id.search_ll_tv1:
			vs.search_ll_tv1.setTextColor(Color.parseColor("#aa0000"));
			UrlUtil.search_item = 1;
			break;
		case R.id.search_ll_tv2:
			vs.search_ll_tv2.setTextColor(Color.parseColor("#aa0000"));
			UrlUtil.search_item = 2;
			break;
		case R.id.search_ll_tv3:
			vs.search_ll_tv3.setTextColor(Color.parseColor("#aa0000"));
			UrlUtil.search_item = 3;
			break;
		case R.id.search_ll_tv4:
			vs.search_ll_tv4.setTextColor(Color.parseColor("#aa0000"));
			UrlUtil.search_item = 4;
			break;
		}
	}
}
