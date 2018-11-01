package com.zhaoqy.app.demo.wifi.wifihot.util;

import java.util.List;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class WifiConnect 
{
	WifiManager mWifiManager;

	public enum WifiCipherType 
	{
		WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
	}

	public WifiConnect(WifiManager wifiManager) 
	{
		this.mWifiManager = wifiManager;
	}

	private boolean openWifi() 
	{
		boolean bRet = true;
		if (!mWifiManager.isWifiEnabled()) 
		{
			bRet = mWifiManager.setWifiEnabled(true);
		}
		return bRet;
	}

	public boolean connect(String SSID, String Password, WifiCipherType Type) 
	{
		if (!openWifi()) 
		{
			return false;
		}
	
		while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) 
		{
			try 
			{
				Thread.currentThread();
				Thread.sleep(100);
			} 
			catch (InterruptedException ie) 
			{
			}
		}

		WifiConfiguration wifiConfig = createWifiInfo(SSID, Password, Type);
		if (wifiConfig == null) 
		{
			return false;
		}

		WifiConfiguration tempConfig = this.isExsits(SSID);
		if (tempConfig != null) 
		{
			mWifiManager.removeNetwork(tempConfig.networkId);
		}

		int netID = mWifiManager.addNetwork(wifiConfig);
		boolean bRet = mWifiManager.enableNetwork(netID, false);
		return bRet;
	}

	private WifiConfiguration isExsits(String SSID) 
	{
		List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) 
		{
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) 
			{
				return existingConfig;
			}
		}
		return null;
	}

	private WifiConfiguration createWifiInfo(String SSID, String Password, WifiCipherType Type) 
	{
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		if (Type == WifiCipherType.WIFICIPHER_NOPASS) 
		{
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == WifiCipherType.WIFICIPHER_WEP) 
		{
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == WifiCipherType.WIFICIPHER_WPA) 
		{
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.status = WifiConfiguration.Status.ENABLED;
		} 
		else 
		{
			return null;
		}
		return config;
	}
}
