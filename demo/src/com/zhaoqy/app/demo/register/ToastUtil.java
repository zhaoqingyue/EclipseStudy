package com.zhaoqy.app.demo.register;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil 
{
	public static void showLongToast(Context context, String pMsg) 
	{
		Toast.makeText(context, pMsg, Toast.LENGTH_LONG).show();
	}

	public static void showShortToast(Context context, String pMsg) 
	{
		Toast.makeText(context, pMsg, Toast.LENGTH_SHORT).show();
	}
}
