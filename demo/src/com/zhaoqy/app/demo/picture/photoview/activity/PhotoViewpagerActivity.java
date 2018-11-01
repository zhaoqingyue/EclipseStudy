package com.zhaoqy.app.demo.picture.photoview.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.picture.photoview.view.PhotoView;

public class PhotoViewpagerActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private ViewPager mPager;
	private int[] imgsId = new int[]{R.drawable.image_viewpager1, R.drawable.image_viewpager2, R.drawable.image_viewpager3, R.drawable.image_viewpager4 };
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_photo_viewpager);
		mContext = this;
		
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mPager = (ViewPager) findViewById(R.id.pager);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("普通ViewPager");
		
		mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mPager.setAdapter(new PhotoAdapter());
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
	
	private class PhotoAdapter extends PagerAdapter
	{
		 @Override
         public int getCount() 
		 {
             return imgsId.length;
         }

         @Override
         public boolean isViewFromObject(View view, Object object) 
         {
             return view == object;
         }

         @Override
         public Object instantiateItem(ViewGroup container, int position) 
         {
             PhotoView view = new PhotoView(mContext);
             view.enable();
             view.setImageResource(imgsId[position]);
             container.addView(view);
             return view;
         }

         @Override
         public void destroyItem(ViewGroup container, int position, Object object) 
         {
             container.removeView((View) object);
         }
	};
}
