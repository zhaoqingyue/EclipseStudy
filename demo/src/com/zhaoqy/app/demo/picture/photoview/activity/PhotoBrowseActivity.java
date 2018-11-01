package com.zhaoqy.app.demo.picture.photoview.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.picture.photoview.util.InfoUtil;
import com.zhaoqy.app.demo.picture.photoview.view.PhotoView;

public class PhotoBrowseActivity extends Activity implements OnClickListener, OnItemClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private GridView  mGridView;
	private View      mParent;
	private View      mBg;
	private PhotoView mPhotoView;
	private InfoUtil  mInfo;
	private AlphaAnimation in = new AlphaAnimation(0, 1);
	private AlphaAnimation out = new AlphaAnimation(1, 0);
	private int[] imgs = new int[]{R.drawable.image_viewpager1, R.drawable.image_viewpager2, R.drawable.image_viewpager3, R.drawable.image_viewpager4 };
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_photo_browse);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mParent = findViewById(R.id.parent);
        mBg = findViewById(R.id.bg);
        mPhotoView = (PhotoView) findViewById(R.id.img);
        mGridView = (GridView) findViewById(R.id.gv);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mGridView.setOnItemClickListener(this);
		mPhotoView.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("ImageView点击浏览大图");
		
		in.setDuration(300);
        out.setDuration(300);
        mGridView.setAdapter(new PhotoAdapter());
	    mPhotoView.enable();
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
		case R.id.img:
		{
			mBg.startAnimation(out);
            mPhotoView.animaTo(mInfo, new Runnable() 
            {
                @Override
                public void run() 
                {
                    mParent.setVisibility(View.GONE);
                }
            });
			break;
		}
		default:
			break;
		}
	}
	
	@Override
    public void onBackPressed() 
	{
        if (mParent.getVisibility() == View.VISIBLE) 
        {
            mBg.startAnimation(out);
            mPhotoView.animaTo(mInfo, new Runnable() 
            {
                @Override
                public void run() 
                {
                    mParent.setVisibility(View.GONE);
                }
            });
        }
        else 
        {
            super.onBackPressed();
        }
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		PhotoView p = (PhotoView) view;
        mInfo = p.getInfo();
        mPhotoView.setImageResource(imgs[position]);
        mBg.startAnimation(in);
        mParent.setVisibility(View.VISIBLE);
        mPhotoView.animaFrom(mInfo);
	}
	
	private class PhotoAdapter extends BaseAdapter
	{
		@Override
		public int getCount() 
		{
			return imgs.length;
		}

		@Override
		public Object getItem(int position) 
		{
			return null;
		}

		@Override
		public long getItemId(int position) 
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			PhotoView p = new PhotoView(mContext);
            p.setLayoutParams(new AbsListView.LayoutParams((int) (getResources().getDisplayMetrics().density * 100), 
            		(int) (getResources().getDisplayMetrics().density * 100)));
            p.setScaleType(ImageView.ScaleType.CENTER_CROP);
            p.setImageResource(imgs[position]);
            //把PhotoView当普通的控件把触摸功能关掉
            p.disenable();
            return p;
		}
	};
}
