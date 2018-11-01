/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: CustomListView.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.menu.kaixin.view
 * @Description: 自定义ListView
 * @author: zhaoqy
 * @date: 2015-11-9 下午2:23:53
 * @version: V1.0
 */

package com.zhaoqy.app.demo.menu.kaixin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class CustomListView extends ListView 
{

	public CustomListView(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
	}

	public CustomListView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}

	public CustomListView(Context context) 
	{
		super(context);
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
