package com.zhaoqy.app.demo.weixin.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

public class WebViewActivity extends Activity implements OnClickListener 
{
	private ImageView   mBack;
	private TextView    mTitle;
	private WebView     mWebView;
	private ProgressBar mBar;
	private MyTimer     mTimer;
	private String      mUrl = "";
	private int         mProgress = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		setContentView(R.layout.activity_weixin_web);
		super.onCreate(savedInstanceState);
		initView();
		setListener();
		initData();
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		try 
		{
			mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}
	
	@Override
	public void onPause() 
	{
		super.onPause();
		try 
		{
			mWebView.getClass().getMethod("onPause").invoke(mWebView, (Object[]) null);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}
	
	private void initView() 
	{
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mWebView = (WebView) findViewById(R.id.mwebview);
		mBar = (ProgressBar) findViewById(R.id.progressbar);
	}
	
	private void setListener() 
	{
		mBack.setOnClickListener(this);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.getString("URL") != null) 
		{
			mUrl = "";
			mUrl = bundle.getString("URL");
		}
		if (bundle != null && bundle.getString("Title") != null) 
		{
			mTitle.setText(bundle.getString("Title"));
		}
		if (!TextUtils.isEmpty(mUrl)) 
		{
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.setWebViewClient(new WeiboWebViewClient());
			mWebView.setWebChromeClient(new WebChromeClient());
			mWebView.loadUrl(mUrl);
		}
	}
	
	private class WeiboWebViewClient extends WebViewClient 
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) 
		{
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) 
		{
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) 
		{
			super.onPageStarted(view, url, favicon);
			if (mTimer == null) 
			{
				mTimer = new MyTimer(15000, 50);
			}
			mTimer.start();
			mBar.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) 
		{
			super.onPageFinished(view, url);
			mTimer.cancel();
			mProgress = 0;
			mBar.setProgress(100);
			mBar.setVisibility(View.GONE);
		}
	}

	/* 定义一个倒计时的内部类 */
	private class MyTimer extends CountDownTimer 
	{
		public MyTimer(long millisInFuture, long countDownInterval) 
		{
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() 
		{
			mProgress = 100;
			mBar.setVisibility(View.GONE);
		}

		@Override
		public void onTick(long millisUntilFinished) 
		{
			if (mProgress == 100) 
			{
				mBar.setVisibility(View.GONE);
			} 
			else 
			{
				mBar.setProgress(mProgress++);
			}
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.id_title_left_img:
		{
			if (mWebView.canGoBack())
			{
				mWebView.goBack();
			}
			else
			{
				finish();
			}
			break;
		}
		default:
			break;
		}
	}
}
