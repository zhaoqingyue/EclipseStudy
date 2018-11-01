package com.zhaoqy.app.demo.textview.typetextview;

import com.zhaoqy.app.demo.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TypeTextActivity extends Activity implements OnClickListener
{
	private Context       mContext;
	private ImageView     mBack;
	private TextView      mTitle;
	private TypeTextView  mText;
	private Button        mButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_textview_type);
		mContext = this;
		initView();
		setListener();
		initData();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mText = (TypeTextView) findViewById(R.id.id_typetextview_text);
		mButton = (Button) findViewById(R.id.id_typetextview_button);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mText.setOnClickListener(this);
		mButton.setOnClickListener(this);
	}

	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.textview_item0);
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
		case R.id.id_typetextview_button:
		{
			mText.start(mContext.getResources().getString(R.string.textview_item0_text));
			break;
		}
		default:
			break;
		}
	}
}
