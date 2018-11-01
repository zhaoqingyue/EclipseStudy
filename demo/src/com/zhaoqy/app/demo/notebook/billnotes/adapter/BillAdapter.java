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

public class BillAdapter extends BaseAdapter 
{
	private Context        mContext;
	private List<BillItem> mList;
    private BillItem       mItem;
    private Integer[]      mIcon;
    private String []      mType;
	
	public BillAdapter(Context context, List<BillItem> list) 
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_billnotes_bill, null);
			hod.mIcon = (ImageView) convertView.findViewById(R.id.billnotes_bill_img);
		    hod.mName = (TextView) convertView.findViewById(R.id.billnotes_bill_name);
		    convertView.setTag(hod);
		}
		else 
		{
			hod=(HodView) convertView.getTag();
		}
		mItem = mList.get(position);
		mIcon = mItem.getImgs();
		mType = mItem.getTypes();
		String path1 = mType[position];
		int path = mIcon[position];
		hod.mIcon.setImageResource(path);
		hod.mName.setText(path1);
		return convertView;
	}

	private class HodView
	{
		ImageView mIcon;
		TextView  mName;
	}
}
