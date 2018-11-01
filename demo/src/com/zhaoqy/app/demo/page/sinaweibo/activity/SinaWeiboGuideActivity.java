package com.zhaoqy.app.demo.page.sinaweibo.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.zhaoqy.app.demo.R;

public class SinaWeiboGuideActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ViewPager mPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_sinaweibo_guide);
		mContext = this;
		
		checkFirst();
	}
	
	private void checkFirst() 
	{
		if(getFirst())
		{
			saveFirst();
			initView();
		}
		else
		{
			startActivity(new Intent(mContext, SinaWeiboActivity.class));
			overridePendingTransition(R.anim.page_slide_sinaweibo_in, R.anim.page_slide_sinaweibo_out);
			finish();
		}
	}

	private void initView() 
	{
		mPager = (ViewPager) findViewById(R.id.viewpage);
		mPager.setVisibility(View.VISIBLE);
		
		List<View> views = new ArrayList<View>();
		ImageView iv0 = new ImageView(this);
		iv0.setScaleType(ScaleType.FIT_XY);
		iv0.setImageResource(R.drawable.page_slide_sinaweibo_guide_01);

		ImageView iv1 = new ImageView(this);
		iv1.setScaleType(ScaleType.FIT_XY);
		iv1.setImageResource(R.drawable.page_slide_sinaweibo_guide_02);

		ImageView iv2 = new ImageView(this);
		iv2.setScaleType(ScaleType.FIT_XY);
		iv2.setImageResource(R.drawable.page_slide_sinaweibo_guide_03);

		View iv3 = getLayoutInflater().inflate(R.layout.view_page_slide_sinaweibo_guide, null);
		ImageView btstarweibo = (ImageView) iv3.findViewById(R.id.id_sinaweibo_start);
		btstarweibo.setOnClickListener(this);

		views.add(iv0);
		views.add(iv1);
		views.add(iv2);
		views.add(iv3);
		MypageAdapter adapter = new MypageAdapter(views);
		mPager.setAdapter(adapter);
	}
	
	public class MypageAdapter extends PagerAdapter
	{
		List<View> views;
		
		public MypageAdapter(List<View> views)
		{
			this.views=views;
		}
		
		@Override
		public int getCount() 
		{
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) 
		{
			return arg0==arg1;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) 
		{
			if (position > 0) 
			{
				((ViewPager) container).removeView(views.get(position));
			}
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) 
		{
			try 
			{
				((ViewPager) container).addView(views.get(position), 0);
			} 
			catch (Exception e) 
			{
			}
			return views.get(position);
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_sinaweibo_start:
		{
			startActivity(new Intent(mContext, SinaWeiboActivity.class));
			overridePendingTransition(R.anim.page_slide_sinaweibo_in, R.anim.page_slide_sinaweibo_out);
			finish();
			break;
		}
		default:
			break;
		}
	}
	
	private Boolean getFirst()
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = getSharedPreferences("sp", mContext.MODE_PRIVATE);
		return sp.getBoolean("SinaWeiboFirst", true);
	}
	
	private void saveFirst()
	{
		@SuppressWarnings("static-access")
		SharedPreferences sp = getSharedPreferences("sp", mContext.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("SinaWeiboFirst", false);
		editor.commit();
	}
}
