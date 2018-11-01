package com.zhaoqy.app.demo.weixin.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.register.ToastUtil;

public class ShareActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mRight;
	private EditText  mEditText;
	private Button    mAdd;
	private TextView  mLocation;
	private TextView  mLook;
	private TextView  mTip;
	private ImageView mQzone;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weixin_share);
		mContext = this;
		initView();
		setListener();
		initData();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mRight = (TextView) findViewById(R.id.id_title_right_text);
		
		mEditText = (EditText) findViewById(R.id.id_share_edittext);
		mAdd = (Button) findViewById(R.id.id_share_add);
		mLocation = (TextView) findViewById(R.id.id_share_location);
		mLook = (TextView) findViewById(R.id.id_share_look);
		mTip = (TextView) findViewById(R.id.id_share_tip);
		mQzone = (ImageView) findViewById(R.id.id_share_qzone);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mRight.setOnClickListener(this);
		mAdd.setOnClickListener(this);
		mLocation.setOnClickListener(this);
		mLook.setOnClickListener(this);
		mTip.setOnClickListener(this);
		mQzone.setOnClickListener(this);
	}

	private void initData() 
	{
		mTitle.setText(R.string.share_friend_circle);
		mBack.setVisibility(View.VISIBLE);
		mRight.setText(R.string.share_send);
		mRight.setTextColor(0xFF45C01A);
		mRight.setVisibility(View.VISIBLE);
		mEditText.getText();
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
		case R.id.id_title_right_text:
		{
			Intent intent=new Intent(Intent.ACTION_SEND);  
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_SUBJECT, R.string.share_friend_circle);  
			intent.putExtra(Intent.EXTRA_TEXT, R.string.share_friend_circle);  
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			startActivity(Intent.createChooser(intent, getTitle()));  
			
			/*Intent intent = getIntent();
			String action = intent.getAction();
			String type = intent.getType();

			if (Intent.ACTION_SEND.equals(action) && type != null) 
			{
				if (type.equals("text/plain")) 
				{
					handleSendText(intent);
				} 
				else if (type.startsWith("image/")) 
				{
					handleSendImage(intent);
				}
			} */
			break;
		}
		case R.id.id_share_add:
		{
			break;
		}
		case R.id.id_share_location:
		{
			Intent intent = new Intent();
			intent.setClass(mContext, BaiduMapActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.id_share_look:
		{
			break;
		}
		case R.id.id_share_tip:
		{
			break;
		}
		case R.id.id_share_qzone:
		{
			break;
		}
		default:
			break;
		}
	}
	
	void handleSendText(Intent intent) 
	{
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		String sharedTitle = intent.getStringExtra(Intent.EXTRA_TITLE);
		if (sharedText != null) 
		{
			ToastUtil.showLongToast(mContext, sharedText + sharedTitle);
		}
	}

	void handleSendImage(Intent intent) 
	{
		Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (imageUri != null) 
		{
			ToastUtil.showLongToast(mContext, imageUri.toString());
		}
	}
}
