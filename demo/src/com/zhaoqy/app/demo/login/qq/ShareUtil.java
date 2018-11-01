package com.zhaoqy.app.demo.login.qq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class ShareUtil 
{
	/*
	1. qq号码
	2. qq密码
	3. 呢称
	4. 邮箱
	5. 头像
	6. 是否记住密码(默认true)
	7. 是否隐身登陆(默认false)
	8. 是否静音登陆(默认false)
	9. 是否允许手机/电脑同时在线(默认true)
	10. 是否接收群消息(默认true)
	11. IP
	12. 端口
	13. 是否在后台运行(默认false)
	14. 是否第一次运行本应用(默认true)
	*/
	
	public static final String SERVER_IP = "192.168.1.110"; //服务器ip
	public static final int SERVER_PORT = 8080;             //服务器端口
	public static final String SAVE_USER = "saveUser";      //保存用户信息的xml文件名
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	@SuppressLint("CommitPrefEdits")
	@SuppressWarnings("static-access")
	public ShareUtil(Context context, String file) 
	{
		sp = context.getSharedPreferences(file, context.MODE_PRIVATE);
		editor = sp.edit();
	}
	
	//用户的QQ号
	public void setNumber(String number) 
	{
		editor.putString("number", number);
		editor.commit();
	}

	public String getNumber() 
	{
		return sp.getString("number", "");
	}

	//用户的密码
	public void setPasswd(String passwd) 
	{
		editor.putString("passwd", passwd);
		editor.commit();
	}

	public String getPasswd() 
	{
		return sp.getString("passwd", "");
	}

	//用户的昵称
	public String getName() 
	{
		return sp.getString("name", "");
	}

	public void setName(String name) 
	{
		editor.putString("name", name);
		editor.commit();
	}

	//用户的邮箱
	public String getEmail() 
	{
		return sp.getString("email", "");
	}

	public void setEmail(String email) 
	{
		editor.putString("email", email);
		editor.commit();
	}

	//用户自己的头像
	public Integer getImg() 
	{
		return sp.getInt("img", 0);
	}

	public void setImg(int i) 
	{
		editor.putInt("img", i);
		editor.commit();
	}
	
	//是否记住密码
	public void setIsRememberPassword(boolean rememberpassword)
	{
		editor.putBoolean("rememberpassword", rememberpassword);
		editor.commit();
	}
	
	public boolean getIsRememberPassword()
	{
		return sp.getBoolean("rememberpassword", true);
	}
	
	//是否隐身登陆
	public void setIsHideLogin(boolean hidelogin)
	{
		editor.putBoolean("hidelogin", hidelogin);
		editor.commit();
	}
	
	public boolean getIsHideLogin()
	{
		return sp.getBoolean("hidelogin", false);
	}
	
	//是否静音登陆
	public void setIsSilenceLogin(boolean silencelogin)
	{
		editor.putBoolean("silencelogin", silencelogin);
		editor.commit();
	}
	
	public boolean getIsSilenceLogin()
	{
		return sp.getBoolean("silencelogin", false);
	}
	
	//是否允许是哦及和电脑同时在线
	public void setIsOnlineTogether(boolean onlinetogether)
	{
		editor.putBoolean("onlinetogether", onlinetogether);
		editor.commit();
	}
	
	public boolean getIsOnlineTogether()
	{
		return sp.getBoolean("onlinetogether", true);
	}	
	
	//是否接收群消息
	public void setIsReceiveGroupMessages(boolean receivegroupmessages)
	{
		editor.putBoolean("receivegroupmessages", receivegroupmessages);
		editor.commit();
	}
	
	public boolean getIsReceiveGroupMessages()
	{
		return sp.getBoolean("receivegroupmessages", true);
	}	
	
	//ip
	public void setIp(String ip) 
	{
		editor.putString("ip", ip);
		editor.commit();
	}

	public String getIp() 
	{
		return sp.getString("ip", SERVER_IP);
	}

	//端口
	public void setPort(int port) 
	{
		editor.putInt("port", port);
		editor.commit();
	}

	public int getPort() 
	{
		return sp.getInt("port", SERVER_PORT);
	}	

	//是否在后台运行标记
	public void setIsRunBackground(boolean runbackground) 
	{
		editor.putBoolean("runbackground", runbackground);
		editor.commit();
	}

	public boolean getIsRunBackground() 
	{
		return sp.getBoolean("runbackground", false);
	}

	//是否第一次运行本应用
	public void setIsFirstRun(boolean firstrun) 
	{
		editor.putBoolean("firstrun", firstrun);
		editor.commit();
	}

	public boolean getisFirstRun() 
	{
		return sp.getBoolean("firstrun", true);
	}
}
