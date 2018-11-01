package com.zhaoqy.app.demo.page.viewpager.util;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListUtils 
{
	private Context mContext;
	
	public ListUtils(Context context)
	{
		mContext = context;
	}
	
	public void setListViewHeightBasedOnChildren(ListView listView) 
	{
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) 
		{
			return;
		}
		int totalHeight = dip2px(mContext, 65) * 2;
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
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

}
