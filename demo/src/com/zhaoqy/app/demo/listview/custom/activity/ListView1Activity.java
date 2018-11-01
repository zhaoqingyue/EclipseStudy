package com.zhaoqy.app.demo.listview.custom.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.listview.custom.view.ListViewCompat;
import com.zhaoqy.app.demo.listview.custom.view.SlideView;
import com.zhaoqy.app.demo.listview.custom.view.SlideView.OnSlideListener;

public class ListView1Activity extends Activity implements OnClickListener, OnItemClickListener, OnSlideListener
{
	private List<MessageItem> mMessageItems = new ArrayList<MessageItem>();
	private ImageView         mBack;
	private TextView          mTitle;
	private ListViewCompat    mListView;
    private SlideView         mSlideView;
    private SlideAdapter      mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview1);
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mListView = (ListViewCompat) findViewById(R.id.list);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.listview_item1));
		
		for (int i = 0; i < 20; i++) 
        {
            MessageItem item = new MessageItem();
            if (i % 3 == 0) 
            {
                item.iconRes = R.drawable.listview_delete_default_qq_avatar;
                item.title = "腾讯新闻";
                item.msg = "青岛爆炸满月：大量鱼虾死亡";
                item.time = "晚上18:18";
            } 
            else 
            {
                item.iconRes = R.drawable.listview_delete_wechat_icon;
                item.title = "微信团队";
                item.msg = "欢迎你使用微信";
                item.time = "12月18日";
            }
            mMessageItems.add(item);
        }
        mAdapter = new SlideAdapter();
        mListView.setAdapter(mAdapter);
	}
	
	private class SlideAdapter extends BaseAdapter 
	{
        private LayoutInflater mInflater;
        
        SlideAdapter() 
        {
            super();
            mInflater = getLayoutInflater();
        }

        @Override
        public int getCount() 
        {
            return mMessageItems.size();
        }

        @Override
        public Object getItem(int position) 
        {
            return mMessageItems.get(position);
        }

        @Override
        public long getItemId(int position) 
        {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) 
        {
            ViewHolder holder;
            SlideView slideView = (SlideView) convertView;
            if (slideView == null) 
            {
                View itemView = mInflater.inflate(R.layout.item_listview_delete, null);
                slideView = new SlideView(ListView1Activity.this);
                slideView.setContentView(itemView);
                holder = new ViewHolder(slideView);
                slideView.setOnSlideListener(ListView1Activity.this);
                slideView.setTag(holder);
            } 
            else 
            {
                holder = (ViewHolder) slideView.getTag();
            }
            MessageItem item = mMessageItems.get(position);
            item.slideView = slideView;
            item.slideView.shrink();

            holder.icon.setImageResource(item.iconRes);
            holder.title.setText(item.title);
            holder.msg.setText(item.msg);
            holder.time.setText(item.time);
            holder.deleteHolder.setOnClickListener(new OnClickListener() 
            {
				@SuppressLint("ShowToast")
				@Override
				public void onClick(View v) 
				{
					mMessageItems.remove(position);
					mAdapter.notifyDataSetChanged();
			    	Toast.makeText(ListView1Activity.this, "删除第" + position+"个条目", 0).show();
				}
			});
            return slideView;
        }
    }

    public class MessageItem 
    {
        public int iconRes;
        public String title;
        public String msg;
        public String time;
        public SlideView slideView;
    }

    private static class ViewHolder 
    {
        public ImageView icon;
        public TextView title;
        public TextView msg;
        public TextView time;
        public ViewGroup deleteHolder;

        ViewHolder(View view) 
        {
            icon = (ImageView) view.findViewById(R.id.icon);
            title = (TextView) view.findViewById(R.id.title);
            msg = (TextView) view.findViewById(R.id.msg);
            time = (TextView) view.findViewById(R.id.time);
            deleteHolder = (ViewGroup)view.findViewById(R.id.holder);
        }
    }

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			finish();
			break;
		}
		case R.id.holder:
		{
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onSlide(View view, int status) 
	{
		if (mSlideView != null && mSlideView != view) 
		{
			mSlideView.shrink();
        }

        if (status == SLIDE_STATUS_ON) 
        {
        	mSlideView = (SlideView) view;
        }
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
	}
}
