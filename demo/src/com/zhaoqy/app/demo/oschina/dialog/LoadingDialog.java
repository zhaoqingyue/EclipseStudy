/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: LoadingDialog.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.oschina.dialog
 * @Description: 加载对话框控件
 * @author: zhaoqy
 * @date: 2015-11-19 上午10:55:34
 * @version: V1.0
 */

package com.zhaoqy.app.demo.oschina.dialog;

import com.zhaoqy.app.demo.R;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class LoadingDialog extends Dialog 
{
	private Context        mContext;
	private LayoutInflater mInflater;
	private LayoutParams   mLayoutParams;
	private TextView       mToadtext;

	public LoadingDialog(Context context) 
	{
		super(context, R.style.oschina_dialog);
		mContext = context;
		
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = mInflater.inflate(R.layout.dialog_oschina_loading, null);
		mToadtext = (TextView) layout.findViewById(R.id.loading_text);
		setContentView(layout);
		
		//设置window属性
		mLayoutParams = getWindow().getAttributes();
		mLayoutParams.gravity = Gravity.CENTER;
		//去背景遮盖
		mLayoutParams.dimAmount = 0; 
		mLayoutParams.alpha = 1.0f;
		getWindow().setAttributes(mLayoutParams);

	}

	public void setLoadText(String content)
	{
		mToadtext.setText(content);
	}
}
