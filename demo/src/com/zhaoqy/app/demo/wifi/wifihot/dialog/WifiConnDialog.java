package com.zhaoqy.app.demo.wifi.wifihot.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.wifi.wifihot.interfaces.OnNetworkChangeListener;
import com.zhaoqy.app.demo.wifi.wifihot.util.WifiAdmin;
import com.zhaoqy.app.demo.wifi.wifihot.util.WifiConnect.WifiCipherType;

@SuppressLint("DefaultLocale")
public class WifiConnDialog extends Dialog 
{
	private ScanResult scanResult;
	private Context  mContext;
	private TextView mName;
	private TextView mStrength;
	private TextView mSecurity;
	private EditText mPassword;
	private CheckBox mCheckBox;
	private TextView mConnect;
	private TextView mCancel;
	private String   mWifiName;
	private String   mSecurigyLevel;
	private int      mLevel;

	public WifiConnDialog(Context context, int theme) 
	{
		super(context, theme);
	}

	private WifiConnDialog(Context context, int theme, String wifiName, int singlStren, String securityLevl) 
	{
		super(context, theme);
		mContext = context;
		mWifiName = wifiName;
		mLevel = singlStren;
		mSecurigyLevel = securityLevl;
	}

	public WifiConnDialog(Context context, int theme, ScanResult scanResult, OnNetworkChangeListener onNetworkChangeListener) 
	{
		this(context, theme, scanResult.SSID, scanResult.level, scanResult.capabilities);
		this.scanResult = scanResult;
		this.onNetworkChangeListener = onNetworkChangeListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_wifi_conn);
		setCanceledOnTouchOutside(false);

		initView();
		setListener();
	}
	
	private void initView() 
	{
		mName = (TextView) findViewById(R.id.id_dialog_wifi_conn_name);
		mStrength = (TextView) findViewById(R.id.id_dialog_wifi_conn_signal_strength);
		mSecurity = (TextView) findViewById(R.id.id_dialog_wifi_conn_security_level);
		mPassword = (EditText) findViewById(R.id.id_dialog_wifi_conn_edt_password);
		mCheckBox = (CheckBox) findViewById(R.id.id_dialog_wifi_conn_show_pass);
		mCancel = (TextView) findViewById(R.id.id_dialog_wifi_conn_cancel);
		mConnect = (TextView) findViewById(R.id.id_dialog_wifi_conn_connect);

		mName.setText(mWifiName);
		mStrength.setText(WifiAdmin.singlLevToStr(mLevel));
		mSecurity.setText(mSecurigyLevel);
		mConnect.setEnabled(false);
		mCheckBox.setEnabled(false);
	}

	@SuppressLint("DefaultLocale")
	private void setListener() 
	{
		mPassword.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				if (TextUtils.isEmpty(s)) 
				{
					mConnect.setEnabled(false);
					mCheckBox.setEnabled(false);
				} 
				else 
				{
					mConnect.setEnabled(true);
					mCheckBox.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) 
			{
			}

			@Override
			public void afterTextChanged(Editable s) 
			{
			}
		});

		mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if (isChecked) 
				{
					mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					Editable etable = mPassword.getText();
					Selection.setSelection(etable, etable.length());
				} 
				else 
				{
					mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					Editable etable = mPassword.getText();
					Selection.setSelection(etable, etable.length());
				}
			}
		});

		mCancel.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dismiss();
			}
		});

		mConnect.setOnClickListener(new View.OnClickListener() 
		{
			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View v) 
			{
				WifiCipherType type = null;
				if (scanResult.capabilities.toUpperCase().contains("WPA")) 
				{
					type = WifiCipherType.WIFICIPHER_WPA;
				}
				else if (scanResult.capabilities.toUpperCase().contains("WEP")) 
				{
					type = WifiCipherType.WIFICIPHER_WEP;
				} 
				else 
				{
					type = WifiCipherType.WIFICIPHER_NOPASS;
				}

				WifiAdmin mWifiAdmin = new WifiAdmin(mContext);
				boolean bRet = mWifiAdmin.connect(scanResult.SSID, mPassword.getText().toString().trim(), type);
				if (bRet) 
				{
					showShortToast(mContext.getResources().getString(R.string.wifi_connect_success));
					onNetworkChangeListener.onNetWorkConnect();
				} 
				else 
				{
					showShortToast(mContext.getResources().getString(R.string.wifi_connect_fail));
					onNetworkChangeListener.onNetWorkConnect();
				}
				WifiConnDialog.this.dismiss();
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

	private void showShortToast(String text) 
	{
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();	
	}

	private OnNetworkChangeListener onNetworkChangeListener;
}
