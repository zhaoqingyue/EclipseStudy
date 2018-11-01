/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MeFragment.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.vmall.fragment
 * @Description: 华为商城--我的
 * @author: zhaoqy
 * @date: 2015-12-18 下午3:21:59
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.vmall.fragment;


import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.vmall.util.UrlUtil;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("NewApi")
public class MeFragment extends Fragment 
{
	private WebView me_wv;

	@SuppressLint("SetJavaScriptEnabled")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_vmall_me, null);

		me_wv = (WebView) view.findViewById(R.id.me_wv);
		me_wv.loadUrl(UrlUtil.personal);
		me_wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		me_wv.getSettings().setJavaScriptEnabled(true);
		
		return view;
	}
}
