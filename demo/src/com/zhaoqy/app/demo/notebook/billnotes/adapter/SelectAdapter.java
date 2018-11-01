package com.zhaoqy.app.demo.notebook.billnotes.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.billnotes.item.BillItem;

public class SelectAdapter extends BaseAdapter
{
	private Context        mContext;
	private List<BillItem> mList;
	
	public SelectAdapter(Context context, List<BillItem> list) 
	{
		super();
		mContext = context;
		mList = list;
	}

	@Override
	public int getCount() 
	{
		if(mList != null)
		{
			return mList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) 
	{
		 return mList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		HodView hod;
		if(convertView == null)
		{
			hod = new HodView();
		    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_billnotes_select, null);
		    hod.mItemName = (TextView) convertView.findViewById(R.id.id_billnotes_select_item);
		    convertView.setTag(hod);
		}
		else 
		{
			hod = (HodView) convertView.getTag();
		}
		hod.mItemName.setText(mList.get(position).getName());
		return convertView;
	}

	private class HodView
	{
		TextView mItemName;
	}
}
