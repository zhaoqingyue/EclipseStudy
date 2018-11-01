package com.zhaoqy.app.demo.progress.square.util;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class PercentStyle 
{
	private Paint.Align align;
	private String  customText = "%";
	private float   textSize;
	private boolean percentSign;
	private int     textColor = Color.BLACK;

	public PercentStyle() 
	{
	}

	public PercentStyle(Align align, float textSize, boolean percentSign) 
	{
		super();
		this.align = align;
		this.textSize = textSize;
		this.percentSign = percentSign;
	}

	public Paint.Align getAlign() 
	{
		return align;
	}

	public void setAlign(Paint.Align align) 
	{
		this.align = align;
	}

	public float getTextSize() 
	{
		return textSize;
	}

	public void setTextSize(float textSize) 
	{
		this.textSize = textSize;
	}

	public boolean isPercentSign() 
	{
		return percentSign;
	}

	public void setPercentSign(boolean percentSign) 
	{
		this.percentSign = percentSign;
	}

	public String getCustomText() 
	{
		return customText;
	}

	public void setCustomText(String customText) 
	{
		this.customText = customText;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) 
	{
		this.textColor = textColor;
	}
}
