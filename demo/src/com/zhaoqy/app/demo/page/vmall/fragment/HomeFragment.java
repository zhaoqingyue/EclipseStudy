/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: HomeFragment.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.vmall.fragment
 * @Description: 华为商城--首页
 * @author: zhaoqy
 * @date: 2015-12-18 上午9:59:26
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.vmall.fragment;

import java.util.List;

import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.vmall.activity.VmallDetailActivity;
import com.zhaoqy.app.demo.page.vmall.task.HomeAdsAsync;
import com.zhaoqy.app.demo.page.vmall.task.HomeHotAsync;
import com.zhaoqy.app.demo.page.vmall.task.HomeListAsync;
import com.zhaoqy.app.demo.page.vmall.util.UrlUtil;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class HomeFragment extends Fragment 
{
	private HomeViewAd homeViewAd;
	private HomeViewList homeViewList;
	private HomeViewHot homeViewHot;

	private ScrollView sv_home;
	private ImageView iv;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			homeViewAd.home_scrollAds.setCurrentItem(homeViewAd.home_scrollAds
					.getCurrentItem() + 1);
		}
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_vmall_home, null);
		homeViewAd = new HomeViewAd();
		homeViewList = new HomeViewList();
		homeViewHot = new HomeViewHot();

		iv = (ImageView) getActivity().findViewById(R.id.ivtotop);
		sv_home = (ScrollView) view.findViewById(R.id.homeScrollView);
		sv_home.smoothScrollTo(0, 0);
		sv_home.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					iv.setVisibility(View.GONE);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					iv.setVisibility(View.VISIBLE);
					if (sv_home.getScrollY() == 0) {
						iv.setVisibility(View.GONE);
					}
				}
				return false;
			}
		});
		iv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sv_home.smoothScrollTo(0, 0);
				iv.setVisibility(View.GONE);
			}
		});

		homeViewAd.home_scrollAds = (ViewPager) view
				.findViewById(R.id.home_scrollAds);
		homeViewAd.home_scrollAds_iconlayout = (LinearLayout) view
				.findViewById(R.id.home_scrollAds_iconlayout);
		homeViewAd.home_locationAds_0 = (ImageView) view
				.findViewById(R.id.home_locationAds_0);
		homeViewAd.home_locationAds_1 = (ImageView) view
				.findViewById(R.id.home_locationAds_1);
		new HomeAdsAsync(getActivity(), homeViewAd)
				.execute(UrlUtil.homeAdInfo);

		homeViewList.home_gvstar = (GridView) view
				.findViewById(R.id.home_gvstar);
		homeViewList.home_gvreco = (GridView) view
				.findViewById(R.id.home_gvreco);
		new HomeListAsync(getActivity(), homeViewList)
				.execute(UrlUtil.homeRegionList);
		homeViewList.home_gvstar
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ImageView iv = (ImageView) view
								.findViewById(R.id.grid_stat_ivprdPic);
						Intent intent = new Intent(getActivity(),
								VmallDetailActivity.class);
						intent.putExtra("url", iv.getContentDescription()
								.toString());
						startActivity(intent);
					}
				});
		homeViewList.home_gvreco
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ImageView iv = (ImageView) view
								.findViewById(R.id.grid_reco_ivprdPic);
						Intent intent = new Intent(getActivity(),
								VmallDetailActivity.class);
						intent.putExtra("url", iv.getContentDescription()
								.toString());
						startActivity(intent);
					}
				});

		homeViewHot.home_gvhot = (GridView) view.findViewById(R.id.home_gvhot);
		new HomeHotAsync(getActivity(), homeViewHot)
				.execute(UrlUtil.getHotWords);

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					SystemClock.sleep(3000);
					handler.sendEmptyMessage(0);
				}
			}
		}).start();
		return view;
	}

	public void onHiddenChanged(boolean hidden) {
		if (!hidden && sv_home.getScrollY() == 0) {
			sv_home.smoothScrollTo(0, 0);
		}
		super.onHiddenChanged(hidden);
	}

	public interface CallBack {
		abstract void sendList(List<String> list);
	}

	public static class HomeViewAd {
		public ViewPager home_scrollAds;
		public LinearLayout home_scrollAds_iconlayout;
		public ImageView home_locationAds_0, home_locationAds_1;
	}

	public static class HomeViewList {
		public GridView home_gvstar, home_gvreco;
	}

	public static class HomeViewHot {
		public GridView home_gvhot;
	}
}
