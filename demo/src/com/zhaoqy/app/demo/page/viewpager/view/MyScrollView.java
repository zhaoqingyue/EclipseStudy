package com.zhaoqy.app.demo.page.viewpager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView 
{
	public MyScrollView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) 
	{
		return false;
	}
}
