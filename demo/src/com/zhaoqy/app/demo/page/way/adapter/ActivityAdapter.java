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

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.way.item.Activity;
import com.zhaoqy.app.demo.page.way.util.ImageCache;

public class ActivityAdapter extends BaseAdapter {

	private ArrayList<Activity> data;
	private Context context;
	private LruCache<String,Bitmap> lruCache;
	
	public void BindData(ArrayList<Activity> data){
		
		this.data = data;
	}
	
	public ActivityAdapter(Context context){
		this.context=context;
		lruCache = ImageCache.GetLruCache(context);
		
	}

	@Override
	public int getCount() 
	{
		return data.size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) 
	{
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHoleder viewHoleder =null;
		if (view==null) {
			viewHoleder =new ViewHoleder();
			view = LayoutInflater.from(context).inflate(R.layout.item_way_special, null);
			viewHoleder.imageView=(ImageView) view.findViewById(R.id.zhuanti_main_image);
			view.setTag(viewHoleder);
		}else {
			viewHoleder = (ViewHoleder) view.getTag();
		}
		Activity huoDongData =data.get(position);
		viewHoleder.imageView.setImageResource(R.drawable.page_slide_way_defaultcovers);
		viewHoleder.imageView.setTag(huoDongData.getIamge());
		new ImageCache(context, lruCache, viewHoleder.imageView, huoDongData.getIamge(),"OnTheWay",800, 400);
		
		return view;
	}
	
	private class ViewHoleder{
		public ImageView imageView;
		
	}

}
