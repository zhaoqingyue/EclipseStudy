/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: VmallDetailActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.vmall.activity
 * @Description: 华为商城详情页
 * @author: zhaoqy
 * @date: 2015-12-18 上午10:03:21
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.vmall.activity;

import com.zhaoqy.app.demo.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class VmallDetailActivity extends Activity implements OnClickListener
{
	private Context   mContext;
	private ImageView mBack;
	private ImageView mShare;
	private TextView  mTitle;
	private WebView   mWebView;
	private String    mUrl;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vmall_detail);
		mContext = this;
		
		initView();
		initData();
		setListener();
	}
	
	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_detail_back);
		mShare = (ImageView) findViewById(R.id.id_detail_share);
		mTitle = (TextView) findViewById(R.id.id_detail_title);
		mWebView = (WebView) findViewById(R.id.id_detail_webview);
	}

	private void initData() 
	{
		Intent intent = getIntent();
		mUrl = intent.getStringExtra("url");
		mWebView.loadUrl(mUrl);
		WebChromeClient wvcc = new WebChromeClient() 
		{
			public void onReceivedTitle(WebView view, String title) 
			{
				super.onReceivedTitle(view, title);
				mTitle.setText(title);
			}
		};
		mWebView.setWebChromeClient(wvcc);
		mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() 
		{
			public boolean shouldOverrideUrlLoading(WebView view, String url) 
			{
				view.loadUrl(url);
				return true;
			}
		});
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mShare.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_detail_back:
		{
			finish();
			break;
		}
		case R.id.id_detail_share:
		{
			final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
			View view = View.inflate(mContext, R.layout.dialog_vmall_share, null);
			LinearLayout llcopy = (LinearLayout) view.findViewById(R.id.id_dialog_share_copy);
			llcopy.setOnClickListener(new OnClickListener() 
			{
				@SuppressLint("NewApi")
				public void onClick(View v) 
				{
					ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
					cm.setPrimaryClip(ClipData.newPlainText("url", mUrl));
					Toast.makeText(mContext, "已复制", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}
			});
			Button btn = (Button) view.findViewById(R.id.id_dialog_share_cancel);
			btn.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					dialog.dismiss();
				}
			});
			dialog.setView(view, 0, 0, 0, 0);
			dialog.show();
			break;
		}
		default:
			break;
		}
	}
}
