package com.zhaoqy.app.demo.wifi.wifihot.util;

import java.net.Inet4Address;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import com.zhaoqy.app.demo.wifi.wifihot.util.WifiConnect.WifiCipherType;

public class WifiAdmin 
{
	private List<WifiConfiguration> mWifiConfigurations;
	private WifiManager      mWifiManager;
	private WifiInfo         mWifiInfo;
	private List<ScanResult> mWifiList;
	private WifiLock         mWifiLock;

	public WifiAdmin(Context context) 
	{
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	public boolean closeWifi() 
	{
		if (mWifiManager.isWifiEnabled()) 
		{
		    return mWifiManager.setWifiEnabled(false);
		}
		return false;
	}
	
	public int checkState() 
	{
		return mWifiManager.getWifiState();
	}

	public void acquireWifiLock() 
	{
		mWifiLock.acquire();
	}

	public void releaseWifiLock() 
	{
		if (mWifiLock.isHeld()) 
		{
			mWifiLock.acquire();
		}
	}

	public void createWifiLock() 
	{
		mWifiLock = mWifiManager.createWifiLock("test");
	}

	public List<WifiConfiguration> getConfiguration() 
	{
		return mWifiConfigurations;
	}

	public void connetionConfiguration(int index) 
	{
		if (index > mWifiConfigurations.size()) 
		{
			return;
		}
		mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);
	}

	public void startScan() 
	{
		mWifiManager.startScan();
		mWifiList = mWifiManager.getScanResults();
		mWifiConfigurations = mWifiManager.getConfiguredNetworks();
	}

	public List<ScanResult> getWifiList() 
	{
		return mWifiList;
	}

	@SuppressLint("UseValueOf")
	public StringBuffer lookUpScan() 
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mWifiList.size(); i++) 
		{
			sb.append("Index_" + new Integer(i + 1).toString() + ":");
			sb.append((mWifiList.get(i)).toString()).append("\n");
		}
		return sb;
	}

	public String getMacAddress() 
	{
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	public String getBSSID() 
	{
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	public int getIpAddress() 
	{
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	public int getNetWordId() 
	{
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	public String getWifiInfo() 
	{
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	public void addNetWork(WifiConfiguration configuration) 
	{
		int wcgId = mWifiManager.addNetwork(configuration);
		mWifiManager.enableNetwork(wcgId, true);
	}

	public void disConnectionWifi(int netId) 
	{
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}

	public boolean openWifi() 
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
		WifiConfiguration tempConfig = isExsits(SSID);
		if (tempConfig != null) 
		{
			mWifiManager.removeNetwork(tempConfig.networkId);
		}
		int netID = mWifiManager.addNetwork(wifiConfig);
		mWifiManager.disconnect();
		boolean bRet = mWifiManager.enableNetwork(netID, true);
		mWifiManager.reconnect();
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
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		}
		else 
		{
			return null;
		}
		return config;
	}

	public boolean isConnect(ScanResult result) 
	{
		if (result == null) 
		{
			return false;
		}
		mWifiInfo = mWifiManager.getConnectionInfo();
		String g2 = "\"" + result.SSID + "\"";
		if (mWifiInfo.getSSID() != null && mWifiInfo.getSSID().endsWith(g2)) 
		{
			return true;
		}
		return false;
	}

	public String ipIntToString(int ip) 
	{
		try 
		{
			byte[] bytes = new byte[4];
			bytes[0] = (byte) (0xff & ip);
			bytes[1] = (byte) ((0xff00 & ip) >> 8);
			bytes[2] = (byte) ((0xff0000 & ip) >> 16);
			bytes[3] = (byte) ((0xff000000 & ip) >> 24);
			return Inet4Address.getByAddress(bytes).getHostAddress();
		} 
		catch (Exception e) 
		{
			return "";
		}
	}

	public int getConnNetId() 
	{
		mWifiInfo = mWifiManager.getConnectionInfo();
		return mWifiInfo.getNetworkId();
	}

	public static String singlLevToStr(int level) 
	{
		String resuString = "无信号";
		if (Math.abs(level) > 100) 
		{
			resuString = "无信号";
		} 
		else if (Math.abs(level) > 80) 
		{
			resuString = "弱";
		} 
		else if (Math.abs(level) > 70) 
		{
			resuString = "强";
		} 
		else if (Math.abs(level) > 60) 
		{
			resuString = "强";
		} 
		else if (Math.abs(level) > 50) 
		{
			resuString = "较强";
		} 
		else 
		{
			resuString = "极强";
		}
		return resuString;
	}

	public boolean addNetwork(WifiConfiguration wcg) 
	{
		if (wcg == null) 
		{
			return false;
		}
		int wcgID = mWifiManager.addNetwork(wcg);
		boolean b = mWifiManager.enableNetwork(wcgID, true);
		mWifiManager.saveConfiguration();
		return b;
	}
	
	public boolean connectSpecificAP(ScanResult scan) 
	{
		List<WifiConfiguration> list = mWifiManager.getConfiguredNetworks();
		boolean networkInSupplicant = false;
		boolean connectResult = false;
		mWifiManager.disconnect();
		for (WifiConfiguration w : list) 
		{
			if (w.BSSID != null && w.BSSID.equals(scan.BSSID)) 
			{
				connectResult = mWifiManager.enableNetwork(w.networkId, true);
				networkInSupplicant = true;
				break;
			}
		}
		if (!networkInSupplicant) 
		{
			WifiConfiguration config = CreateWifiInfo(scan, "");
			connectResult = addNetwork(config);
		}
		return connectResult;
	}

	public WifiConfiguration CreateWifiInfo(ScanResult scan, String Password) 
	{
		WifiConfiguration config = new WifiConfiguration();
		config.hiddenSSID = false;
		config.status = WifiConfiguration.Status.ENABLED;
		if (scan.capabilities.contains("WEP")) 
		{
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.SSID = "\"" + scan.SSID + "\"";
			config.wepTxKeyIndex = 0;
			config.wepKeys[0] = Password;
		} 
		else if (scan.capabilities.contains("PSK")) 
		{
			config.SSID = "\"" + scan.SSID + "\"";
			config.preSharedKey = "\"" + Password + "\"";
		} 
		else if (scan.capabilities.contains("EAP")) 
		{
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.SSID = "\"" + scan.SSID + "\"";
			config.preSharedKey = "\"" + Password + "\"";
		} 
		else 
		{
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.SSID = "\"" + scan.SSID + "\"";
			config.preSharedKey = null;
		}
		return config;
	}
}
