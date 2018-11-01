/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: FindAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.adapter
 * @Description: 查找应用图标界面的Gallery显示
 * @author: zhaoqy
 * @date: 2015-12-14 下午5:49:07
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.zhaoqy.app.demo.page.safety.item.FindAppInfo;

@SuppressWarnings("deprecation")
public class FindAdapter extends BaseAdapter 
{
	private Context                mContext;
	private ArrayList<FindAppInfo> mList;

	public FindAdapter(Context context, ArrayList<FindAppInfo> list) 
	{
		mContext = context;
		mList = list;
	}

	@Override
	public int getCount() 
	{
		return mList.size();
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
		FindAppInfo info = mList.get(position);
		ImageView imageView = null;
		imageView = new ImageView(mContext);
		imageView.setScaleType(ScaleType.FIT_XY);
		Gallery.LayoutParams galleryParams = new Gallery.LayoutParams(100, 100);
		imageView.setLayoutParams(galleryParams);
		imageView.setImageDrawable(info.getAppIcon());
		return imageView;
	}
}
