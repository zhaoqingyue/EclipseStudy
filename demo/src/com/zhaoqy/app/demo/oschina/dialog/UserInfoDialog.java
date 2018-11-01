/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: UserInfoDialog.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.dialog
 * @Description: 用户信息对话框
 * @author: zhaoqy
 * @date: 2015-11-25 上午9:33:31
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.dialog;

import com.zhaoqy.app.demo.R;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;

public class UserInfoDialog extends Dialog 
{
	private LayoutParams lp;

	public UserInfoDialog(Context context) 
	{
		super(context, R.style.oschina_dialog);
		setContentView(R.layout.dialog_oschina_userinfo);

		//设置点击对话框之外能消失
		setCanceledOnTouchOutside(true);
		//设置window属性
		lp = getWindow().getAttributes();
		lp.gravity = Gravity.TOP;
		lp.dimAmount = 0; // 去背景遮盖
		lp.alpha = 1.0f;
		lp.y = 55;
		getWindow().setAttributes(lp);
	}
}
