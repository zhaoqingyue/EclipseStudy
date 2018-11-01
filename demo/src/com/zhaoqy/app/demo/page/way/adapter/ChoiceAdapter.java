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
import com.zhaoqy.app.demo.page.way.item.Choice;
import com.zhaoqy.app.demo.page.way.item.UserInfo;
import com.zhaoqy.app.demo.page.way.util.ImageCache;
import com.zhaoqy.app.demo.page.way.view.CircleImageView;

public class ChoiceAdapter extends BaseAdapter 
{
	private Context context;
	private ArrayList<Choice> datas;
	private LruCache<String, Bitmap> lruCache;
	private final static String IMAGEHOME = "http://img.117go.com/timg/p308/";
	private final static String AVATAR = "http://img.117go.com/demo27/img/ava66/";

	public ChoiceAdapter(Context context) 
	{
		this.context = context;
		lruCache = ImageCache.GetLruCache(context);
	}

	public void BindData(ArrayList<Choice> datas) 
	{
		this.datas = datas;
	}

	@Override
	public int getCount() 
	{
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) 
	{
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) 
	{
		ViewHolder viewHolder = null;
		if (view == null) 
		{
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_way_choice, null);
			viewHolder.disCitys = (TextView) view.findViewById(R.id.jingxuan_txt_address);
			viewHolder.favoriteCount = (TextView) view.findViewById(R.id.jingxuan_favoriteCount);
			viewHolder.image = (ImageView) view.findViewById(R.id.jingxuan_imageView);
			viewHolder.pictureCount = (TextView) view.findViewById(R.id.jingxuan_txt_picCount);
			viewHolder.title = (TextView) view.findViewById(R.id.jingxuan_txt_title);
			viewHolder.viewCount = (TextView) view.findViewById(R.id.jingxuan_txt_eye);
			viewHolder.userCircleImageView = (CircleImageView) view.findViewById(R.id.jingxuan_user_circleImageView);
			view.setTag(viewHolder);
		} 
		else 
		{
			viewHolder = (ViewHolder) view.getTag();
		}
		Choice choiceData = datas.get(arg0);
		viewHolder.favoriteCount.setText(choiceData.getFavoriteCount());
		viewHolder.pictureCount.setText(choiceData.getPictureCount());
		viewHolder.title.setText(choiceData.getTitle());
		viewHolder.viewCount.setText(choiceData.getViewCount());
		StringBuffer stringBuffer = new StringBuffer();
		if (choiceData.getDispCities().length > 0) {
			for (int i = 0; i < choiceData.getDispCities().length; i++) {
				stringBuffer.append(choiceData.getDispCities()[i]).append("->");
				if (i ==choiceData.getDispCities().length) {
					stringBuffer.append(choiceData.getDispCities()[i]);
						
				}

			}
			viewHolder.disCitys.setText(choiceData.getDispCities()[0]); 
		}

		viewHolder.image.setTag(IMAGEHOME + choiceData.getImage());
		viewHolder.image.setImageResource(R.drawable.page_slide_way_defaultcovers);
		new ImageCache(context, lruCache, viewHolder.image, IMAGEHOME + choiceData.getImage(), "OnTheWay", 800, 400);
		UserInfo userInfo = choiceData.getUserInfo();
		viewHolder.userCircleImageView.setTag(AVATAR + userInfo.getAvatar());
		new ImageCache(context, lruCache, viewHolder.userCircleImageView,
				AVATAR + userInfo.getAvatar(), "OnTheWay", 800, 400);
		return view;
	}

	private class ViewHolder {

		private CircleImageView userCircleImageView;
		public TextView pictureCount;
		public TextView favoriteCount;
		public TextView disCitys;
		public TextView viewCount;
		public ImageView image;
		public TextView title;

	}

}
