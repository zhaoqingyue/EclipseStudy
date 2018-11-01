package com.zhaoqy.app.demo.page.sinaweibo.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.sinaweibo.activity.SinaWeiboImageDetailActivity;
import com.zhaoqy.app.demo.page.sinaweibo.activity.SinaWeiboUserInfoActivity;
import com.zhaoqy.app.demo.page.sinaweibo.item.Statuses;
import com.zhaoqy.app.demo.page.sinaweibo.util.TimeUtil;
import com.zhaoqy.app.demo.page.sinaweibo.util.WeiboAutolinkUtil;

public class WeiboAdapter extends BaseAdapter 
{
	private Context             mContext;
	private ArrayList<Statuses> mStatuses;
	private LayoutInflater      mInflater;

	public WeiboAdapter(Context context, ArrayList<Statuses> statuses, ListView listView) 
	{
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mStatuses = statuses;
	}

	@Override
	public int getCount() 
	{
		return mStatuses.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return mStatuses.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder = null;
		if (convertView == null) 
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_page_slide_sinaweibo, null);
			holder.ivicon = (ImageView) convertView.findViewById(R.id.id_sinaweibo_icon);
			holder.ivimg = (ImageView) convertView.findViewById(R.id.id_sinaweibo_status_img);
			holder.tvname = (TextView) convertView.findViewById(R.id.id_sinaweibo_username);
			holder.tvtext = (TextView) convertView.findViewById(R.id.id_sinaweibo_text);
			holder.tvcommentcout = (TextView) convertView.findViewById(R.id.id_sinaweibo_comment_count);
			holder.tvredcount = (TextView) convertView.findViewById(R.id.id_sinaweibo_red_count);
			holder.tvlikecount = (TextView) convertView.findViewById(R.id.id_sinaweibo_like_count);
			holder.tvretcontent = (TextView) convertView.findViewById(R.id.id_sinaweibo_red_stu_content);
			holder.tvtime = (TextView) convertView.findViewById(R.id.id_sinaweibo_time);
			holder.tvsource = (TextView) convertView.findViewById(R.id.id_sinaweibo_source);
			holder.ivrewimg = (ImageView) convertView.findViewById(R.id.id_sinaweibo_red_stu_img);
			holder.rewlayout = (RelativeLayout) convertView.findViewById(R.id.id_sinaweibo_layout);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		final Statuses statuses = mStatuses.get(position);
		holder.ivimg.setVisibility(View.VISIBLE);
		holder.tvname.setText(statuses.getUser().getScreen_name());
		holder.tvtext.setMovementMethod(LinkMovementMethod.getInstance());
		holder.tvtext.setText(WeiboAutolinkUtil.Autolink(statuses.getText(), mContext));
		holder.tvcommentcout.setText(statuses.getComments_count() + "");
		holder.tvredcount.setText(statuses.getReposts_count() + "");
		holder.tvlikecount.setText(" + " + statuses.getAttitudes_count() + "");
		holder.tvtime.setText(TimeUtil.getShortTime(statuses.getCreated_at()));
		holder.tvsource.setText(Html.fromHtml(statuses.getSource()));
		if (statuses.getRetweeted_status() != null) 
		{
			holder.rewlayout.setVisibility(View.VISIBLE);
			holder.ivimg.setVisibility(View.GONE);
			Statuses rewsStatuses = statuses.getRetweeted_status();
			holder.tvretcontent.setMovementMethod(LinkMovementMethod.getInstance());
			holder.tvretcontent.setText(WeiboAutolinkUtil.Autolink(rewsStatuses.getText(), mContext));
		} 
		else 
		{
			holder.rewlayout.setVisibility(View.GONE);
		}

		holder.ivrewimg.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent in = new Intent(mContext, SinaWeiboImageDetailActivity.class);
				in.putExtra("orurl", statuses.getRetweeted_status().getOriginal_pic());
				mContext.startActivity(in);
			}
		});
		holder.ivimg.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent in = new Intent(mContext, SinaWeiboImageDetailActivity.class);
				in.putExtra("orurl", statuses.getOriginal_pic());
				mContext.startActivity(in);
			}
		});

		holder.ivicon.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent in = new Intent(mContext, SinaWeiboUserInfoActivity.class);
				in.putExtra("username", statuses.getUser().getScreen_name());
				mContext.startActivity(in);
			}
		});
		return convertView;
	}

	class ViewHolder 
	{
		TextView tvname, tvtext, tvcommentcout, tvredcount, tvretcontent, tvtime, tvsource, tvlikecount;
		ImageView ivicon, ivimg, ivrewimg;
		RelativeLayout rewlayout;
	}
}
