package com.zhaoqy.app.demo.page.sinaweibo.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import com.zhaoqy.app.demo.R;

@SuppressWarnings("deprecation")
public class SinaWeiboActivity extends TabActivity implements OnCheckedChangeListener, OnClickListener
{
	private Context    mContext;
	private ImageView  mBack;
	private TextView   mTitle;
	private RadioGroup mGroup;
	private TabHost    mHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_slide_sinaweibo);
		mContext = this;
		
		initView();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mGroup = (RadioGroup) findViewById(R.id.id_sinaweibo_radiogroup);
		mHost = getTabHost();

		TabSpec spec0 = mHost.newTabSpec("home").setIndicator("home").setContent(new Intent(mContext, SinaWeiboHomeActivity.class));
		mHost.addTab(spec0);
		TabSpec spec1 = mHost.newTabSpec("msg").setIndicator("msg").setContent(new Intent(mContext, SinaWeiboMessageActivity.class));
		mHost.addTab(spec1);
		TabSpec spec2 = mHost.newTabSpec("friend").setIndicator("friend").setContent(new Intent(mContext, SinaWeiboFriendActivity.class));
		mHost.addTab(spec2);
		TabSpec spec3 = mHost.newTabSpec("squar").setIndicator("squar").setContent(new Intent(mContext, SinaWeiboSquarActivity.class));
		mHost.addTab(spec3);
		TabSpec spec4 = mHost.newTabSpec("more").setIndicator("more").setContent(new Intent(mContext, SinaWeiboMoreActivity.class));
		mHost.addTab(spec4);
		
		((RadioButton) findViewById(R.id.id_sinaweibo_radio_home)).setChecked(true);
		mGroup.setOnCheckedChangeListener(this);
		mBack.setOnClickListener(this);
		
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(getResources().getString(R.string.page_slide_item7));
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) 
	{
		switch (checkedId) 
		{
		case R.id.id_sinaweibo_radio_home:
		{
			mHost.setCurrentTabByTag("home");
			break;
		}
		case R.id.id_sinaweibo_radio_message:
		{
			mHost.setCurrentTabByTag("msg");
			break;
		}
		case R.id.id_sinaweibo_radio_friend:
		{
			mHost.setCurrentTabByTag("friend");
			break;
		}
		case R.id.id_sinaweibo_radio_squar:
		{
			mHost.setCurrentTabByTag("squar");
			break;
		}
		case R.id.id_sinaweibo_radio_more:
		{
			mHost.setCurrentTabByTag("more");
			break;
		}
		default:
			break;
		}
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
}
