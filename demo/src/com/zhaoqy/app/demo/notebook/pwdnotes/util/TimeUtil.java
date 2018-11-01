package com.zhaoqy.app.demo.notebook.pwdnotes.util;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class TimeUtil 
{
	private long time;

	public TimeUtil(long time)
	{
		this.time = time;
	}

	public String getDescription()
	{
		long currentTime = System.currentTimeMillis();
		System.out.println(System.currentTimeMillis());
		Date date = new Date(time);
		DateFormat format = new SimpleDateFormat("HH:mm");
		String endTime = format.format(date);

		int a = 0;
		try
		{
			a = isYeaterday(new Date(time), new Date(currentTime));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		if (a == 0)
		{
			return "昨天 "+endTime;
		}
		else if (a == -1)
		{
			return "今天 "+endTime;
		}
		else if (a == 1)
		{
			return "前天 "+endTime;
		}
		else
		{
			DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			return format2.format(date);
		}
	}
	
	private int isYeaterday(Date oldTime, Date newTime) throws ParseException
	{
		if (newTime == null)
		{
			newTime = new Date();
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String todayStr = format.format(newTime);
		Date today = format.parse(todayStr);

		if ((today.getTime() - oldTime.getTime()) > 0 && (today.getTime() - oldTime.getTime()) <= 86400000)
		{
			return 0;
		}
		else if ((today.getTime() - oldTime.getTime()) <= 0)
		{ 
			return -1;
		}
		else if ((today.getTime() - oldTime.getTime()) >= 86400000 && (today.getTime() - oldTime.getTime()) <= 2 * 86400000)
		{ 
			return 1;
		}
		else
		{
			return 2;
		}
	}
}
