/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: MenuKaixinCheckInActivity.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.activity
 * @Description: 签到
 * @author: zhaoqy
 * @date: 2015-11-9 下午2:25:18
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.activity;

import java.io.InputStream;
import org.json.JSONArray;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.camera.qrcode.activity.QRCodeScanActivity;
import com.zhaoqy.app.demo.menu.kaixin.item.LocationResult;
import com.zhaoqy.app.demo.menu.kaixin.util.TextUtil;
import com.zhaoqy.app.demo.menu.kaixin.view.CustomListView;

public class MenuKaixinCheckInActivity extends MenuKaixinBaseActivity 
{
	private CustomListView mDisplay;
	private Context   mContext;
	private ImageView mBack;
	private TextView  mTitle;
	private TextView  mCapture;
	private EditText  mSearch;
	private int       mCount;  

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_kaixin_chechin);
		mContext = this;
		
		findViewById();
		setListener();
		init();
	}

	private void findViewById() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mCapture = (TextView) findViewById(R.id.id_title_right_text);
		mSearch = (EditText) findViewById(R.id.checkin_search);
		mDisplay = (CustomListView) findViewById(R.id.checkin_display);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) 
			{
				//关闭当前界面
				finish();
			}
		});
		mCapture.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) 
			{
				//跳转到二维码扫描
				startActivity(new Intent(mContext, QRCodeScanActivity.class));
			}
		});
		mSearch.addTextChangedListener(new TextWatcher() 
		{
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				// 暂时不做查找功能
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) 
			{
			}

			public void afterTextChanged(Editable s) 
			{
			}
		});
	}

	private void init() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText("附近");
		mCapture.setVisibility(View.VISIBLE);
		mCapture.setText("扫描二维码");
		
		//获取地理位置数据
		getLocation();
		//设置内容总长度
		mCount = mApplication.mMyLocationResults.size() + 3;
		//添加适配器
		mDisplay.setAdapter(new CheckInAdapter());
	}

	/**
	 * 获取地理位置数据
	 */
	private void getLocation() 
	{
		if (mApplication.mMyLocationResults.isEmpty()) 
		{
			InputStream inputStream;
			try 
			{
				inputStream = getAssets().open("data/my_location.KX");
				String json = new TextUtil(mApplication).readTextFile(inputStream);
				JSONArray array = new JSONArray(json);
				LocationResult result = null;
				for (int i = 0; i < array.length(); i++) 
				{
					result = new LocationResult();
					result.setName(array.getJSONObject(i).getString("name"));
					result.setLocation(array.getJSONObject(i).getString("location"));
					mApplication.mMyLocationResults.add(result);
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	private class CheckInAdapter extends BaseAdapter 
	{
		public int getCount() 
		{
			return mCount;
		}

		public Object getItem(int arg0) 
		{
			return null;
		}

		public long getItemId(int position) 
		{
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ViewHolder holder = null;
			if (convertView == null) 
			{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_kaixin_checkin, null);
				holder = new ViewHolder();
				holder.top = (View) convertView.findViewById(R.id.checkin_item_top);
				holder.mid = (View) convertView.findViewById(R.id.checkin_item_mid);
				holder.more = (View) convertView.findViewById(R.id.checkin_item_more);
				holder.bottom = (View) convertView.findViewById(R.id.checkin_item_bottom);
				holder.name = (TextView) holder.mid.findViewById(R.id.checkin_item_mid_name);
				holder.location = (TextView) holder.mid.findViewById(R.id.checkin_item_mid_location);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if (position == 0) 
			{
				holder.top.setVisibility(View.VISIBLE);
				holder.mid.setVisibility(View.GONE);
				holder.more.setVisibility(View.GONE);
				holder.bottom.setVisibility(View.GONE);
			} 
			else if (position == mCount - 2) 
			{
				holder.top.setVisibility(View.GONE);
				holder.mid.setVisibility(View.GONE);
				holder.more.setVisibility(View.VISIBLE);
				holder.bottom.setVisibility(View.GONE);
			} 
			else if (position == mCount - 1) 
			{
				holder.top.setVisibility(View.GONE);
				holder.mid.setVisibility(View.GONE);
				holder.more.setVisibility(View.GONE);
				holder.bottom.setVisibility(View.VISIBLE);
			}
			else 
			{
				holder.top.setVisibility(View.GONE);
				holder.mid.setVisibility(View.VISIBLE);
				holder.more.setVisibility(View.GONE);
				holder.bottom.setVisibility(View.GONE);
				LocationResult result = mApplication.mMyLocationResults.get(position - 1);
				holder.name.setText(result.getName());
				holder.location.setText(result.getLocation());
			}
			return convertView;
		}

		class ViewHolder 
		{
			View     top;
			View     mid;
			View     more;
			View     bottom;
			TextView name;
			TextView location;
		}
	}
}
