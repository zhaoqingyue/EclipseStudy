package com.zhaoqy.app.demo.page.vmall.adapter;

import java.util.List;

import com.zhaoqy.app.demo.R;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchInitAdapter extends BaseAdapter {

	private Context context;
	private List<String> list;
	private TextView grid_searchinit_tvword;
	
	public SearchInitAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=View.inflate(context, R.layout.item_vmall_search_init, null);
			grid_searchinit_tvword=(TextView) convertView.findViewById(R.id.grid_searchinit_tvword);
			convertView.setTag(grid_searchinit_tvword);
		}else{
			grid_searchinit_tvword=(TextView) convertView.getTag();
		}
		grid_searchinit_tvword.setText(list.get(position));
		return convertView;
	}

}
