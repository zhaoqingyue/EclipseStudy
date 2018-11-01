package com.zhaoqy.app.demo.index.search.view;

import java.util.List;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.index.search.util.SortModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SortAdapter extends BaseAdapter implements SectionIndexer
{
	private List<SortModel> list = null;
	private Context mContext;
	
	public SortAdapter(Context mContext, List<SortModel> list) 
	{
		this.mContext = mContext;
		this.list = list;
	}
	
	public void updateListView(List<SortModel> list)
	{
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() 
	{
		return list.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) 
	{
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(position);
		if (view == null) 
		{
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_index_search, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			view.setTag(viewHolder);
		} 
		else 
		{
			viewHolder = (ViewHolder) view.getTag();
		}
		
		int section = getSectionForPosition(position);
		if(position == getPositionForSection(section))
		{
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		}
		else
		{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.tvTitle.setText(this.list.get(position).getName());
		return view;
	}
	
	final static class ViewHolder 
	{
		TextView tvLetter;
		TextView tvTitle;
	}

	@Override
	public Object[] getSections() 
	{
		return null;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public int getPositionForSection(int section) 
	{
		for (int i = 0; i < getCount(); i++) 
		{
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) 
			{
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) 
	{
		return list.get(position).getSortLetters().charAt(0);
	}
	
	@SuppressLint("DefaultLocale")
	private String getAlpha(String str) 
	{
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		if (sortStr.matches("[A-Z]")) 
		{
			return sortStr;
		} else {
			return "#";
		}
	}
}
