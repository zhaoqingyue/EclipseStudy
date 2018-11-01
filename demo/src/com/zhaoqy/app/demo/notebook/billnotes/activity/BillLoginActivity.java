package com.zhaoqy.app.demo.notebook.billnotes.activity;

import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BillLoginActivity extends Activity implements OnClickListener
{
	private Context  mContext;
	private Button   mDirect;
	private TextView mNoAccout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billnotes_login);
		mContext = this;
		
		initView();
		setListener();
	}

	private void initView()
	{
		mDirect = (Button)findViewById(R.id.id_bill_login_direct);
		mNoAccout = (TextView) findViewById(R.id.id_bill_login_no_account);
	}
	
	private void setListener() 
	{
		mDirect.setOnClickListener(this);
		mNoAccout.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_bill_login_direct:
		{
			Intent intent = new Intent(mContext, BillMainActivity.class);
		    startActivity(intent);
		    finish();
			break;
		}
		case R.id.id_bill_login_no_account:
		{
			Intent intents = new Intent(mContext, BillRegisterActivity.class);
			startActivity(intents);
			break;
		}
		default:
			break;
		}
	}
}
