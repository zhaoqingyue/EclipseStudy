/**
 * 
 * Copyright © 2015GreatVision. All rights reserved.
 *
 * @Title: SideBar.java
 * @Prject: demo
 * @Package: com.zhaoqy.app.demo.page.dianping.view
 * @Description: 侧边索引
 * @author: zhaoqy
 * @date: 2015-12-17 下午4:16:00
 * @version: V1.0
 */

package com.zhaoqy.app.demo.page.dianping.view;

import com.zhaoqy.app.demo.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View 
{
	private OnTouchingLetterChangedListener mOnTouchingLetterChangedListener; 
	
	public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", 
		"O", "P", "Q", "R", "S", "T", "U", "V",  "W", "X", "Y", "Z", "#" };  
	private int choose = -1;
	private Paint paint = new Paint();  

	private TextView mTextDialog;  

	public void setTextView(TextView textDialog) 
	{  
		mTextDialog = textDialog;  
	}  

	public SideBar(Context context, AttributeSet attrs, int defStyle) 
	{  
		super(context, attrs, defStyle);  
	}  

	public SideBar(Context context, AttributeSet attrs) 
	{  
		super(context, attrs);  
	}  

	public SideBar(Context context) 
	{  
		super(context);  
	}  

	 protected void onDraw(Canvas canvas) 
	 {  
		 super.onDraw(canvas);  
		 int height = getHeight();
		 int width = getWidth();
		 int singleHeight = height / b.length;

		 for (int i = 0; i < b.length; i++) 
		 {  
			 paint.setColor(Color.rgb(33, 65, 98));  
			 paint.setTypeface(Typeface.DEFAULT_BOLD);  
			 paint.setAntiAlias(true);  
			 paint.setTextSize(singleHeight-12);  

			 float xPos = width / 2 - paint.measureText(b[i]) / 2;
			 float yPos = singleHeight * i + singleHeight;
			 if (i == choose) 
			 { 
				 float wid=width*2/3;
				 float hei=singleHeight*4/5;
				 float rectXPos=width / 2 - wid / 2;
				 float rectYPos=singleHeight * i + singleHeight;
				 canvas.drawRect(rectXPos, rectYPos-hei, rectXPos+wid, rectYPos, paint);
				 paint.setColor(Color.parseColor("#3399ff"));  
				 paint.setFakeBoldText(true); 
			 }  
			 canvas.drawText(b[i], xPos, yPos, paint);  
			 paint.reset();
		 }  
	 }  

	 @Override  
	 public boolean dispatchTouchEvent(MotionEvent event) 
	 {  
		 final int action = event.getAction();  
		 final float y = event.getY();
		 final int oldChoose = choose;  
		 final OnTouchingLetterChangedListener listener = mOnTouchingLetterChangedListener;  
		 final int c = (int) (y / getHeight() * b.length);
		 switch (action) 
		 {  
		 case MotionEvent.ACTION_UP:  
			 setBackgroundResource(android.R.color.transparent);
			 choose = -1;
			 invalidate();  
			 if (mTextDialog != null) 
			 {  
				 mTextDialog.setVisibility(View.GONE);  
			 }  
			 break;  
		 default:  
			 setBackgroundResource(R.drawable.dianping_city_sidebar_bg);  
			 if (oldChoose != c) 
			 {  
				 if (c >= 0 && c < b.length) 
				 {  
					 if (listener != null) 
					 {  
						 listener.onTouchingLetterChanged(b[c]);
					 }  
					 if (mTextDialog != null) 
					 {  
						 mTextDialog.setText(b[c]);  
						 mTextDialog.setVisibility(View.VISIBLE);  
					 }  
					 choose = c;  
					 invalidate();  
				 }  
			 }  
			 break;  
		 }  
		 return true;  
	 }  

	 public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) 
	 {  
		 mOnTouchingLetterChangedListener = onTouchingLetterChangedListener;  
	 }  

	 public interface OnTouchingLetterChangedListener 
	 {  
		 public void onTouchingLetterChanged(String s);  
	 }  
}
