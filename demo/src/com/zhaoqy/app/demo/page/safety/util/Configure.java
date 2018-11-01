/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: Configure.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.util
 * @Description: 安全卫士配置文件
 * @author: zhaoqy
 * @date: 2015-12-10 下午4:16:15
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.util;

import android.app.Activity;
import android.util.DisplayMetrics;

public class Configure 
{
	public static boolean isMove=false;
	public static boolean isChangingPage=false;
	public static boolean isDelDark = false;
	public static int curentPage=0;
	public static int countPages=0;
	public static int removeItem=0;
	public static int screenHeight=0;
	public static int screenWidth=0;
	public static float screenDensity=0;
	
	public static void init(Activity context) 
	{
		if(screenDensity==0||screenWidth==0||screenHeight==0)
		{
			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			Configure.screenDensity = dm.density;
			Configure.screenHeight = dm.heightPixels;
			Configure.screenWidth = dm.widthPixels;
		}
	}
}
