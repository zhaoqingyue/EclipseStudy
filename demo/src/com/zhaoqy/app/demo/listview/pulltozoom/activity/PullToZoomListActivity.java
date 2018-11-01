package com.zhaoqy.app.demo.listview.pulltozoom.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.listview.pulltozoom.view.PullToZoomListViewEx;

public class PullToZoomListActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private PullToZoomListViewEx mListView;
	
	private String[] adapterData = new String[] { "Activity", "Service",
			"Content Provider", "Intent", "BroadcastReceiver", "ADT",
			"Sqlite3", "HttpClient", "DDMS", "Android Studio", "Fragment",
			"Loader", "Activity", "Service", "Content Provider", "Intent",
			"BroadcastReceiver", "ADT", "Sqlite3", "HttpClient", "Activity",
			"Service", "Content Provider", "Intent", "BroadcastReceiver",
			"ADT", "Sqlite3", "HttpClient" };

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_pulltozoom_list);
        mContext = this;
		
		initView();
		setListener();
		initData();
    }
    
    private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mListView = (PullToZoomListViewEx) findViewById(R.id.listview);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("PullToZoomListView");
		
		mListView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.item_listview_paging, adapterData));
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        mListView.setHeaderLayoutParams(localObject);
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
    	getMenuInflater().inflate(R.menu.pulltozoom_list, menu);
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
			mListView.setParallax(false);
            return true;
		}	
		case R.id.action_parallax:
		{
			mListView.setParallax(true);
            return true;
		}	
		case R.id.action_show_head:
		{
			mListView.setHideHeader(false);
            return true;
		}	
		case R.id.action_hide_head:
		{
			mListView.setHideHeader(true);
            return true;
		}	
		case R.id.action_disable_zoom:
		{
			mListView.setZoomEnabled(false);
            return true;
		}	
		case R.id.action_enable_zoom:
		{
			mListView.setZoomEnabled(true);
            return true;
		}	
		default:
			break;
		}
        return super.onOptionsItemSelected(item);
    }
}
