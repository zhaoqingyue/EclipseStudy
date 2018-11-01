package com.zhaoqy.app.demo.notebook.billnotes.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.billnotes.item.NoteItem;

public class NoteAdapter extends BaseAdapter 
{
	private Context        mContext;
	private List<NoteItem> mList;
	private String         mTime;
	
	public NoteAdapter(Context context, List<NoteItem> list) 
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
		if (convertView == null) 
		{
			hod = new HodView();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_billnotes_note, null);
			hod.mTitle = (TextView) convertView.findViewById(R.id.id_billnotes_note_title);
			hod.mContent = (TextView) convertView.findViewById(R.id.id_billnotes_note_content);
			hod.mTime = (TextView) convertView.findViewById(R.id.id_billnotes_note_time);
			convertView.setTag(hod);
		} 
		else 
		{
			hod = (HodView) convertView.getTag();
		}
		if (mList.get(position).getTime() == null) 
		{
			mTime = "";
		} 
		else 
		{
			mTime = mList.get(position).getTime();
		}
		hod.mContent.setText(mTime + " " + mList.get(position).getContent());
		hod.mTitle.setText(mList.get(position).getTitle());
		hod.mTime.setText(mTime);
		return convertView;
	}

	private class HodView
	{
		TextView mTitle;
		TextView mContent;
		TextView mTime;
	}
}
