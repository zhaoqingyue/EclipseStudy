package com.zhaoqy.app.demo.notebook.billnotes.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class BillTransferActivity extends Activity implements OnClickListener
{
	private final static int Roll_Code = 1;
	private Context  mContext;
	private View     mAccountIn;
	private View     mAccountOut;
	private Button   mBack;
	private String   mAccountName;
	private TextView mName;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billnotes_transfer);
		mContext = this;
		
		initView();
		setListener();
	}

	private void initView() 
	{
		mAccountOut = findViewById(R.id.outaccount);
		mAccountIn = findViewById(R.id.inaccount);
		mBack = (Button) findViewById(R.id.back_btn_of_roll);
		mName=(TextView) findViewById(R.id.in_text);
	}
	
	private void setListener() 
	{
		mAccountIn.setOnClickListener(this);
		mAccountOut.setOnClickListener(this);
		mBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.outaccount:
		{
			Intent intent = new Intent(mContext, BillSelectActivity.class);
			startActivityForResult(intent, Roll_Code);
			break;
		}
		case R.id.inaccount:
		{
			Intent intent = new Intent(mContext, BillSelectActivity.class);
			startActivityForResult(intent, Roll_Code);
			break;
		}
		case R.id.back_btn_of_roll:
		{
			finish();
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(data == null)
		{
			return;
		}
		switch (requestCode) 
		{
		case Roll_Code:
		{
			mAccountName = data.getStringExtra("data");
			mName.setText(mAccountName);
			break;
		}
		default:
			break;
		}
	}
}
