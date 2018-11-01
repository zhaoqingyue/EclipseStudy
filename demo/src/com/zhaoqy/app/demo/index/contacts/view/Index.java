package com.zhaoqy.app.demo.index.contacts.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Index extends View 
{
	private String  mText[] = new String[26];
	private Paint   mPaint;
	private OnIndex mOnIndex;
	private int     mSeparation;

	public Index(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		init();
	}

	public Index(Context context) 
	{
		super(context);
		init();
	}

	public void init() 
	{
		for (int i=0; i<mText.length; i++) 
		{
			mText[i] = (char)(65+i) + "";
		}
		mPaint = new Paint();
		mPaint.setTextSize(20.0f);
		mPaint.setColor(0xff000000);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		switch (event.getAction()) 
		{
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
		{
			float y = event.getY();
			int index = (int) (y/mSeparation);
			index = index >= mText.length ? mText.length-1 : index;
			mOnIndex.OnIndexSelect(mText[index]);
			break;
		}
		case MotionEvent.ACTION_UP:
		{
			mOnIndex.OnIndexUp();
			break;
		}
		default:
			break;
		}
		return true;
	}

	@Override
	public void draw(Canvas canvas) 
	{
		super.draw(canvas);
		mSeparation = getHeight()/26;
		int width = getWidth();
		for (int i=0; i<mText.length; i++) 
		{
			canvas.drawText(mText[i], width-getFontWidth(mText[i]) >> 1, mSeparation*(i+1), mPaint);
		}
	}

	public int getFontWidth(String str) 
	{
		Rect rect = new Rect();
		mPaint.getTextBounds(str, 0, str.length(), rect);
		return rect.width();
	}

	public void setOnIndex(OnIndex onIndex) 
	{
		mOnIndex = onIndex;
	}
}
