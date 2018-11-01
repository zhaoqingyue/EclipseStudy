/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: CrankSmsReportAdapter.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.adapter
 * @Description: 举报垃圾短信adapter
 * @author: zhaoqy
 * @date: 2015-12-11 下午2:47:58
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.adapter;

import java.util.List;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.item.CrankSmsItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CrankSmsReportAdapter extends BaseAdapter 
{
	private Context            mContext;
	private List<CrankSmsItem> mList;

	public CrankSmsReportAdapter(Context context, List<CrankSmsItem> list) 
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
	public Object getItem(int arg0) 
	{
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		convertView = LayoutInflater.from(mContext).inflate(R.layout.item_safety_crank_sms_report, null);
		ImageView iv = (ImageView) convertView.findViewById(R.id.cranksmscheckbox);
		if(mList.get(position).getFlag())
		{
			iv.setBackgroundResource(R.drawable.safety_clear_memory_checkbox_unchecked);
		}
		else
		{
			iv.setBackgroundResource(R.drawable.safety_clear_memory_checkbox_checked);
		}
		TextView photonum = (TextView) convertView.findViewById(R.id.cranksmsphotonumm);
		TextView body = (TextView) convertView.findViewById(R.id.cranksmsbody);
		CrankSmsItem bean = mList.get(position);
		body.setText(bean.getBody());
		photonum.setText(bean.getPhoto());
		return convertView;
	}
}
