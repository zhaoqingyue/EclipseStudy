package com.zhaoqy.app.demo.notebook.billnotes.activity;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.billnotes.adapter.SelectAdapter;
import com.zhaoqy.app.demo.notebook.billnotes.db.UserManager;
import com.zhaoqy.app.demo.notebook.billnotes.item.BillItem;

public class BillSelectActivity extends Activity implements OnItemClickListener
{
	private SelectAdapter  mAdapter;
	private List<BillItem> mList;
	private UserManager    mManager;
	private ListView       mListView;
	private Context        mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billnotes_select);
		mContext = this;
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		initView();
		initData();
		setListener();
	}

	private void initView()
	{
		mListView = (ListView)findViewById(R.id.listview_of_diact);
	}
	
	private void initData()
	{
		mManager = new UserManager(mContext);
		mList = mManager.getAccount();
		mAdapter = new SelectAdapter(mContext, mList);
		mListView.setAdapter(mAdapter);
	}
	
	private void setListener() 
	{
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		String st = mList.get(position).getName();
		Toast.makeText(mContext, st, Toast.LENGTH_SHORT).show();
		getIntent().putExtra("data", st);
		setResult(RESULT_OK, getIntent());
		finish();
	}
}
