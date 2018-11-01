package com.zhaoqy.app.demo.page.viewpager.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.viewpager.item.BiaoPanData;
import com.zhaoqy.app.demo.page.viewpager.util.ListUtils;
import com.zhaoqy.app.demo.page.viewpager.view.BiaoPanView;

public class ViewPagerScrollListActivity extends Activity implements OnClickListener
{
	private Context    mContext;
	private ImageView  mBack;
	private TextView   mTitle;
	private MyPageAdapter viewpageAdapter;
	private ArrayList<View> viewList;
	private ListView lv1;
	private ListView lv2;
	private ListView lv3;
	private int dataNum = 10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_viewpager_scroll_listview);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		
		prepareData();
		ViewPager viewpager = (ViewPager)findViewById(R.id.viewpager);
		viewpageAdapter = new MyPageAdapter();
		viewpager.setAdapter(viewpageAdapter);
		
		lv1 = (ListView) findViewById(R.id.lv1);
		MyListAdapter1 adapter1 = new MyListAdapter1();
		lv1.setAdapter(adapter1);
		new ListUtils(this).setListViewHeightBasedOnChildren(lv1);
		lv1.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				System.out.println("click"+position);
			}
		});
		lv2 = (ListView) findViewById(R.id.lv2);
		MyListAdapter2 adapter2 = new MyListAdapter2();
		lv2.setAdapter(adapter2);
		new ListUtils(this).setListViewHeightBasedOnChildren(lv2);
		
		lv3 = (ListView) findViewById(R.id.lv3);
		MyListAdapter3 adapter3 = new MyListAdapter3();
		lv3.setAdapter(adapter3);
		int totalHeight = dip2px(this, 35) * 3;
		ViewGroup.LayoutParams params = lv3.getLayoutParams();
		params.height = totalHeight + (lv3.getDividerHeight() * (lv3.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		lv3.setLayoutParams(params);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.page_slide_item2));
	}
	
	private class MyListAdapter3 extends BaseAdapter 
	{
		@Override
		public int getCount() 
		{
			return dataNum;
		}

		@Override
		public Object getItem(int position) 
		{
			return position;
		}

		@Override
		public long getItemId(int position) 
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			TextView view = new TextView(mContext);
			view.setText("理想丰满回复你发的《生如夏花》"+position);
			view.setTextSize(14);
			AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, dip2px(getApplicationContext(), 35));
			view.setGravity(Gravity.CENTER_VERTICAL);
			view.setLayoutParams(layoutParams);
			view.setBackgroundResource(android.R.drawable.editbox_background);
			return view;
		}
	}
	private class MyListAdapter1 extends BaseAdapter 
	{
		@Override
		public int getCount() 
		{
			return dataNum;
		}

		@Override
		public Object getItem(int position) 
		{
			return position;
		}

		@Override
		public long getItemId(int position) 
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			View view = View.inflate(getApplicationContext(), R.layout.item_page_slide_viewpager_scroll_listview, null);
			return view;
		}
	}
	
	private class MyListAdapter2 extends BaseAdapter 
	{
		@Override
		public int getCount() 
		{
			return dataNum;
		}

		@Override
		public Object getItem(int position) 
		{
			return position;
		}

		@Override
		public long getItemId(int position) 
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			View view = View.inflate(getApplicationContext(), R.layout.item_page_slide_viewpager_scroll_listview, null);
			return view;
		}
	}

	private void prepareData() 
	{
		viewList = new ArrayList<View>();
		ImageView view1 = new ImageView(this);
		view1.setImageResource(R.drawable.ic_launcher);
		viewList.add(view1);
		LinearLayout view2 = (LinearLayout) View.inflate(getApplicationContext(), R.layout.view_page_slide_viewpager_scroll_listview_biaopan, null);
		BiaoPanView biaopan = (BiaoPanView) view2.findViewById(R.id.biaopan);
		BiaoPanData data = new BiaoPanData();
		data.setTianqi("晴");
		data.setChengdu("空气正常");
		data.setCity("北京");
		data.setQiwen(9);
		data.setZhiliang(101);
		data.setTishi("可以多参加户外活动,呼吸空气");
		biaopan.setBiaopanData(data);
		viewList.add(view2);
	}

	class MyPageAdapter extends PagerAdapter 
	{
		@Override
		public int getCount() 
		{
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) 
		{
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) 
		{
			container.removeView(viewList.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) 
		{
			container.addView(viewList.get(position));
			return viewList.get(position);
		}
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) 
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		default:
			break;
		}
	}
}
