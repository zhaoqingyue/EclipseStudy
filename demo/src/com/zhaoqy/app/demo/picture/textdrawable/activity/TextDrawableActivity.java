package com.zhaoqy.app.demo.picture.textdrawable.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.picture.textdrawable.item.DataItem;
import com.zhaoqy.app.demo.picture.textdrawable.util.DataSource;

public class TextDrawableActivity extends Activity implements OnClickListener, OnItemClickListener
{
	public static final String TYPE = "TYPE";
	private Context    mContext;
	private ImageView  mBack;
	private TextView   mTitle;
    private DataSource mDataSource;
    private ListView   mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_textdrawable);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mListView = (ListView) findViewById(R.id.listView);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	    mListView.setOnItemClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.picture_item10);
		mDataSource = new DataSource(mContext);
        mListView.setAdapter(new SampleAdapter());
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
		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		DataItem item = (DataItem) mListView.getItemAtPosition(position);
        //if navigation is supported, open the next activity
        if (item.getNavigationInfo() != DataSource.NO_NAVIGATION) 
        {
            Intent intent = new Intent(mContext, TextDrawableListActivity.class);
            intent.putExtra(TYPE, item.getNavigationInfo());
            startActivity(intent);
        }
	}
	
	private class SampleAdapter extends BaseAdapter 
	{
        @Override
        public int getCount() 
        {
            return mDataSource.getCount();
        }

        @Override
        public DataItem getItem(int position) 
        {
            return mDataSource.getItem(position);
        }

        @Override
        public long getItemId(int position) 
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {
            ViewHolder holder;
            if (convertView == null) 
            {
                convertView = View.inflate(mContext, R.layout.item_image_textdrawable, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } 
            else 
            {
                holder = (ViewHolder) convertView.getTag();
            }

            DataItem item = getItem(position);
            final Drawable drawable = item.getDrawable();
            holder.imageView.setImageDrawable(drawable);
            holder.textView.setText(item.getLabel());

            // if navigation is supported, show the ">" navigation icon
            if (item.getNavigationInfo() != DataSource.NO_NAVIGATION) 
            {
                holder.textView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.textdrawable_next), null);
            }
            else 
            {
                holder.textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

            //fix for animation not playing for some below 4.4 devices
            if (drawable instanceof AnimationDrawable) 
            {
                holder.imageView.post(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        ((AnimationDrawable) drawable).stop();
                        ((AnimationDrawable) drawable).start();
                    }
                });
            }
            return convertView;
        }
    }
	
	private static class ViewHolder 
	{
        private ImageView imageView;
        private TextView textView;

        private ViewHolder(View view) 
        {
            imageView = (ImageView) view.findViewById(R.id.imageView);
            textView = (TextView) view.findViewById(R.id.textView);
        }
    }
}
