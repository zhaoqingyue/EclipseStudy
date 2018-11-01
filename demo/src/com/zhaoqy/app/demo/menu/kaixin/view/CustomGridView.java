/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: CustomGridView.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.view
 * @Description: 自定义GridView
 * @author: zhaoqy
 * @date: 2015-11-10 下午4:18:15
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CustomGridView extends GridView 
{
	public CustomGridView(Context context) 
	{
		super(context);
	}

	public CustomGridView(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
	}

	public CustomGridView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}

	/**
	 * 设置不滚动
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	{
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
