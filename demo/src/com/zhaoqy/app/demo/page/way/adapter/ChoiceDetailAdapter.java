package com.zhaoqy.app.demo.page.way.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.way.item.ChoiceDetail;
import com.zhaoqy.app.demo.page.way.util.ImageCache;

public class ChoiceDetailAdapter extends BaseAdapter 
{
	private Context context;
	private LruCache<String, Bitmap> lruCache;
	private ArrayList<ChoiceDetail> list;

	public ChoiceDetailAdapter(Context context) 
	{
		this.context = context;
		lruCache = ImageCache.GetLruCache(context);
	}

	public void BindData(ArrayList<ChoiceDetail> list) 
	{
		this.list = list;
	}

	@Override
	public int getCount() 
	{
		return list.size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) 
	{
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) 
	{
		ViewHolder viewHolder = null;
		if (convertView == null) 
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_way_choice_detail, null);
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.jingxuan_detail_list_image);
			viewHolder.text = (TextView) convertView
					.findViewById(R.id.jingxuna_detail_content);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ChoiceDetail jingxuanDetailData = list.get(arg0);
		// viewHolder.poi.setText(jingxuanDetailData.getPoi());
		viewHolder.text.setText(jingxuanDetailData.getText());
		viewHolder.image.setTag(jingxuanDetailData.getImage());
		viewHolder.image.setImageResource(R.drawable.page_slide_way_defaultcovers);
		new ImageCache(context, lruCache, viewHolder.image,
				jingxuanDetailData.getImage(), "OnTheWay", 800, 1080);
		return convertView;
	}

	private class ViewHolder {
		public ImageView image;
		public TextView text;
		public TextView poi;

	}

}
