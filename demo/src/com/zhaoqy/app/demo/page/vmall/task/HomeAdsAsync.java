/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: HomeAdsAsync.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.vmall.task
 * @Description: 首页广告异步task
 * @author: zhaoqy
 * @date: 2015-12-18 上午11:41:47
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.vmall.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.vmall.activity.VmallDetailActivity;
import com.zhaoqy.app.demo.page.vmall.adapter.HomePagerAdapter;
import com.zhaoqy.app.demo.page.vmall.fragment.HomeFragment.HomeViewAd;
import com.zhaoqy.app.demo.page.vmall.item.Ads;
import com.zhaoqy.app.demo.page.vmall.util.Constant;
import com.zhaoqy.app.demo.page.vmall.util.DBManager;
import com.zhaoqy.app.demo.page.vmall.util.HttpUtil;
import com.zhaoqy.app.demo.page.vmall.util.PaserUtil;
import com.zhaoqy.app.demo.page.vmall.util.VmallStatic;

public class HomeAdsAsync extends AsyncTask<String, Void, Map<String, Object>> 
{
	private Map<String, Bitmap> map = new HashMap<String, Bitmap>();
	private Context context;
	private HomeViewAd hva;
	private ProgressDialog pd;
	private ImageView[] icons;

	public HomeAdsAsync(Context context, HomeViewAd hva) 
	{
		this.context = context;
		this.hva = hva;
	}

	protected void onPreExecute() 
	{
		pd = new ProgressDialog(context);
		pd.setTitle("Loading...");
		pd.show();
	}

	protected Map<String, Object> doInBackground(String... params) 
	{
		Map<String, Object> map = null;
		Cursor cursor = DBManager.rawQuery(VmallStatic.db, "select * from "+Constant.TABLE_NAME+" where "+Constant._ID+"='"+params[0]+"'",null);
		if (cursor.getCount() == 1) 
		{
			byte[] buff = DBManager.CursorToByte(cursor);
			map = PaserUtil.parserHomeAdsJson(buff);
		} 
		else 
		{
			if (HttpUtil.isHaveInternet(context)) 
			{
				byte[] buff = HttpUtil.getDataFromHttp(params[0]);
				String sql = "insert into " + Constant.TABLE_NAME + " values('" + params[0] + "','" + new String(buff, 0, buff.length) + "')";
				DBManager.execSQL(VmallStatic.db, sql);
				map = PaserUtil.parserHomeAdsJson(buff);
			}
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	protected void onPostExecute(Map<String, Object> result) 
	{
		if (result != null && result.size() != 0) 
		{
			List<Ads> list0 = (List<Ads>) result.get("locationAds");
			hva.home_locationAds_0.setContentDescription(list0.get(0).getAdPrdUrl());
			hva.home_locationAds_0.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					Intent intent = new Intent(context, VmallDetailActivity.class);
					intent.putExtra("url", hva.home_locationAds_0.getContentDescription());
					context.startActivity(intent);
				}
			});
			hva.home_locationAds_0.setImageResource(R.drawable.vmall_home_icon_no_pic);
			String str = list0.get(0).getAdPicUrl();
			hva.home_locationAds_0.setTag(str);
			if (!map.containsKey(str)) 
			{
				new DownSaveImgAsync(context, new DownSaveImgAsync.CallBack() 
				{
					public void sendImage(Bitmap bm, String key) 
					{
						hva.home_locationAds_0.setImageBitmap(bm);
					}
				}, map).execute(list0.get(0).getAdPicUrl());
			} 
			else 
			{
				hva.home_locationAds_0.setImageBitmap(map.get(str));
			}
			
			hva.home_locationAds_1.setContentDescription(list0.get(1).getAdPrdUrl());
			hva.home_locationAds_1.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v)
				{
					Intent intent = new Intent(context, VmallDetailActivity.class);
					intent.putExtra("url",hva.home_locationAds_1.getContentDescription());
					context.startActivity(intent);
				}
			});
			hva.home_locationAds_1.setImageResource(R.drawable.vmall_home_icon_no_pic);
			String str1 = list0.get(1).getAdPicUrl();
			hva.home_locationAds_1.setTag(str);
			if (!map.containsKey(str1)) 
			{
				new DownSaveImgAsync(context, new DownSaveImgAsync.CallBack() 
				{
					public void sendImage(Bitmap bm, String key) 
					{
						hva.home_locationAds_1.setImageBitmap(bm);
					}
				}, map).execute(list0.get(1).getAdPicUrl());
			} 
			else 
			{
				hva.home_locationAds_1.setImageBitmap(map.get(str));
			}

			List<Ads> list = (List<Ads>) result.get("scrollAds");
			icons = new ImageView[list.size()];
			List<ImageView> ivlist = new ArrayList<ImageView>();
			for (int i = 0; i < list.size(); i++) 
			{
				final ImageView iv = new ImageView(context);
				iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				iv.setScaleType(ScaleType.FIT_XY);
				iv.setContentDescription(list.get(i).getAdPrdUrl());
				iv.setImageResource(R.drawable.vmall_home_icon_no_pic);
				String strs = list.get(i).getAdPicUrl();
				iv.setTag(strs);
				if (!map.containsKey(strs)) 
				{
					new DownSaveImgAsync(context, new DownSaveImgAsync.CallBack() 
					{
						public void sendImage(Bitmap bm, String key) 
						{
							iv.setImageBitmap(bm);
							iv.setOnClickListener(new OnClickListener() 
							{
								public void onClick(View v) 
								{
									Intent intent = new Intent(context, VmallDetailActivity.class);
									intent.putExtra("url", iv.getContentDescription().toString());
									context.startActivity(intent);
								}
							});
						}
					}, map).execute(list.get(i).getAdPicUrl());
				} 
				else 
				{
					iv.setImageBitmap(map.get(strs));
				}
				ivlist.add(iv);
				icons[i] = new ImageView(context);
				icons[i].setImageResource(R.drawable.vmall_main_dot_normal);
				icons[i].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				icons[i].setMaxHeight(20);
				icons[i].setPadding(2, 0, 2, 0);
				icons[i].setAdjustViewBounds(true);
				hva.home_scrollAds_iconlayout.addView(icons[i]);
			}
			icons[0].setImageResource(R.drawable.vmall_main_dot_pressed);
			HomePagerAdapter hvpa = new HomePagerAdapter(ivlist);
			hva.home_scrollAds.setAdapter(hvpa);
			hva.home_scrollAds.setOnPageChangeListener(new OnPageChangeListener() 
			{
				public void onPageSelected(int arg0) 
				{
					arg0 %= icons.length;
					if (arg0 < 0) 
					{
						arg0 = icons.length + arg0;
					}
					for (int i = 0; i < icons.length; i++) 
					{
						icons[i].setImageResource(R.drawable.vmall_main_dot_normal);
					}
					icons[arg0].setImageResource(R.drawable.vmall_main_dot_pressed);
				}

				public void onPageScrolled(int arg0, float arg1, int arg2) 
				{
				}

				public void onPageScrollStateChanged(int arg0) 
				{
				}
			});
			hva.home_scrollAds.setCurrentItem(ivlist.size() * 50);
		}
		pd.dismiss();
	}
}
