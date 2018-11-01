package com.zhaoqy.app.demo.picture.textdrawable.item;

import android.graphics.drawable.Drawable;

public class DataItem 
{
	private String label;
    private Drawable drawable;
    private int navigationInfo;

    public DataItem(String label, Drawable drawable, int navigationInfo) 
    {
        this.label = label;
        this.drawable = drawable;
        this.navigationInfo = navigationInfo;
    }

    public String getLabel() 
    {
        return label;
    }

    public Drawable getDrawable() 
    {
        return drawable;
    }

    public int getNavigationInfo() 
    {
        return navigationInfo;
    }
}
