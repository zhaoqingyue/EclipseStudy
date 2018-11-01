package com.zhaoqy.app.demo.notebook.billnotes.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.notebook.billnotes.adapter.BillAdapter;
import com.zhaoqy.app.demo.notebook.billnotes.db.UserManager;
import com.zhaoqy.app.demo.notebook.billnotes.item.BillItem;

public class BillAddBillActivity extends Activity implements OnClickListener, OnItemClickListener
{
	private static final int REQCODE = 1;
	private Context        mContext;
	private UserManager    mManager;
	private BillAdapter    mAdapter;
	private GridView       mGridView;
	private BillItem       mBillItem;
	private Integer[]      mIcon;
	private String[]       mName;
	private List<BillItem> mList;
	private Button         mExpenses;
	private Button         mIncome;
	private ImageView      mChangedIcon;
	private TextView       mChangedName;
	private TextView       mChangedNumber;
	private Button         mCash;
	private String inbill;
	private int indeximg;
	private int instate=0;
	private String intype;
	private String intime;
	private BillItem inent;
	
	private BillItem dbent;
	private Long e;

	private TextView one, four, seven, ac, two, five, eight, zero, three, six, nine, point, del, add, ok;
	
	private boolean isChoicked = false;
	private String intext1 = null;
	private String intext2 = null;
	private String intext3 = null;
	private String intext4 = null;
	private String intext5 = null;
	private String intext6 = null;
	private String intext7 = null;
	private String intext8 = null;
	private String intext9 = null;
	private String intext0 = null;
	private String intextpoint = null;

	private double result = 0;
	private double in1 = 0;
	private double in2 = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billnotes_add_bill);
		mContext = this;
		
		initView();
		initExpenses();
		initSelectColor();
		initCalculator();
		
		setListener();
	}

	private void initView() 
	{
		mExpenses = (Button) findViewById(R.id.billnotes_add_bill_expenses);
		mIncome = (Button) findViewById(R.id.billnotes_add_bill_income);
		mChangedName = (TextView) findViewById(R.id.billnotes_add_bill_changed_name);
		mChangedIcon = (ImageView) findViewById(R.id.billnotes_add_bill_changed_icon);
		mChangedNumber = (TextView) findViewById(R.id.billnotes_add_bill_changed_number);
		mGridView = (GridView) findViewById(R.id.billnotes_add_bill_gridview);
		mCash = (Button) findViewById(R.id.billnotes_add_bill_cash);
	}
	
	private void setListener() 
	{
		mExpenses.setOnClickListener(this);
		mIncome.setOnClickListener(this);
		mGridView.setOnItemClickListener(this);
		mCash.setOnClickListener(this);
	}

	private void initCalculator() {
		one = (TextView) findViewById(R.id.one);
		one.setOnClickListener(this);
		two = (TextView) findViewById(R.id.two);
		two.setOnClickListener(this);
		three = (TextView) findViewById(R.id.three);
		three.setOnClickListener(this);
		four = (TextView) findViewById(R.id.four);
		four.setOnClickListener(this);
		five = (TextView) findViewById(R.id.five);
		five.setOnClickListener(this);
		six = (TextView) findViewById(R.id.six);
		six.setOnClickListener(this);
		seven = (TextView) findViewById(R.id.seven);
		seven.setOnClickListener(this);
		eight = (TextView) findViewById(R.id.eight);
		eight.setOnClickListener(this);
		nine = (TextView) findViewById(R.id.nine);
		nine.setOnClickListener(this);
		zero = (TextView) findViewById(R.id.zero);
		zero.setOnClickListener(this);
		ac = (TextView) findViewById(R.id.ac);
		ac.setOnClickListener(this);
		add = (TextView) findViewById(R.id.add);
		add.setOnClickListener(this);
		del = (TextView) findViewById(R.id.del);
		del.setOnClickListener(this);
		point = (TextView) findViewById(R.id.point);
		point.setOnClickListener(this);
		ok = (TextView) findViewById(R.id.ok);
		ok.setOnClickListener(this);
		
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.billnotes_add_bill_expenses:
		{
			initExpenses();
			mExpenses.setBackgroundColor(mExpenses.getResources().getColor(R.color.blue));
			mIncome.setBackgroundColor(mExpenses.getResources().getColor(R.color.white));
			mExpenses.setTextColor(mExpenses.getResources().getColor(R.color.white));
			mIncome.setTextColor(mIncome.getResources().getColor(R.color.black));
			instate = 0;
			break;
		}
		case R.id.billnotes_add_bill_income:
		{
			initIncome();
			mIncome.setBackgroundColor(mIncome.getResources().getColor(R.color.blue));
			mExpenses.setBackgroundColor(mExpenses.getResources().getColor(R.color.white));
			mIncome.setTextColor(mIncome.getResources().getColor(R.color.white));
			mExpenses.setTextColor(mExpenses.getResources().getColor(R.color.black));
			instate = 1;
			break;
		}
		case R.id.billnotes_add_bill_cash:
		{
			Intent intent = new Intent(mContext, BillSelectActivity.class);
			startActivityForResult(intent, REQCODE);
			break;
		}
		case R.id.one:
			if (isChoicked) {
				mChangedNumber.setText(null);
				isChoicked = false;

			}
			intext1 = mChangedNumber.getText().toString();
			intext1 += "1";
			mChangedNumber.setText(intext1);
			break;
		case R.id.two:
			if (isChoicked) {
				mChangedNumber.setText(null);
				isChoicked = false;
			}
			intext2 = mChangedNumber.getText().toString();
			intext2 += "2";
			mChangedNumber.setText(intext2);
			break;
		case R.id.three:
			if (isChoicked) {
				mChangedNumber.setText(null);
				isChoicked = false;
			}
			intext3 = mChangedNumber.getText().toString();
			intext3 += "3";
			mChangedNumber.setText(intext3);
			break;
		case R.id.four:
			if (isChoicked) {
				mChangedNumber.setText(null);
				isChoicked = false;
			}
			intext4 = mChangedNumber.getText().toString();
			intext4 += "4";
			mChangedNumber.setText(intext4);
			break;
		case R.id.five:
			if (isChoicked) {
				mChangedNumber.setText(null);
				isChoicked = false;
			}
			intext5 = mChangedNumber.getText().toString();
			intext5 += "5";
			mChangedNumber.setText(intext5);
			break;
		case R.id.six:
			if (isChoicked) {
				mChangedNumber.setText(null);
				isChoicked = false;
			}
			intext6 = mChangedNumber.getText().toString();
			intext6 += "6";
			mChangedNumber.setText(intext6);
			break;
		case R.id.seven:
			if (isChoicked) {
				mChangedNumber.setText(null);
				isChoicked = false;
			}
			intext7 = mChangedNumber.getText().toString();
			intext7 += "7";
			mChangedNumber.setText(intext7);
			break;
		case R.id.eight:
			if (isChoicked) {
				mChangedNumber.setText(null);
				isChoicked = false;
			}
			intext8 = mChangedNumber.getText().toString();
			intext8 += "8";
			mChangedNumber.setText(intext8);
			break;
		case R.id.nine:
			if (isChoicked) {
				mChangedNumber.setText(null);
				isChoicked = false;
			}
			intext9 = mChangedNumber.getText().toString();
			intext9 += "9";
			mChangedNumber.setText(intext9);
			break;
		case R.id.zero:
			if (isChoicked) {
				mChangedNumber.setText(null);
				isChoicked = false;
			}
			intext0 = mChangedNumber.getText().toString();
			intext0 += "0";
			mChangedNumber.setText(intext0);
			break;

		case R.id.point:

			intextpoint = mChangedNumber.getText() + ".";
			mChangedNumber.setText(intextpoint);
			break;
		case R.id.add:
			if (mChangedNumber.getText().toString().equals(null)
					|| mChangedNumber.getText().toString().equals("")) {
				return;
			}
			in1 = Double.parseDouble(mChangedNumber.getText().toString());
			mChangedNumber.setText(null);
			isChoicked = false;
			break;
		case R.id.ac:
			if (mChangedNumber.getText().toString().equals(null)
					|| mChangedNumber.getText().toString().equals("")) {
				return;
			}

			mChangedNumber.setText(null);
			isChoicked = false;
			break;
		case R.id.del:
			if (mChangedNumber.getText().toString().equals(null)
					|| mChangedNumber.getText().toString().equals("")) {
				return;
			}
			String st = mChangedNumber.getText().toString();
			mChangedNumber.setText(st.subSequence(0, st.length() - 1));
			isChoicked = false;
			break;

		case R.id.ok:

			if (mChangedNumber.getText().toString().equals(null)
					|| mChangedNumber.getText().toString().equals("")) {
				return;
			}
			in2 = Double.parseDouble(mChangedNumber.getText().toString());
			mChangedNumber.setText(null);
			result = in1 + in2;
			mChangedNumber.setText(Double.toString(result));
			isChoicked = true;
			inbill = mChangedNumber.getText().toString().trim();
			in1=0;
			in2=0;
			intype = mChangedName.getText().toString().trim();
			intime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
			inent = new BillItem();
			inent.setBill(inbill);
			inent.setState(instate+"");
			inent.setType(intype);
			inent.setImg(indeximg);
			inent.setTime(intime);
			inent.setName(mCash.getText().toString().trim());
			insertBill(inent);
			UpdataAccount(inent);
			break;
		default:
			break;
		}
	}

	private void initSelectColor() 
	{
		mExpenses.setBackgroundColor(mExpenses.getResources().getColor(R.color.blue));
		mIncome.setBackgroundColor(mExpenses.getResources().getColor(R.color.white));
		mExpenses.setTextColor(mExpenses.getResources().getColor(R.color.white));
		mIncome.setTextColor(mIncome.getResources().getColor(R.color.black));
	}

	private void changedShow(BillItem ent, int position) 
	{
		Integer[] img = ent.getImgs();
		int path = img[position];
		mChangedIcon.setImageResource(path);
		String[] st = ent.getTypes();
		String text = st[position];
		mChangedName.setText(text);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQCODE) 
		{
			switch (resultCode) 
			{
			case RESULT_OK:
			{
				String accountname = data.getStringExtra("data");
				mCash.setText(accountname);
				break;
			}
			default:
				break;
			}
		}
	}

	private long insertBill(BillItem inent) 
	{
		mManager = new UserManager(mContext);
		dbent = mManager.getBills(inent.getType(), inent.getTime());
		if (dbent != null) 
		{
			Double dbbill = Double.valueOf(dbent.getBill());
			Double newbill = Double.valueOf(inent.getBill());
			String bill= Double.toString(dbbill + newbill);
			String id = Integer.toString(dbent.getId());
			ContentValues values = new ContentValues();
			values.put("bill", bill);
			e = mManager.updateBill(id, values);
		} 
		else 
		{
		  	e = mManager.insertBill(inent);
		}
		return e;
	}

	private void UpdataAccount(BillItem gve) 
	{
		mManager = new UserManager(mContext);
		mManager.updateAccount(gve);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		mBillItem = mList.get(position);
		changedShow(mBillItem, position);
		indeximg = position;
	}
	
	private void initExpenses() 
	{
		mIcon = new Integer[] { 
				R.drawable.billnotes_mybill_icon1, 
				R.drawable.billnotes_mybill_icon2, 
				R.drawable.billnotes_mybill_icon3,
				R.drawable.billnotes_mybill_icon4, 
				R.drawable.billnotes_mybill_icon5, 
				R.drawable.billnotes_mybill_icon6, 
				R.drawable.billnotes_mybill_icon7,
				R.drawable.billnotes_mybill_icon8, 
				R.drawable.billnotes_mybill_icon9, 
				R.drawable.billnotes_mybill_icon10, 
				R.drawable.billnotes_mybill_icon11,
				R.drawable.billnotes_mybill_icon12 };
		mName = new String[] { 
				"一般", 
				"美食", 
				"兼职", 
				"交通", 
				"信用卡", 
				"购物", 
				"KTV", 
				"住宅", 
				"咖啡", 
				"网购", 
				"日用品", 
				"其它" };
		mBillItem = new BillItem(mIcon, mName);
		mList = new ArrayList<BillItem>();
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mAdapter = new BillAdapter(mContext, mList);
		mGridView.setAdapter(mAdapter);
	}

	private void initIncome() 
	{
		mIcon = new Integer[] { 
				R.drawable.billnotes_mybill_icon13, 
				R.drawable.billnotes_mybill_icon14, 
				R.drawable.billnotes_mybill_icon15,
				R.drawable.billnotes_mybill_icon16, 
				R.drawable.billnotes_mybill_icon17, 
				R.drawable.billnotes_mybill_icon18, 
				R.drawable.billnotes_mybill_icon19 };
		mName = new String[] { 
				"工资", 
				"投资", 
				"房产", 
				"转账", 
				"理财", 
				"股票", 
				"礼物" };
		mBillItem = new BillItem(mIcon, mName);
		mList = new ArrayList<BillItem>();
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mList.add(mBillItem);
		mAdapter = new BillAdapter(mContext, mList);
		mAdapter.notifyDataSetChanged();
		mGridView.setAdapter(mAdapter);
	}
}
