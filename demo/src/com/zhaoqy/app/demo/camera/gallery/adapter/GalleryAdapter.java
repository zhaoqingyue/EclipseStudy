package com.zhaoqy.app.demo.camera.gallery.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.gallery.item.PhotoBucket;
import com.zhaoqy.app.demo.camera.gallery.util.ImageLoaderHelper;

public class GalleryAdapter extends BaseAdapter 
{
	private Context           mContext;
	private List<PhotoBucket> mList;
	
	public GalleryAdapter(Context context)
	{
		mContext = context;
		mList = new ArrayList<PhotoBucket>();
	};
	
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
			holder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.view_custom_gallery_item, parent, false);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.count = (TextView) convertView.findViewById(R.id.count);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (Holder) convertView.getTag();
		}
		holder.count.setText("" + mList.get(position).getCount());
		holder.name.setText(mList.get(position).getBucketName());
		ImageLoaderHelper.mImageLoader.displayImage("file://" + mList.get(position).getImageList().get(0).getImagePath(), 
				                                    holder.image, ImageLoaderHelper.mGalleryOption);
		return convertView;
	}

	class Holder
	{
		ImageView image;
		TextView name;
		TextView count;
	}

	public void setArrayList(List<PhotoBucket> arrayList) 
	{
		mList = arrayList;
	}
}
