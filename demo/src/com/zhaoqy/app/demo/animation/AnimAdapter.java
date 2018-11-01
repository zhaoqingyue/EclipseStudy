package com.zhaoqy.app.demo.animation;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class AnimAdapter extends BaseAdapter 
{
	private Context      mContext;
	private List<String> mDatas;

	public AnimAdapter(Context context, List<String> datas) 
	{
		super();
		mDatas = datas;
		mContext = context;
	}

	@Override
	public int getCount() 
	{
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		Holder tag = null;
		if (convertView == null) 
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.view_animation_item, null);
			tag = new Holder();
			tag.mName = (TextView) convertView.findViewById(R.id.id_animation_item_name);
			convertView.setTag(tag);
		}
		Holder holder = (Holder) convertView.getTag();
		holder.mName.setText(mDatas.get(position));
		return convertView;
	}

	public static class Holder 
	{
		public TextView  mName;
	}
}