package com.zhaoqy.app.demo.menu.headline.adapt;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.menu.headline.item.NewsEntity;
import com.zhaoqy.app.demo.menu.headline.util.Constants;
import com.zhaoqy.app.demo.menu.headline.util.Options;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter 
{
	private ImageLoader           mImageLoader = ImageLoader.getInstance();
	private ArrayList<NewsEntity> mNewsList;
	private Activity              mActivity;
	private LayoutInflater        mInflater;
	private DisplayImageOptions   mOptions;
	private PopupWindow           mPopupWindow;
	private ImageView             mPopClose;
	
	public NewsAdapter(Activity activity, ArrayList<NewsEntity> newsList) 
	{
		mActivity = activity;
		mNewsList = newsList;
		mInflater = LayoutInflater.from(mActivity);
		mOptions = Options.getListOptions();
		initPopWindow();
	}

	@Override
	public int getCount() 
	{
		return mNewsList == null ? 0 : mNewsList.size();
	}

	@Override
	public NewsEntity getItem(int position) 
	{
		if (mNewsList != null && mNewsList.size() != 0) 
		{
			return mNewsList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) 
		{
			view = mInflater.inflate(R.layout.item_menu_headline_slidemenu, null);
			mHolder = new ViewHolder();
			mHolder.item_layout = (RelativeLayout)view.findViewById(R.id.id_slidemenu_item_layout);
			mHolder.comment_layout = (RelativeLayout)view.findViewById(R.id.id_slidemenu_item_comment_layout);
			mHolder.item_title = (TextView)view.findViewById(R.id.id_slidemenu_item_article_top_title_name);
			mHolder.item_source = (TextView)view.findViewById(R.id.id_slidemenu_item_article_info_source);
			mHolder.list_item_local = (TextView)view.findViewById(R.id.id_slidemenu_item_article_info_local);
			mHolder.comment_count = (TextView)view.findViewById(R.id.id_slidemenu_item_article_info_comment_count);
			mHolder.publish_time = (TextView)view.findViewById(R.id.id_slidemenu_item_article_info_publish_time);
			mHolder.item_abstract = (TextView)view.findViewById(R.id.id_slidemenu_item_abstract);
			mHolder.alt_mark = (ImageView)view.findViewById(R.id.id_slidemenu_item_mark);
			mHolder.right_image = (ImageView)view.findViewById(R.id.id_slidemenu_item_article_top_title_right_image);
			mHolder.item_image_layout = (LinearLayout)view.findViewById(R.id.id_slidemenu_item_image_layout);
			mHolder.item_image_0 = (ImageView)view.findViewById(R.id.id_slidemenu_item_image0);
			mHolder.item_image_1 = (ImageView)view.findViewById(R.id.id_slidemenu_item_image1);
			mHolder.item_image_2 = (ImageView)view.findViewById(R.id.id_slidemenu_item_image2);
			mHolder.large_image = (ImageView)view.findViewById(R.id.id_slidemenu_item_large_image);
			mHolder.popicon = (ImageView)view.findViewById(R.id.id_slidemenu_item_article_pop_icon);
			mHolder.comment_content = (TextView)view.findViewById(R.id.id_slidemenu_item_comment_content);
			mHolder.right_padding_view = (View)view.findViewById(R.id.id_slidemenu_item_article_info_padding_view);
			view.setTag(mHolder);
		}
		else 
		{
			mHolder = (ViewHolder) view.getTag();
		}
		NewsEntity news = getItem(position);
		mHolder.item_title.setText(news.getTitle());
		mHolder.item_source.setText(news.getSource());
		mHolder.comment_count.setText("评论" + news.getCommentNum());
		mHolder.publish_time.setText(news.getPublishTime() + "小时前");
		List<String> imgUrlList = news.getPicList();
		mHolder.popicon.setVisibility(View.VISIBLE);
		mHolder.comment_count.setVisibility(View.VISIBLE);
		mHolder.right_padding_view.setVisibility(View.VISIBLE);
		if(imgUrlList !=null && imgUrlList.size() !=0)
		{
			if(imgUrlList.size() == 1)
			{
				mHolder.item_image_layout.setVisibility(View.GONE);
				if(news.getIsLarge())
				{
					mHolder.large_image.setVisibility(View.VISIBLE);
					mHolder.right_image.setVisibility(View.GONE);
					mImageLoader.displayImage(imgUrlList.get(0), mHolder.large_image, mOptions);
					mHolder.popicon.setVisibility(View.GONE);
					mHolder.comment_count.setVisibility(View.GONE);
					mHolder.right_padding_view.setVisibility(View.GONE);
				}
				else
				{
					mHolder.large_image.setVisibility(View.GONE);
					mHolder.right_image.setVisibility(View.VISIBLE);
					mImageLoader.displayImage(imgUrlList.get(0), mHolder.right_image, mOptions);
				}
			}
			else
			{
				mHolder.large_image.setVisibility(View.GONE);
				mHolder.right_image.setVisibility(View.GONE);
				mHolder.item_image_layout.setVisibility(View.VISIBLE);
				mImageLoader.displayImage(imgUrlList.get(0), mHolder.item_image_0, mOptions);
				mImageLoader.displayImage(imgUrlList.get(1), mHolder.item_image_1, mOptions);
				mImageLoader.displayImage(imgUrlList.get(2), mHolder.item_image_2, mOptions);
			}
		}
		else
		{
			mHolder.right_image.setVisibility(View.GONE);
			mHolder.item_image_layout.setVisibility(View.GONE);
		}
		int markResID = getAltMarkResID(news.getMark(),news.getCollectStatus());
		if(markResID != -1)
		{
			mHolder.alt_mark.setVisibility(View.VISIBLE);
			mHolder.alt_mark.setImageResource(markResID);
		}
		else
		{
			mHolder.alt_mark.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(news.getNewsAbstract())) 
		{
			mHolder.item_abstract.setVisibility(View.VISIBLE);
			mHolder.item_abstract.setText(news.getNewsAbstract());
		} 
		else 
		{
			mHolder.item_abstract.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(news.getLocal()))
		{
			mHolder.list_item_local.setVisibility(View.VISIBLE);
			mHolder.list_item_local.setText(news.getLocal());
		}
		else
		{
			mHolder.list_item_local.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(news.getComment()))
		{
			mHolder.comment_layout.setVisibility(View.VISIBLE);
			mHolder.comment_content.setText(news.getComment());
		}
		else
		{
			mHolder.comment_layout.setVisibility(View.GONE);
		}
		if(!news.getReadStatus())
		{
			mHolder.item_layout.setSelected(true);
		}
		else
		{
			mHolder.item_layout.setSelected(false);
		}
		mHolder.popicon.setOnClickListener(new popAction(position));
		return view;
	}

	static class ViewHolder 
	{
		RelativeLayout item_layout;
		TextView item_title;
		TextView item_source;
		TextView list_item_local;
		TextView comment_count;
		TextView publish_time;
		TextView item_abstract;
		ImageView alt_mark;
		ImageView right_image;
		LinearLayout item_image_layout; 
		ImageView item_image_0;
		ImageView item_image_1;
		ImageView item_image_2;
		ImageView large_image;
		ImageView popicon;
		RelativeLayout comment_layout;
		TextView comment_content;
		View right_padding_view;
	}
	
	public int getAltMarkResID(int mark,boolean isfavor)
	{
		if(isfavor)
		{
			return R.drawable.menu_headline_mark_favor;
		}
		switch (mark) 
		{
		case Constants.mark_recom:
			return R.drawable.menu_headline_mark_recommend;
		case Constants.mark_hot:
			return R.drawable.menu_headline_mark_hot;
		case Constants.mark_frist:
			return R.drawable.menu_headline_mark_first;
		case Constants.mark_exclusive:
			return R.drawable.menu_headline_mark_exclusive;
		case Constants.mark_favor:
			return R.drawable.menu_headline_mark_favor;
		default:
			break;
		}
		return -1;
	}
	
	private void initPopWindow() 
	{
		View popView = mInflater.inflate(R.layout.item_menu_headline_slidemenu_pop, null);
		mPopupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
		mPopupWindow.setAnimationStyle(R.style.menu_headline_slidemenu_pop_animation);
		mPopClose = (ImageView) popView.findViewById(R.id.id_slidemenu_pop_close);
	}

	public void showPop(View parent, int x, int y,int postion) 
	{
		mPopupWindow.showAtLocation(parent, 0, x, y);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.update();
		if (mPopupWindow.isShowing()) 
		{
		}
		mPopClose.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View paramView) 
			{
				mPopupWindow.dismiss();
			}
		});
	}
	
	public class popAction implements OnClickListener
	{
		int mPosition;
		
		public popAction(int position)
		{
			mPosition = position;
		}
		@Override
		public void onClick(View v) 
		{
			int[] arrayOfInt = new int[2];
			v.getLocationOnScreen(arrayOfInt);
	        int x = arrayOfInt[0];
	        int y = arrayOfInt[1];
	        showPop(v, x , y, mPosition);
		}
	}
}
