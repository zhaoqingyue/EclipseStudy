package com.zhaoqy.app.demo.page.vmall.adapter;

import java.util.List;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.vmall.item.Category;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoryLeftAdapter extends BaseAdapter {

	private Context context;
	private List<Category> list;
	private TextView list_category_left_tvname;

	public CategoryLeftAdapter(Context context, List<Category> list) {
		this.context = context;
		this.list = list;
	}

	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=View.inflate(context, R.layout.item_vmall_category_left, null);
			list_category_left_tvname=(TextView) convertView.findViewById(R.id.list_category_left_tvname);
			convertView.setTag(list_category_left_tvname);
		}else{
			list_category_left_tvname=(TextView) convertView.getTag();
		}
		list_category_left_tvname.setText(list.get(position).getName());
		if(position==0){
			convertView.setBackgroundResource(R.drawable.vmall_category_arrow_left);
		}
		return convertView;
	}

}
