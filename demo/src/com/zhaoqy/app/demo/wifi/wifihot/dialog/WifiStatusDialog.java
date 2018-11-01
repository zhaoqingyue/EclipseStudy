package com.zhaoqy.app.demo.wifi.wifihot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.TextView;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.wifi.wifihot.interfaces.OnNetworkChangeListener;
import com.zhaoqy.app.demo.wifi.wifihot.util.WifiAdmin;

public class WifiStatusDialog extends Dialog 
{
	private OnNetworkChangeListener mOnNetworkChangeListener;
	private WifiAdmin mWifiAdmin;
	private TextView mName;
	private TextView mConnStatus;
	private TextView mSinglStrength;
	private TextView mSecurityLevel;
	private TextView mIpAddress;
	private TextView mDisconnect;
	private TextView mCancel;
	private String   mWifiName;
	private String   mSecurigyLevel;
	private int      mLevel;

	public WifiStatusDialog(Context context, int theme) 
	{
		super(context, theme);
		this.mWifiAdmin = new WifiAdmin(context);
	}

	private WifiStatusDialog(Context context, int theme, String wifiName, int singlStren, String securityLevl) 
	{
		super(context, theme);
		mWifiName = wifiName;
		mLevel = singlStren;
		mSecurigyLevel = securityLevl;
		mWifiAdmin = new WifiAdmin(context);
	}

	public WifiStatusDialog(Context context, int theme, ScanResult scanResult, OnNetworkChangeListener onNetworkChangeListener) 
	{
		this(context, theme, scanResult.SSID, scanResult.level, scanResult.capabilities);
		mWifiAdmin = new WifiAdmin(context);
		mOnNetworkChangeListener = onNetworkChangeListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_wifi_status);
		setCanceledOnTouchOutside(false);

		initView();
		setListener();
	}

	private void initView() 
	{
		mName = (TextView) findViewById(R.id.id_dialog_wifi_status_name);
		mConnStatus = (TextView) findViewById(R.id.id_dialog_wifi_status_conn_status);
		mSinglStrength = (TextView) findViewById(R.id.id_dialog_wifi_status_signal_strength);
		mSecurityLevel = (TextView) findViewById(R.id.id_dialog_wifi_status_security_level);
		mIpAddress = (TextView) findViewById(R.id.id_dialog_wifi_status_ip_address);
		mCancel = (TextView) findViewById(R.id.id_dialog_wifi_status_cancel);
		mDisconnect = (TextView) findViewById(R.id.id_dialog_wifi_status_disconnect);

		mName.setText(mWifiName);
		mConnStatus.setText(R.string.wifi_connected);
		mSinglStrength.setText(WifiAdmin.singlLevToStr(mLevel));
		mSecurityLevel.setText(mSecurigyLevel);
		mIpAddress.setText(mWifiAdmin.ipIntToString(mWifiAdmin.getIpAddress()));
	}
	
	private void setListener() 
	{
		mCancel.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dismiss();
			}
		});

		mDisconnect.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				int netId = mWifiAdmin.getConnNetId();
				mWifiAdmin.disConnectionWifi(netId);
				WifiStatusDialog.this.dismiss();
				mOnNetworkChangeListener.onNetWorkDisConnect();
			}
		});
	}

	@Override
	public void show() 
	{
		super.show();
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Point size = new Point();
		wm.getDefaultDisplay().getSize(size);
		getWindow().setLayout((int) (size.x * 9 / 10), LayoutParams.WRAP_CONTENT);
	}
}
