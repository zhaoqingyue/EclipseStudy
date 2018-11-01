package com.zhaoqy.app.demo.wifi.wifihot.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;

@SuppressLint("DefaultLocale")
public class WifiAdapter extends BaseAdapter 
{
	private Context             mContext;
	private List<ScanResult>    mDatas;
	private WifiManager         mWifiManager;
	private ConnectivityManager mConnectivityManager;

	public void setDatas(List<ScanResult> datas) 
	{
		mDatas = datas;
	}

	public WifiAdapter(Context context, List<ScanResult> datas) 
	{
		super();
		mDatas = datas;
		mContext = context;
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	@Override
	public int getCount() 
	{
		if (mDatas == null) 
		{
			return 0;
		}
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		Holder tag = null;
		if (convertView == null) 
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.view_wifi_item, null);
			tag = new Holder();
			tag.mName = (TextView) convertView.findViewById(R.id.id_wifi_item_name);
			tag.mDesc = (TextView) convertView.findViewById(R.id.id_wifi_item_desc);
			tag.mLevel = (ImageView) convertView.findViewById(R.id.tid_wifi_item_level);
			convertView.setTag(tag);
		}

		Holder holder = (Holder) convertView.getTag();
		holder.mName.setText(mDatas.get(position).SSID);
		String desc = "";
		String descOri = mDatas.get(position).capabilities;
		if (descOri.toUpperCase().contains("WPA-PSK")) 
		{
			desc = "WPA";
		}
		if (descOri.toUpperCase().contains("WPA2-PSK")) 
		{
			desc = "WPA2";
		}
		if (descOri.toUpperCase().contains("WPA-PSK") && descOri.toUpperCase().contains("WPA2-PSK")) 
		{
			desc = "WPA/WPA2";
		}

		if (TextUtils.isEmpty(desc)) 
		{
			desc = mContext.getResources().getString(R.string.wifi_status_unprotect);
		} 
		else 
		{
			desc = mContext.getResources().getString(R.string.wifi_status_by) + " " + desc + " " + mContext.getResources().getString(R.string.wifi_status_protect);
		}

		State wifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (wifi == State.CONNECTED) 
		{
			WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
			String g1 = wifiInfo.getSSID();
			String g2 = "\"" + mDatas.get(position).SSID + "\"";
			if (g2.endsWith(g1)) 
			{
				desc = mContext.getResources().getString(R.string.wifi_connected);
			}
		}
		holder.mDesc.setText(desc);
		int level = mDatas.get(position).level;
		int imgId = R.drawable.wifi05;
		if (Math.abs(level) > 100)
		{
			imgId = R.drawable.wifi05;
		} 
		else if (Math.abs(level) > 80) 
		{
			imgId = R.drawable.wifi04;
		} 
		else if (Math.abs(level) > 70) 
		{
			imgId = R.drawable.wifi04;
		} 
		else if (Math.abs(level) > 60) 
		{
			imgId = R.drawable.wifi03;
		} 
		else if (Math.abs(level) > 50) 
		{
			imgId = R.drawable.wifi02;
		} 
		else 
		{
			imgId = R.drawable.wifi01;
		}
		holder.mLevel.setImageResource(imgId);
		return convertView;
	}

	public static class Holder 
	{
		public TextView  mName;
		public TextView  mDesc;
		public ImageView mLevel;
	}
}