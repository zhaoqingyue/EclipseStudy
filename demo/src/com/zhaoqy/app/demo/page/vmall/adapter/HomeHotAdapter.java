package com.zhaoqy.app.demo.page.vmall.adapter;

import java.util.List;

import com.zhaoqy.app.demo.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HomeHotAdapter extends BaseAdapter {

	private Context context;
	private List<String> list;
	private TextView grid_hot_tvword;

	public HomeHotAdapter(Context context, List<String> list) {
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
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_vmall_home_hot,null);
			grid_hot_tvword = (TextView) convertView.findViewById(R.id.grid_hot_tvword);
			convertView.setTag(grid_hot_tvword);
		} else {
			grid_hot_tvword = (TextView) convertView.getTag();
		}
		grid_hot_tvword.setText(list.get(position));
		return convertView;
	}

}
