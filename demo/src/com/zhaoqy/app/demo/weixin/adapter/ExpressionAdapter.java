/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: ExpressionAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.weixin.adapter
 * @Description: 表情Adapt
 * @author: zhaoqy
 * @date: 2015-12-8 下午3:57:36
 * @version: V1.0
 */

package com.zhaoqy.app.demo.weixin.adapter;

import java.util.List;

import com.zhaoqy.app.demo.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ExpressionAdapter extends ArrayAdapter<String> 
{
	private Context mContext;

	public ExpressionAdapter(Context context, int textViewResourceId, List<String> objects) 
	{
		super(context, textViewResourceId, objects);
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null) 
		{
			convertView = View.inflate(mContext, R.layout.row_expression, null);
		}

		ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_expression);
		String filename = getItem(position);
		int resId = getContext().getResources().getIdentifier(filename, "drawable", getContext().getPackageName());
		imageView.setImageResource(resId);
		return convertView;
	}
}
