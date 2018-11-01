package com.zhaoqy.app.demo.listview.paging.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.listview.paging.adapter.PagingAdaper;
import com.zhaoqy.app.demo.listview.paging.util.SafeAsyncTask;
import com.zhaoqy.app.demo.listview.paging.view.PagingListView;
import com.zhaoqy.app.demo.listview.paging.view.PagingListView.Pagingable;

public class PagingActivity extends Activity implements OnClickListener, Pagingable
{
	private ImageView      mBack;
	private TextView       mTitle;
	private PagingListView mListView;
    private PagingAdaper   mAdapter;
    private List<String>   mFirstList;
    private List<String>   mSecondList;
    private List<String>   mThirdList;
    private int            mPager = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview_paging);
		
		initView();
		setListener();
		initList();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mListView = (PagingListView) findViewById(R.id.paging_list_view);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mListView.setPagingableListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.listview_item4);
		mAdapter = new PagingAdaper();
		mListView.setAdapter(mAdapter);
		mListView.setHasMoreItems(true);
	}
	
	private void initList()
	{
		mFirstList = new ArrayList<String>();
		mFirstList.add("Afghanistan");
		mFirstList.add("Albania");
		mFirstList.add("Algeria");
		mFirstList.add("Andorra");
        mFirstList.add("Angola");
        mFirstList.add("Antigua and Barbuda");
        mFirstList.add("Argentina");
        mFirstList.add("Armenia");
        mFirstList.add("Aruba");
        mFirstList.add("Australia");
        mFirstList.add("Austria");
        mFirstList.add("Azerbaijan");

        mSecondList = new ArrayList<String>();
        mSecondList.add("Bahamas, The");
        mSecondList.add("Bahrain");
        mSecondList.add("Bangladesh");
        mSecondList.add("Barbados");
        mSecondList.add("Belarus");
        mSecondList.add("Belgium");
        mSecondList.add("Belize");
        mSecondList.add("Benin");
        mSecondList.add("Bhutan");
        mSecondList.add("Bolivia");
        mSecondList.add("Bosnia and Herzegovina");
        mSecondList.add("Botswana");
        mSecondList.add("Brazil");
        mSecondList.add("Brunei");
        mSecondList.add("Bulgaria");
        mSecondList.add("Burkina Faso");
        mSecondList.add("Burma");
        mSecondList.add("Burundi");

        mThirdList = new ArrayList<String>();
        mThirdList.add("Denmark");
        mThirdList.add("Djibouti");
        mThirdList.add("Dominica");
        mThirdList.add("Dominican Republic");
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
	public void onLoadMoreItems() 
	{
		if (mPager < 3) 
		{
			new CountryAsyncTask().execute();
		} 
		else 
		{
			mListView.onFinishLoading(false, null);
		}
	}

	private class CountryAsyncTask extends SafeAsyncTask<List<String>> 
	{
		@Override
		public List<String> call() throws Exception 
		{
			List<String> result = null;
			switch (mPager) 
			{
			case 0:
				result = mFirstList;
				break;
			case 1:
				result = mSecondList;
				break;
			case 2:
				result = mThirdList;
				break;
			}
			Thread.sleep(3000);
			return result;
		}

		@Override
		protected void onSuccess(List<String> newItems) throws Exception 
		{
			super.onSuccess(newItems);
			mPager++;
			mListView.onFinishLoading(true, newItems);
		}
	}
}
