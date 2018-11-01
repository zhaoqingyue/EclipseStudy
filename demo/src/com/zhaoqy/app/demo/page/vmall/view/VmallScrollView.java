/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: VmallScrollView.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.vmall.view
 * @Description: 自定义滚动条(ScrollView中不能嵌套具有滑动特性的View，系统无法判断该哪个控件获取滚动焦点，会导致GridView无法滑动)
 * @author: zhaoqy
 * @date: 2015-12-18 上午9:51:34
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.vmall.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class VmallScrollView extends ScrollView 
{
	private float xDistance, yDistance, xLast, yLast;
	
    public VmallScrollView(Context context) 
    {
        super(context);
    }
    
    public VmallScrollView(Context context,AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public VmallScrollView(Context context, AttributeSet attrs, int defStyle) 
    {
        super(context, attrs, defStyle);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) 
    {
		switch (ev.getAction()) 
		{
		case MotionEvent.ACTION_DOWN:
		{
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		}
		case MotionEvent.ACTION_MOVE:
		{
			final float curX = ev.getX();
			final float curY = ev.getY();
			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;
			if (xDistance > yDistance) 
			{
				return false;
			}
		}
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
    }
}
