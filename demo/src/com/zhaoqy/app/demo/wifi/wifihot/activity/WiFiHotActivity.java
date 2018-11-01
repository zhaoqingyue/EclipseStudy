package com.zhaoqy.app.demo.wifi.wifihot.activity;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.wifi.wifihot.adapter.WifiAdapter;
import com.zhaoqy.app.demo.wifi.wifihot.dialog.WifiConnDialog;
import com.zhaoqy.app.demo.wifi.wifihot.dialog.WifiStatusDialog;
import com.zhaoqy.app.demo.wifi.wifihot.interfaces.OnNetworkChangeListener;
import com.zhaoqy.app.demo.wifi.wifihot.util.WifiAdmin;
import com.zhaoqy.app.demo.wifi.wifihot.view.WifiListView;
import com.zhaoqy.app.demo.wifi.wifihot.view.WifiListView.OnRefreshListener;

@SuppressLint("DefaultLocale")
public class WiFiHotActivity extends Activity implements OnClickListener, OnCheckedChangeListener, OnRefreshListener, OnItemClickListener
{
	private static final int REFRESH_CONN = 100;
	private static final int REQ_SET_WIFI = 200;
	private List<ScanResult> mList = new ArrayList<ScanResult>();
	private Context      mContext;
	private ImageView    mBack;
	private TextView     mTitle;
	private ToggleButton mToggle;
	private WifiListView mListView;
	private WifiAdapter  mAdapter;
	private WifiAdmin    mWifiAdmin; 
	private boolean      mUpdate = true;

	protected void onCreate(android.os.Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_hot);
		mContext = this;

		initView();
		initData();
		setListener();
		refreshWifiStatusOnTime();
	}

	private void initView() 
	{
		mBack = (ImageView) findViewById(R.id.id_title_left_img);
		mTitle = (TextView) findViewById(R.id.id_title_text);
		mToggle = (ToggleButton) findViewById(R.id.id_title_wifi_switch);
		mListView = (WifiListView) findViewById(R.id.freelook_listview);
	}

	private void setListener() 
	{
		mBack.setOnClickListener(this);
		mToggle.setOnCheckedChangeListener(this);
		mListView.setonRefreshListener(this);
		mListView.setOnItemClickListener(this);
	}
	
	private void initData() 
	{
		mBack.setVisibility(View.VISIBLE);
		mTitle.setText(R.string.wifi_hot);
		mToggle.setVisibility(View.VISIBLE);
		
		mWifiAdmin = new WifiAdmin(mContext);
		getWifiListInfo();
		mAdapter = new WifiAdapter(mContext, mList);
		mListView.setAdapter(mAdapter);
		int wifiState = mWifiAdmin.checkState();
		if (wifiState == WifiManager.WIFI_STATE_DISABLED || wifiState == WifiManager.WIFI_STATE_DISABLING || wifiState == WifiManager.WIFI_STATE_UNKNOWN) 
		{
			mToggle.setChecked(false);
		}
		else 
		{
			mToggle.setChecked(true);
		}
	}

	private void getWifiListInfo() 
	{
		mWifiAdmin.startScan();
		mList = mWifiAdmin.getWifiList();
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case REFRESH_CONN:
			{
				getWifiListInfo();
				mAdapter.setDatas(mList);
				mAdapter.notifyDataSetChanged();
				break;
			}
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void refreshWifiStatusOnTime() 
	{
		new Thread() 
		{
			public void run() 
			{
				while (mUpdate) 
				{
					try 
					{
						Thread.sleep(1000);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					mHandler.sendEmptyMessage(REFRESH_CONN);
				}
			}
		}.start();
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
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
	{
		if (isChecked) 
		{
			mWifiAdmin.openWifi();
			mHandler.sendEmptyMessage(REFRESH_CONN);
		} 
		else
		{
			mWifiAdmin.closeWifi();
			mList.clear();
			mAdapter.setDatas(mList);
			mAdapter.notifyDataSetChanged();
		}
	}

	private OnNetworkChangeListener mOnNetworkChangeListener = new OnNetworkChangeListener() 
	{
		@Override
		public void onNetWorkDisConnect() 
		{
			getWifiListInfo();
			mAdapter.setDatas(mList);
			mAdapter.notifyDataSetChanged();
		}

		@Override
		public void onNetWorkConnect() 
		{
			getWifiListInfo();
			mAdapter.setDatas(mList);
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			mAdapter.notifyDataSetChanged();
		}
	};
	
	@Override
	public void onRefresh() 
	{
		new AsyncTask<Void, Void, Void>() 
		{
			protected Void doInBackground(Void... params) 
			{
				try 
				{
					Thread.sleep(1000);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				getWifiListInfo();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) 
			{
				mAdapter.setDatas(mList);
				mAdapter.notifyDataSetChanged();
				mListView.onRefreshComplete();
			}
		}.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) 
	{
		int position = pos - 1;
		ScanResult scanResult = mList.get(position);
		String desc = "";
		String descOri = scanResult.capabilities;
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
		
		if (desc.equals("")) 
		{
			isConnectSelf(scanResult);
			return;
		}
		isConnect(scanResult);
	}
	
	private void isConnect(ScanResult scanResult) 
	{
		if (mWifiAdmin.isConnect(scanResult)) 
		{
			WifiStatusDialog mStatusDialog = new WifiStatusDialog(mContext, R.style.wifi_dialog, scanResult, mOnNetworkChangeListener);
			mStatusDialog.show();
		}
		else 
		{
			WifiConnDialog mDialog = new WifiConnDialog(mContext, R.style.wifi_dialog, scanResult, mOnNetworkChangeListener);
			mDialog.show();
		}
	}
	
	@SuppressLint("ShowToast")
	private void isConnectSelf(ScanResult scanResult) 
	{
		if (mWifiAdmin.isConnect(scanResult)) 
		{
			WifiStatusDialog mStatusDialog = new WifiStatusDialog(mContext, R.style.wifi_dialog, scanResult, mOnNetworkChangeListener);
			mStatusDialog.show();
		}
		else 
		{
			boolean iswifi = mWifiAdmin.connectSpecificAP(scanResult);
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
			if (iswifi) 
			{
				Toast.makeText(mContext, mContext.getResources().getString(R.string.wifi_connect_success), 0).show();
			}
			else 
			{
				Toast.makeText(mContext, mContext.getResources().getString(R.string.wifi_connect_fail), 0).show();
			}	
		}
	}

	public void gotoSysCloseWifi() 
	{
		Intent intent = new Intent("android.settings.WIFI_SETTINGS");
		intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$WifiSettingsActivity"));
		startActivityForResult(intent, REQ_SET_WIFI);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) 
		{
		case REQ_SET_WIFI:
		{
			int wifiState = mWifiAdmin.checkState();
			if (wifiState == WifiManager.WIFI_STATE_DISABLED || wifiState == WifiManager.WIFI_STATE_DISABLING || wifiState == WifiManager.WIFI_STATE_UNKNOWN) 
			{
				mToggle.setChecked(false);
			} 
			else 
			{
				mToggle.setChecked(true);
			}
			break;
		}
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		
		mUpdate = false;
	}
}
