/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: Common.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.safety.util
 * @Description: 安全卫士Common
 * @author: zhaoqy
 * @date: 2015-12-10 下午3:55:44
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.safety.util;

import java.util.ArrayList;
import java.util.List;
import com.zhaoqy.app.demo.R;
import com.zhaoqy.app.demo.page.safety.item.SafetyItem;

public class Common 
{
	public static List<SafetyItem> mList1;
	public static List<SafetyItem> mList2;
	
	public static int[] mImageId1 = { 
		R.drawable.safety_disk_sysclear_alert,     //手机清理
		R.drawable.safety_disk_phone_block,        //骚扰拦截
		R.drawable.safety_disk_phone_exam_default, //手机体检
		R.drawable.safety_disk_privacy_protection, //隐私空间
		R.drawable.safety_disk_anti_scan,          //手机杀毒
		R.drawable.safety_disk_power_manager,      //节电管理
		R.drawable.safety_disk_program_manager,    //手机管家
		R.drawable.safety_disk_active_defense,     //隐私行为监控
	};
	
	public static int[] mImageId2 = { 
		R.drawable.safety_disk_flow_traffic,    //流量监控
		R.drawable.safety_disk_secure_bak,      //通讯录备份
		R.drawable.safety_disk_phone_anti_open, //手机防盗
		R.drawable.safety_disk_app_market,      //安全市场
		R.drawable.safety_disk_advertise_block, //恶意广告拦截
		R.drawable.safety_disk_qrcode,          //安全二维码
		R.drawable.safety_disk_use_tools,       //应用工具
	};
	
	public static String[] mItemName1 = {
		"手机清理", 
		"骚扰拦截", 
		"手机体检", 
		"隐私空间",
		"手机杀毒", 
		"节电管理", 
		"手机管家", 
		"隐私行为监控", 
	};
	
	public static String[] mItemName2 = { 
		"流量监控", 
		"通讯录备份", 
		"手机防盗", 
		"安全市场",
		"恶意广告拦截", 
		"安全二维码", 
		"应用工具" 
	};

	public static List<SafetyItem> getDiskList1() 
	{
		mList1 = new ArrayList<SafetyItem>();
		for (int i=0; i<mItemName1.length; i++)
		{
			SafetyItem bean = new SafetyItem();
			bean.setName(mItemName1[i]);
			bean.setId(mImageId1[i]);
			mList1.add(bean);
		}
		return mList1;
	}

	public static List<SafetyItem> getDiskList2() 
	{
		mList2 = new ArrayList<SafetyItem>();
		for (int i=0; i<mItemName2.length; i++) 
		{
			SafetyItem bean = new SafetyItem();
			bean.setName(mItemName2[i]);
			bean.setId(mImageId2[i]);
			mList2.add(bean);
		}
		return mList2;
	}
}
