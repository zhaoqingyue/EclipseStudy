package com.zhaoqy.app.demo.notebook.billnotes.activity;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.billnotes.adapter.ViewPagerAdapter;
import com.zhaoqy.app.demo.notebook.billnotes.fragment.AccountFragment;
import com.zhaoqy.app.demo.notebook.billnotes.fragment.BlotterFragment;
import com.zhaoqy.app.demo.notebook.billnotes.fragment.MyBillFragment;

public class BillMainActivity extends FragmentActivity implements OnClickListener, OnPageChangeListener
{
	private ViewPager        mViewPager;
	private ViewPagerAdapter mAdapter;
	private List<Fragment>   mList;
	private FragmentManager  mFragmentManager;
	private BlotterFragment  mNotebookFrag;
	private MyBillFragment   mBillFrag;
	private AccountFragment  mAccountFrag;
	private RelativeLayout   mNotebook;
	private RelativeLayout   mBill;
	private RelativeLayout   mAccount;
	private View             mNotebookMark;
	private View             mBillMark;
	private View             mAccountMark;
	private TextView         mNotebookName;
	private TextView         mBillName;
	private TextView         mAccountName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billnotes_main);
		
		initView();
		initData();
		initState();
		setListener();
	}

	private void initView()
	{
		mViewPager = (ViewPager)findViewById(R.id.billnotes_main_viewpager);
		mNotebook = (RelativeLayout)findViewById(R.id.billnotes_bottom_notebook);
		mBill = (RelativeLayout)findViewById(R.id.billnotes_bottom_bill);
		mAccount = (RelativeLayout)findViewById(R.id.billnotes_bottom_account);
		mNotebookName = (TextView)findViewById(R.id.billnotes_bottom_notebook_name);
		mBillName = (TextView)findViewById(R.id.billnotes_bottom_bill_name);
		mAccountName = (TextView)findViewById(R.id.billnotes_bottom_account_name);
		mNotebookMark = findViewById(R.id.billnotes_bottom_notebook_mark);
		mBillMark = findViewById(R.id.billnotes_bottom_bill_mark);
		mAccountMark = findViewById(R.id.billnotes_bottom_account_mark);
	}
	
	private void initData()
	{
		mFragmentManager = getSupportFragmentManager();
		mNotebookFrag = new BlotterFragment();
		mBillFrag = new MyBillFragment();
		mAccountFrag = new AccountFragment();
		mList = new ArrayList<Fragment>();
		mList.add(mNotebookFrag);
		mList.add(mBillFrag);
		mList.add(mAccountFrag);
		mAdapter = new ViewPagerAdapter(mFragmentManager, mList);
		mViewPager.setAdapter(mAdapter);
	}
	
	private void initState()
	{
		mNotebookName.setTextColor(mNotebookName.getResources().getColor(R.color.billnote_black));
		mNotebookMark.setBackgroundColor(mNotebookMark.getResources().getColor(R.color.billnote_blue));
	    mViewPager.setCurrentItem(0);
	}
	
	private void setListener() 
	{
		mViewPager.setOnPageChangeListener(this);
		mNotebook.setOnClickListener(this);
		mBill.setOnClickListener(this);
		mAccount.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) 
	{
		clearChoicked();
		changechoicked(v.getId());
	}

	@Override
	public void onPageScrollStateChanged(int arg0) 
	{
		if(arg0 == 2)
		{
			int resource = mViewPager.getCurrentItem();
			clearChoicked();
			changechoicked(resource);
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) 
	{
	}

	@Override
	public void onPageSelected(int arg0) 
	{
	}
	
	private  void clearChoicked()
	{
		mNotebookName.setTextColor(getResources().getColor(R.color.billnote_black));
		mBillName.setTextColor(getResources().getColor(R.color.billnote_black));
		mAccountName.setTextColor(getResources().getColor(R.color.billnote_black));
		mNotebookMark.setBackgroundColor(getResources().getColor(R.color.billnote_line));
		mBillMark.setBackgroundColor(getResources().getColor(R.color.billnote_line));
		mAccountMark.setBackgroundColor(getResources().getColor(R.color.billnote_line));
	}

	public void changechoicked(int resourceAndVnum) 
	{
		switch (resourceAndVnum) 
		{
		case R.id.billnotes_bottom_notebook:
		{
			mNotebookName.setTextColor(getResources().getColor(R.color.billnote_blue));
			mNotebookMark.setBackgroundColor(getResources().getColor(R.color.billnote_blue));
			mViewPager.setCurrentItem(0);
			break;
		}
		case R.id.billnotes_bottom_bill:
		{
			mBillName.setTextColor(getResources().getColor(R.color.billnote_blue));
			mBillMark.setBackgroundColor(getResources().getColor(R.color.billnote_blue));
			mViewPager.setCurrentItem(1);
			break;
		}
		case R.id.billnotes_bottom_account:
		{
			mAccountName.setTextColor(getResources().getColor(R.color.billnote_blue));
			mAccountMark.setBackgroundColor(getResources().getColor(R.color.billnote_blue));
			mViewPager.setCurrentItem(2);
			break;
		}
		default:
			break;
		}
	}
}
