package com.zhaoqy.app.demo.notebook.billnotes.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.billnotes.item.BillItem;

public class MyBillAdapter extends BaseAdapter 
{
	private Context        mContext;
	private List<BillItem> mList;
	
	public MyBillAdapter(Context context, List<BillItem> list) 
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_billnotes_mybill, null);
			hod.mIcon = (ImageView) convertView.findViewById(R.id.billnotes_bill_icon);
		    hod.mRightName = (TextView) convertView.findViewById(R.id.billnotes_bill_right_name);
		    hod.mLeftName = (TextView) convertView.findViewById(R.id.billnotes_bill_left_name);
		    convertView.setTag(hod);
		}
		else
		{
			hod = (HodView)convertView.getTag();
		}
	   
		hod.mIcon.setImageResource(mList.get(position).getImg());
		int state = Integer.parseInt(mList.get(position).getState());
		if(state == 0)
		{
			hod.mRightName.setText(mList.get(position).getType() + "  " + mList.get(position).getBill());
			hod.mRightName.setVisibility(View.VISIBLE);
			hod.mLeftName.setVisibility(View.GONE);
		}
		if(state == 1)
		{
			hod.mLeftName.setText(mList.get(position).getType() + "  " + mList.get(position).getBill());
		    hod.mLeftName.setVisibility(View.VISIBLE);
		    hod.mRightName.setVisibility(View.GONE);
		}
		return convertView;
	}

	private class HodView
	{
		ImageView mIcon;
		TextView  mLeftName;
		TextView  mRightName;
	}
}
