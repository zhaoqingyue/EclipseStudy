package com.zhaoqy.app.demo.index.contacts.activity;

import com.zhaoqy.app.demo.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AddContactsActivity extends Activity implements OnClickListener
{
	public static final int SEND_SEARCH_NAME = 4;
	public static final int SEND_SEARCH_PHONE = 5;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private EditText  mNameEdit;
	private EditText  mPhoneEdit;
	private Button    mNameContacts;
	private Button    mPhoneContacts;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index_add_contacts);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mNameEdit = (EditText) findViewById(R.id.add_name);
		mPhoneEdit = (EditText) findViewById(R.id.add_phone_number);
		mNameContacts = (Button) findViewById(R.id.add_name_contacts);
		mPhoneContacts = (Button) findViewById(R.id.add_phone_contacts);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mNameContacts.setOnClickListener(this);
		mPhoneContacts.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.index_add_contacts);
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
		case R.id.add_name_contacts:
		{
			Intent intent = new Intent();
			intent.setClass(mContext, ContactsActivity.class);
			intent.putExtra("add_index_name", 1);
			startActivityForResult(intent, SEND_SEARCH_NAME);
			break;
		}
		case R.id.add_phone_contacts:
		{
			Intent intent = new Intent();
			intent.setClass(mContext, ContactsActivity.class);
			intent.putExtra("add_index_phone", 2);
			startActivityForResult(intent, SEND_SEARCH_PHONE);
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		switch (requestCode) 
		{
		case SEND_SEARCH_NAME: 
		{
			if (data != null) 
			{
				Bundle b = data.getExtras();
				String str = b.getString("add_name");
				mNameEdit.setText(str);
			}
			break;
		}
		case SEND_SEARCH_PHONE: 
		{
			if (data != null) 
			{
				Bundle b = data.getExtras();
				String str = b.getString("add_phone");
				mPhoneEdit.setText(str);
			}
			break;
		}
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
