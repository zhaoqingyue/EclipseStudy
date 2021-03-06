package com.zhaoqy.app.demo;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;

public class Common 
{
	//SDK号
	@SuppressWarnings("deprecation")
	public static String getSDK() 
	{
		return android.os.Build.VERSION.SDK; 
	}
	
	//手机型号
	public static String getModel() 
	{
		return android.os.Build.MODEL;
	}
	
	//android系统版本号
	public static String getRelease() 
	{
		return android.os.Build.VERSION.RELEASE;
	}
	
	//获取手机身份证
	public static String getImei(Context context) 
	{
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	//获取版本名字
	public static String getVerName(Context context) 
	{ 
		try 
		{
			String pkName = context.getPackageName();
			String versionName = context.getPackageManager().getPackageInfo(pkName, 0).versionName;
			return versionName;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	//获取版本号
	public static int getVerCode(Context context) 
	{ 
		String pkName = context.getPackageName();
		try 
		{
			int versionCode = context.getPackageManager().getPackageInfo(pkName, 0).versionCode;
			return versionCode;
		} 
		catch (NameNotFoundException e) 
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	//判断网络连接是否可用
	public static boolean isNetworkAvailable(Context context) 
	{ 
		//获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null)
			return false;
		//获取网络连接管理的对象
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info == null || !info.isConnected())
			return false;
		//判断当前网络是否已经连接
		return (info.getState() == NetworkInfo.State.CONNECTED);
	}
	
	//字符串超出给定文字则截取
	public static String trim(String str, int limit) 
	{ 
		String mStr = str.trim();
		return mStr.length() > limit ? mStr.substring(0, limit) : mStr;
	}

	//获取手机号码，很多手机获取不到
	public static String getTel(Context context) 
	{ 
		TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telManager.getLine1Number();
	}

	//获取时机mac地址
	public static String getMac(Context context) 
	{ 
		final WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifi != null) 
		{
			WifiInfo info = wifi.getConnectionInfo();
			if (info.getMacAddress() != null) 
			{
				return info.getMacAddress().toLowerCase(Locale.ENGLISH).replace(":", "");
			}
			return "";
		}
		return "";
	}

	//将像素转化成dp
	public static int px2dip(Context context, int pxValue) 
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	//将像素转化成sp
	public static int px2sp(Context context, int px) 
	{
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (px / scaledDensity);
	}
	
	//将dp转化成像素
	public static int dip2px(Context context, float dipValue) 
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
	
	/**
	 * 传入一个view,返回一个int数组来存放 view在手机屏幕上左上角的绝对坐标
	 * @param views 传入的view
	 * @return 返回int型数组, location[0]表示x, location[1]表示y
	 */
	public static int[] getViewPosition(View view) 
	{
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		return location;
	}
	
	/**
	 * 将传进的图片存储在手机上,并返回存储路径
	 * @param photo Bitmap 类型,传进的图片
	 * @return String 返回存储路径
	 */
	public static String savePic(Bitmap photo, String name, String path) 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); //创建一个outputstream来读取文件流
		photo.compress(Bitmap.CompressFormat.PNG, 100, baos);     //把 bitmap 的图片转换成jpge的格式放入输出流中
		byte[] byteArray = baos.toByteArray();
		String saveDir = Environment.getExternalStorageDirectory().getAbsolutePath();
		File dir = new File(saveDir + "/" + path); //定义一个路径
		if (!dir.exists()) //如果路径不存在,创建路径
		{ 
			dir.mkdir();
		}
		File file = new File(saveDir, name + ".png"); //定义一个文件
		if (file.exists())
			file.delete(); //删除原来有此名字文件,删除
		try 
		{
			file.createNewFile();
			FileOutputStream fos;
			fos = new FileOutputStream(file); //通过 FileOutputStream 关联文件
			BufferedOutputStream bos = new BufferedOutputStream(fos); //通过 bos 往文件里写东西												 
			bos.write(byteArray);
			bos.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return file.getPath();
	}
	
	//回收 bitmap 减小系统占用的资源消耗
	public static void destoryBimap(Bitmap photo) 
	{
		if (photo != null && !photo.isRecycled()) 
		{
			photo.recycle();
			photo = null;
		}
	}
	
	//将输入字符串做md5编码
	public static String md5(String s) 
	{
		try 
		{
			//Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes("UTF-8"));
			byte messageDigest[] = digest.digest();

			//Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (byte b : messageDigest) 
			{
				if ((b & 0xFF) < 0x10)
					hexString.append("0");
				hexString.append(Integer.toHexString(b & 0xFF));
			}
			return hexString.toString();
		} 
		catch (NoSuchAlgorithmException e) 
		{
			return "";
		} 
		catch (UnsupportedEncodingException e) 
		{
			return "";
		}
	}
	
	//是否是数字
	public static boolean isNumber(char c) 
	{
		boolean isNumer = false;
		if (c >= '0' && c <= '9') 
		{
			isNumer = true;
		}
		return isNumer;
	}

	//是否是正确的邮箱地址
	public static boolean isEmail(String strEmail) 
	{ 
		String checkemail = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern pattern = Pattern.compile(checkemail);
		Matcher matcher = pattern.matcher(strEmail);
		return matcher.matches();
	}

	//字符串是否为空
	public static boolean isNull(String string) 
	{
		if (null == string || "".equals(string)) 
		{
			return false;
		}
		return true;
	}

	//字符串长度检测
	public static boolean isLenghtOk(String string, int max, int min) 
	{
		if (null != string) 
		{
			if (string.length() > max || string.length() < min) 
			{
				return false;
			}
		}
		return true;
	}

	//字符串长度是否
	public static boolean isLenghtOk(String string, int max) 
	{
		if (null != string) 
		{
			if (string.length() > max) 
			{
				return false;
			}
		}
		return true;
	}
}
