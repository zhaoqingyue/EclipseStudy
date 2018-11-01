package com.zhaoqy.app.demo.camera.gallery.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.gallery.item.PhotoItem;
import com.zhaoqy.app.demo.camera.gallery.util.ImageLoaderHelper;

public class GalleryItemAdapter extends BaseAdapter
{
	private Context         mContext;
	private List<PhotoItem> mList;
	
	public GalleryItemAdapter(List<PhotoItem> list,Context context)
	{
		mList = list;
		mContext = context;
	}
	
	@Override
	public int getCount() 
	{
		return mList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		Holder holder;
		if (convertView == null) 
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.view_custom_gallery_list_item, parent, false);
			holder = new Holder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.image_item);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (Holder) convertView.getTag();
		}
		ImageLoaderHelper.mImageLoader.displayImage("file://" + mList.get(position).getImagePath(), holder.imageView, ImageLoaderHelper.mGalleryItemOption);
		return convertView;
	}
	
	class Holder
	{
		ImageView imageView;
	}
}
