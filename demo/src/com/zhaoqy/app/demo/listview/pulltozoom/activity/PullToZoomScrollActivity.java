package com.zhaoqy.app.demo.listview.pulltozoom.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.listview.pulltozoom.view.PullToZoomScrollViewEx;

public class PullToZoomScrollActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private PullToZoomScrollViewEx scrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview_pulltozoom_scroll);
		mContext = this;
		
		loadViewForCode();
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("PullToZoomScrollView");
		
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
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
    public boolean onCreateOptionsMenu(Menu menu) 
	{
        getMenuInflater().inflate(R.menu.pulltozoom_scroll, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId()) 
    	{
		case android.R.id.home:
		{
			finish();
			return true;
		}
		case R.id.action_normal:
		{
			scrollView.setParallax(false);
			return true;
		}
		case R.id.action_parallax:
		{
			scrollView.setParallax(true);
			return true;
		}
		case R.id.action_show_head:
		{
			scrollView.setHideHeader(false);
			return true;
		}
		case R.id.action_hide_head:
		{
			scrollView.setHideHeader(true);
			return true;
		}
		case R.id.action_disable_zoom:
		{
			scrollView.setZoomEnabled(false);
			return true;
		}
		case R.id.action_enable_zoom:
		{
			scrollView.setZoomEnabled(true);
			return true;
		}
		default:
			break;
		}
        return super.onOptionsItemSelected(item);
    }

    private void loadViewForCode() 
    {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(mContext).inflate(R.layout.view_pulltozoom_profile_head, null, false);
        View zoomView = LayoutInflater.from(mContext).inflate(R.layout.view_pulltozoom_profile_zoom, null, false);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.view_pulltozoom_profile_content, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
    }
}
